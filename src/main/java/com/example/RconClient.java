package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * RCON client implementation for Source engine games.
 * Implements the RCON protocol as used by Source engine servers.
 */
public class RconClient {
    
    private static final Logger logger = LoggerFactory.getLogger(RconClient.class);
    
    private String host;
    private int port;
    private String password;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    
    // RCON packet types
    private static final int SERVERDATA_AUTH = 3;
    private static final int SERVERDATA_AUTH_RESPONSE = 2;
    private static final int SERVERDATA_EXECCOMMAND = 2;
    private static final int SERVERDATA_RESPONSE_VALUE = 0;
    
    // Request ID counter
    private int requestId = 1;
    
    public RconClient(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }
    
    /**
     * Connect to the RCON server
     */
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(10000); // 10 second timeout
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            logger.info("Connected to RCON server {}:{}", host, port);
            return authenticate();
        } catch (IOException e) {
            logger.error("Failed to connect to RCON server: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Authenticate with the RCON server
     */
    private boolean authenticate() {
        try {
            // Send authentication packet
            byte[] authPacket = createPacket(SERVERDATA_AUTH, password);
            outputStream.write(authPacket);
            outputStream.flush();
            
            // Read response
            RconPacket response = readPacket();
            if (response != null && response.getType() == SERVERDATA_AUTH_RESPONSE) {
                if (response.getRequestId() == -1) {
                    logger.error("Authentication failed - invalid password");
                    return false;
                } else {
                    logger.info("Successfully authenticated with RCON server");
                    return true;
                }
            }
            
            logger.error("Authentication failed - unexpected response");
            return false;
        } catch (IOException e) {
            logger.error("Authentication error: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Send a command to the RCON server
     */
    public String sendCommand(String command) {
        if (socket == null || socket.isClosed()) {
            return "Error: Not connected to server";
        }
        
        try {
            byte[] commandPacket = createPacket(SERVERDATA_EXECCOMMAND, command);
            outputStream.write(commandPacket);
            outputStream.flush();
            
            // Read response
            RconPacket response = readPacket();
            if (response != null) {
                return response.getBody();
            }
            
            return "No response received";
        } catch (IOException e) {
            logger.error("Error sending command: {}", e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Create an RCON packet
     */
    private byte[] createPacket(int type, String payload) {
        byte[] payloadBytes = payload.getBytes();
        int packetSize = 10 + payloadBytes.length;
        
        ByteBuffer buffer = ByteBuffer.allocate(packetSize + 4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        buffer.putInt(packetSize);
        buffer.putInt(requestId++);
        buffer.putInt(type);
        buffer.put(payloadBytes);
        buffer.put((byte) 0); // Null terminator
        buffer.put((byte) 0); // Null terminator
        
        return buffer.array();
    }
    
    /**
     * Read an RCON packet from the socket
     */
    private RconPacket readPacket() throws IOException {
        // Read packet size (4 bytes)
        byte[] sizeBuffer = new byte[4];
        int bytesRead = inputStream.read(sizeBuffer);
        if (bytesRead != 4) {
            logger.error("Failed to read packet size");
            return null;
        }
        
        int packetSize = ByteBuffer.wrap(sizeBuffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
        //logger.debug("Reading packet of size: {}", packetSize);
        
        // Read the rest of the packet
        byte[] packetData = new byte[packetSize];
        bytesRead = inputStream.read(packetData);
        if (bytesRead != packetSize) {
            logger.error("Failed to read complete packet. Expected: {}, Got: {}", packetSize, bytesRead);
            return null;
        }
        
        // Parse packet
        ByteBuffer buffer = ByteBuffer.wrap(packetData).order(ByteOrder.LITTLE_ENDIAN);
        int requestId = buffer.getInt();
        int type = buffer.getInt();
        
        // Read body (everything except the two null terminators)
        byte[] bodyBytes = new byte[packetSize - 8];
        buffer.get(bodyBytes);
        
        // Remove null terminators
        int bodyLength = bodyBytes.length;
        while (bodyLength > 0 && bodyBytes[bodyLength - 1] == 0) {
            bodyLength--;
        }
        
        String body = new String(bodyBytes, 0, bodyLength);
        
        return new RconPacket(requestId, type, body);
    }
    
    /**
     * Disconnect from the RCON server
     */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Disconnected from RCON server");
            }
        } catch (IOException e) {
            logger.error("Error disconnecting: {}", e.getMessage());
        }
    }
    
    /**
     * Inner class to represent an RCON packet
     */
    private static class RconPacket {
        private final int requestId;
        private final int type;
        private final String body;
        
        public RconPacket(int requestId, int type, String body) {
            this.requestId = requestId;
            this.type = type;
            this.body = body;
        }
        
        public int getRequestId() { return requestId; }
        public int getType() { return type; }
        public String getBody() { return body; }
    }

    public void setBlock(int wx, int wy, int wz, String mat){
        //System.out.println("setting block at: "+wx+", "+wy+", "+wz);
        String response = sendCommand("setblock "+wx+" "+wy+" "+wz+" "+ mat);
        System.out.println(response);
    }
} 