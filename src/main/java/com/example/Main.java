package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.Set;
import java.util.Vector;
import java.util.HashSet;
import java.util.Arrays;


/**
 * Main class for the Java RCON project.
 * This demonstrates connecting to a server and querying active players.
 */
public class Main {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        logger.info("Starting Java RCON Project");
        
        // Load configuration
        Properties config = loadConfiguration();
        String serverHost = config.getProperty("server.host", "voidrunner.zapto.org");
        int serverPort = Integer.parseInt(config.getProperty("server.port", "25575"));
        String rconPassword = config.getProperty("server.rcon.password", "password");
        
        System.out.println("Connecting to server: " + serverHost + ":" + serverPort);
        
        // Check if password is set
        if ("password".equals(rconPassword)) {
            System.out.println("WARNING: Please set the RCON password in src/main/resources/application.properties");
            System.out.println("Current password is the default placeholder value.");
            return;
        }
        
        // Create RCON client
        RconClient rconClient = new RconClient(serverHost, serverPort, rconPassword);
        try {
      
            // Connect to the server
            if (rconClient.connect()) {
                logger.info("Successfully connected to server");
                
                // do Somethin
                int height = 16;
                int width = 24;
                
                int startX = 1300; 
                int startY = 70;
                int startZ = -1040; 
                
                Matrix4 base = new Matrix4();
                base.translate(startX, startY, startZ);
                base.rotateX(Math.PI/2);


                String command = "forceload add "+startX+" "+startZ+" "+(startX+width*11)+" "+(startZ+height*11);
                String response = rconClient.sendCommand(command);
                System.out.println(command);
                System.out.println(response);
                System.out.println("StartX: "+startX+" StartZ: " + startZ + 
                                    " startX+width*11: "+startX+width*11 + " startZ+height*11"+startZ+height*11);

                for (int col = 1; col <= 1; col++){
                   
                    for(int row = 1; row <= 10; row++){
                        Complex c = new CardioidToRect().toCardioid(new Complex(1,(double)(row)/10));
                        jool(0, 0, 0, width, height, c, base, rconClient);
                        base.translate(width, 0, 0);
                        base.setRotationY(Math.sin(Math.PI*(double)(row)/5));
                        //if(col == 1) System.out.println("row: "+row+", angle: "+Math.sin(Math.PI*(double)(row)/2.5));
                        
                    }
                    base.rotateX(-Math.PI/2);
                    base.setTranslation(startX, startY, startZ+height*col);
                    base.rotateX(Math.PI/2);
                }
 
                /*
                for (int i = 0; i<4; i++){
                    for (int j = 0; j <4; j++){
                        System.out.print(base.m[i][j] + "  ");
                    }
                    System.out.println();
                }
                */
                //base.rotateX(Math.PI/3);
                /*
                for (int i = 0; i<=3; i++){
                    double[]latitude = {1,0,0,0};
                    double[]longitude = {0,0,-1,-i};
                    double[]radial = {0,1,0,0};
                    Complex c = new CardioidToRect().toCardioid(new Complex((double)i/3,(double)i/3));

                    Basis b = new Basis(latitude, longitude, radial);

                    joolTangential(b,1420, 100, -350, c, rconClient);
                }
                */
                //sphereJool(1400, 170, 220, 50, new Complex(.1,.1), rconClient);
                //sphericalCuboids(1650, 180, 300, 50, rconClient);
                //erase(1300, 137, 413, 1850, 100,460, rconClient);
                //createSphere(1100, -85, 448, -120, 120, -120, 1.5, rconClient);
                //polarSphere(2030, 71, 0,20, rconClient);
                //oldJool(1540, 0, 127,120, 80, ctr.toCardioid(new Complex(.8,.8)), rconClient);
                
                /*
                for (int i = 0; i<20; i++){
                    Complex c = ctr.toCardioid(new Complex((double)i/20, (double)i/20));
                    oldJool(1950+i, 420+i, 100+i,100-2*i, 50-2*i, c, rconClient);
                }
                */
                /*
                System.out.println("start sphering");
                createSphere(670,78,140,50 ,.05, rconClient );
                System.out.println("sphering done!");
                 */
            }
            else {
                logger.error("Failed to connect to server");
            }
        }
        catch (Exception e) {
            logger.error("Error connecting to server: {}", e.getMessage());
        }
        finally {
            // Always disconnect
            rconClient.disconnect();
            logger.info("Disconnected from server");
        }
        
    }
    
    static void joolTangential(Basis b, int xCenter, int yCenter, int zCenter, Complex c, RconClient rconCLient){
        // place fractals of 120x80 blocks tangential in a circle radius 230 
        
        double[] center = new double[]{b.n[0], b.n[1], b.n[2]};

        double zoom = .8;
        
        int width = 60;
        int height = 40;
        int maxIter = 50;
        
        //normalize width to range -1.5 to 1.5
        //normalize height to range -1 to 1
        double reStep = 3.0 / width;
        double imStep = 2.0 / height;
        
        for (int px = 0; px <= width; px++){
            double re = (px * reStep - 1.5) / zoom ;
            
            for (int py = 0; py <= height; py++){
                double im = (py * imStep - 1) / zoom;
                /*
                double newRe = re;
                double newIm = im;
                
                int i;
                for (i = 0; i < maxIter; i++){
                    double oldRe = newRe;
                    double oldIm = newIm;
                    
                    double r2 = oldRe * oldRe;
                    double i2 = oldIm * oldIm; 
                    
                    if (r2 + i2 > 4) break; // Fluchtbedingung
                    
                    newRe = r2 - i2 + c.x;
                    newIm = 2.0 * oldRe * oldIm + c.y;
                    }
                */
                    // Farb-/Materialentscheidung: Teil der Menge?
                int i = calcJulia(re, im, c, maxIter);
                boolean inside = (i == maxIter);
                String material;
                String command;
                String response;

                double wx = center[0] + (px-width/2)*b.u[0] + (py-height/2)*b.v[0];
                double wy = center[1] + (px-width/2)*b.u[1] + (py-height/2)*b.v[1];
                double wz = center[2] + (px-width/2)*b.u[2] + (py-height/2)*b.v[2];
                //System.out.println("center[2]: "+center[2] +", b.u[2]" +b.u[2]+", b.v[2]:"+b.v[2]+", wz:"+wz);
                
                int blockX = iround(wx) + xCenter;
                int blockY = iround(wy) + yCenter;
                int blockZ = iround(wz) + zCenter;
                
                if (inside) {material = " green_stained_glass";}
                else if(i == 0) {material = " air";}
                else {
                    material = " minecraft:red_concrete";
                }
                command = "setblock "+blockX+" "+blockY+" "+blockZ + material;
                response = rconCLient.sendCommand(command);
                System.out.println(response);
                
                
            }
        }
    }


    static void jool(int startX, int startY, int startZ, int width, int height, Complex c, Matrix4 base, RconClient rconClient){

        int maxIter = 50;
        double zoom = .8;

        int centerX = startX + width/2;
        int centerY = startY + height/2;
        int endX = startX + width;
        int endY = startY + height;
        
        //normalize width to -1.5 to 1.5
        // normalize height to -1 to 1
        double deltaRe = 3 / (double)width ;
        double deltaIm = 2 / (double)height ;

        for (int px = 0; px < width; px ++){
            double re = (px * deltaRe - 1.5) / zoom;

            for (int py = 0; py < height; py++){
                double im = (py * deltaIm - 1) / zoom;
                int escapeTime = calcJulia(re, im, c, maxIter);
                
                boolean inside = (escapeTime == maxIter);
                String material;
                String command;
                String response;

                Vector4 pixel = new Vector4(px, py, 1);
                pixel.multiplyMatrix(base);
/*
                double wx = base.m[0][0]*px + base.m[0][1]*px + base.m[0][2]*px + base.m[0][3]*px; 
                double wy = base.m[1][0]*py + base.m[1][1]*py + base.m[1][2]*py + base.m[1][3]*py;
                double wz = base.m[2][0] + base.m[2][1] + base.m[2][2] + base.m[2][2];
                //System.out.println("center[2]: "+center[2] +", b.u[2]" +b.u[2]+", b.v[2]:"+b.v[2]+", wz:"+wz);
 */                
                int blockX = iround(pixel.v[0]);
                int blockY = iround(pixel.v[1]);
                int blockZ = iround(pixel.v[2]);
                
                if (inside) {material = " minecraft:blue_stained_glass";}
                else if(escapeTime == 0) {material = " minecraft:blue_stained_glass";}
                else {material = " minecraft:brown_concrete";}
                command = "setblock "+blockX+" "+blockY+" "+blockZ + material;
                response = rconClient.sendCommand(command);
                System.out.println(response);
            }
        }

    }

    static int calcJulia(double re, double im, Complex c, int maxIter){
        double newRe = re;
        double newIm = im;
        //System.out.println("re: "+re+", im: "+im);
        
            for (int i = 0; i < maxIter; i++){
                double oldRe = newRe;
                double oldIm = newIm;
                
                double r2 = oldRe * oldRe;
                double i2 = oldIm * oldIm; 
                
                if (r2 + i2 > 4) {return i;}// Fluchtbedingung
                
                newRe = r2 - i2 + c.x;
                newIm = 2.0 * oldRe * oldIm + c.y;
            }
            return maxIter;
    }
    
    static void sphericalCuboids(int xCenter, int yCenter, int zCenter, int radius, RconClient rconCLient){
        // lets place 12 panes aquatorial
        // going up by 30 degrees PI/6 8 panes
        // and at 60 degrees PI/3 4 Panes
        // one at the poles
        
        int thetaSteps = 6;
        int[] phiSteps = {1, 4, 8, 12, 8, 4, 1};

        double deltaTheta = Math.PI / thetaSteps;
        
        for (int i = 0; i <= thetaSteps; i++){
            double theta = i * deltaTheta;
            double cth = Math.cos(theta);
            double sth = Math.sin(theta);
            double deltaPhi = 2 * Math.PI / (phiSteps[i]);
            
            for (int j = 0; j < phiSteps[i]; j++){
                double phi = j * deltaPhi;
                double cph = Math.cos(phi);
                double sph = Math.sin(phi);
                placeCuboidTangential(radius, theta, phi, new int[]{xCenter, yCenter, zCenter}, rconCLient);
            }
        }
    }

    static void sphereJool(int xCenter, int yCenter, int zCenter, int radius, Complex startC, RconClient rconCLient){
        int thetaSteps = 6;
        int[] phiSteps = {1, 4, 8, 12, 8, 4, 1};
        int sum = Arrays.stream(phiSteps).sum();
        double deltaIntricacy = (1-startC.x) / sum;
        double deltaCharacter = (1-startC.y) / sum;
        Complex c = startC;
        CardioidToRect ctr = new CardioidToRect();

        double deltaTheta = Math.PI / thetaSteps;
        
        for (int i = 0; i <= thetaSteps; i++){
            double theta = i * deltaTheta;
            double cth = Math.cos(theta);
            double sth = Math.sin(theta);
            double deltaPhi = 2 * Math.PI / (phiSteps[i]);
            
            for (int j = 0; j < phiSteps[i]; j++){
                double phi = j * deltaPhi;
                double cph = Math.cos(phi);
                double sph = Math.sin(phi);
                joolTangential(basisLLR(theta, phi, radius), xCenter, yCenter, zCenter, ctr.toCardioid(c), rconCLient);
                c = new Complex(c.x + deltaCharacter, c.y + deltaIntricacy);
            }
            
        }
    }

    static void placeCuboidTangential(int r, double theta, double phi, int[] center, RconClient rconClient) {
        
        int width = 30;
        int height = 30;
        int thickness = 1;

        Basis B = basisLL(theta, phi);
        double[] C = new double[]{ r*B.n[0], r*B.n[1], r*B.n[2] };

        for (int x = -width/2; x <= width/2; x++) {
            for (int y = -height/2; y <= height/2; y++) {
                for (int z = 0; z < thickness; z++) { 
                    double px = C[0] + x*B.u[0] + y*B.v[0] + z*B.n[0];
                    double py = C[1] + x*B.u[1] + y*B.v[1] + z*B.n[1];
                    double pz = C[2] + x*B.u[2] + y*B.v[2] + z*B.n[2];
                    //System.out.println(z);
                    int wx = iround(px), wy = iround(py), wz = iround(pz);
                    rconClient.setBlock(wx + center[0], wy+ center[1], wz + center[2], "minecraft:magenta_concrete");
                }
            }
        }
    }

    private static void oldJool(int startX, int startZ, int yLevel, int width, int depth, Complex c, double rotation,  RconClient rconClient){
        
        int maxIter = 50;
        // Skalierung von Minecraft-Koordinaten auf den Fraktalbereich
        double zoom = .8; // je größer, desto "näher" dran
        double moveX = 0.0;
        double moveZ = 0.0;
        int py = 0;
    
        for (int px = 0; px < width; px++) {
            for (int pz = 0; pz < depth; pz++) {
                // Normalisierte Koordinaten (-1 bis +1)
                double newRe = 1.5 * (px - width / 2.0) / (0.5 * zoom * width) + moveX;
                double newIm = (pz - depth / 2.0) / (0.5 * zoom * depth) + moveZ;
                int i;
                    for (i = 0; i < maxIter; i++) { // Iterationstiefe
                        double oldRe = newRe;
                        double oldIm = newIm;
    
                        double r2 = oldRe * oldRe;
                        double i2 = oldIm * oldIm;
    
                        if (r2 + i2 > 4) break; // Fluchtbedingung
    
                        newRe = r2 - i2 + c.x;
                        newIm = 2.0 * oldRe * oldIm + c.y;
                    }
                    // Farb-/Materialentscheidung: Teil der Menge?
                    boolean inside = (i == maxIter);
                    
                    // lets rotate this sculpture 45° around the x-axis. Means the x-values stay constant
                    // y and z-values change
                    // normalize the width(x-value) to 1, then add sinus and cos values of the arc to each block
                    // dependent of their relation to the axis
                    // 
                    // calculate the vectorLength of each block as multiplicator for the shift-amount
                    
                    // for rotation purpose normalize pz to width/2 // rotate by the center of the plane
                    // center rotation axis for z-values
                    double dz = pz - width/2; 
                    double dy = py; // no height-parameter
                    double vectorLength = dz;// Math.sqrt(dz*dz + dy*dy); always positive
                    double arc = rotation;
                    
                    dz = Math.cos(arc) * vectorLength + width/2; 
                    dy = Math.sin(arc) * vectorLength;
    
                    int blockX = startX + px;
                    int blockY  = (int)(yLevel + dy);
                    int blockZ = (int)(startZ + dz);
                                        
                    String material;
                    String command;
                    
                    if(inside){ 
                        material = " light_blue_stained_glass";
                        command = "setblock " + blockX + " " + blockY + " " + blockZ + material;
                    }
                    else if( i == 0){
                        material = " air";
                        command = "setblock " + blockX + " " + blockY + " " + blockZ + material;
                    }
                    else{
                        material = " minecraft:orange_concrete";
                        command = "setblock "+ blockX + " " + blockY + " " + blockZ + material;                        
                    }
                    String response = rconClient.sendCommand(command);
                    logger.info(command + ", "+response);
                }
            }
        }
        
    static int iround(double x){ return (int)Math.round(x); }
        
    private static void polarSphere(int xCenter, int yCenter, int zCenter, int radius, RconClient rconClient){
        int polarSteps = radius;
        int azimutalSteps;
        double azimutalStepWidth;
        double cth;
        double sth;
        double cph;
        double sph;
        double polarStepWidth = Math.PI/2/polarSteps; // quarter circle going from 0 to 90 degrees

        int blockX;
        int blockY;
        int blockZ;
        String command;
        String response;

        for (int i = 0; i <= polarSteps; i++){
            cth = Math.cos(i * polarStepWidth);
            sth = Math.sin(i * polarStepWidth);
            
            azimutalSteps = (int)(cth * 6 * radius); //to fill each block should need cth * 2 * PI * radius
            azimutalStepWidth = 2 * Math.PI/azimutalSteps;
            //System.out.println(i +", "+ cth);
                
            for (int j = 0; j < azimutalSteps; j++){
                cph = Math.cos(j * azimutalStepWidth);
                sph = Math.sin(j * azimutalStepWidth);
                blockX = (int)Math.round(cph * cth * radius + xCenter);
                blockY = (int)Math.round(sth * radius + yCenter);
                blockZ = (int)Math.round(sph * cth* radius + zCenter);
                
               
                command = "setblock "+blockX+" "+blockY+" "+blockZ+" deepslate_emerald_ore";
                
                response = rconClient.sendCommand(command);
                System.out.println(command +", "+ response);
            }
        } 

/*        int density = 30;
        // create a sphere from walking over one axis creating rings of radia sinalpha at step cosalpha
        // specify density in degrees, 90 means 90 rings half the sphere, 1 means only the center and the pivot-point, 30 means 3 rings a side
        // number of rings and resolution of rings stays related for now

        // calculate how many rings there is to be rendered
        int numberOfRings = 90/density;

        //iterate over the number of rings, making sure, first iteration is at 0, last iteration at 90 degrees
        for (int i = 0; i < numberOfRings; i++){
            double radialAngle = i * density /180 * Math.PI;
            double xTarget = Math.cos(radialAngle);
            double ortogonalRadius = Math.sin(radialAngle);

            for (int j = 0; j < numberOfRings; j++){
                double ortogonalAngle = j * density / 180 * Math.PI;
            
                double yTarget = Math.sin(ortogonalAngle);
                double zTarget = Math.cos(ortogonalAngle);
                
                int blockX = (int)(radius * xTarget + xCenter);
                int blockY = (int)(radius * yTarget + yCenter);
                int blockZ = (int)(radius * zTarget + z,Center);

                //drawPane(rotation: ortogonal to vector to 0,0,0)


                String command = "setblock "+blockX+" "+blockY+" "+blockZ+" "+" minecraft:fire_coral_block";
                String response = rconClient.sendCommand(command);
                System.out.println(response);
            }
        }
    
         */
    }
    private static void oldJool(int startX, int startZ, int yLevel, int width, int depth, Complex c,  RconClient rconClient){
        oldJool(startX, startZ, yLevel, width, depth, c, 0, rconClient);
    }
    
/*
    private static void jool(int x1, int z1, int y, int x2, int z2, RconClient rconClient){
        jool(x1, z1, y, x2, z2, rconClient, new Complex(.3, .05));
    }

    private static void jool(int x1, int z1, int altidute, int width, int depth, RconClient rconClient, Complex c){
       int x2 = x1 + width;
       int z2 = z1 + depth;
       
       int xMax = Math.max(x1,x2);
       int zMax = Math.max(z1,z2);
       int xMin = Math.min(x1,x2);
       int zMin = Math.min(z1,z2);
        
       int iterationDepth = 50;
            
            for (int x = xMin; x <= xMax; x++){
                for (int z = zMin; z <= zMax; z++){
                    double re = ((((double)(x - x1) / (double)(x2 - x1)) * 3) - 1.5);
                    double im = ((((double)(z - z1) / (double)(z2 - z1)) * 2) - 1);
                    for (int i = 0; i < iterationDepth; i++){
                        double reTemp = re;
                         re = re*re + im*im + c.x;
                         im = 2*reTemp*im + c.y;
                         
                         if (re*re + im*im > 4 || i == iterationDepth-1){
                            break;
                        }
                            
                        String command = "setblock " + x + " " + y + " " + z +" minecraft:purple_concrete";
                        System.out.println(command);
                        String response = rconClient.sendCommand(command);
                        logger.info(response);
                        break;
                        
                    }
                }
            
        }
        
        
    }
    */ 
    private static void createSphere(int centerX, int centerY, int centerZ, int radius, double shell, RconClient rconClient) {
        double stepwidth = 1.0 / (2.0 * radius);
    
        for (double y = 1.0; y >= -1.0; y -= stepwidth) {
            for (double x = 1.0; x >= -1.0; x -= stepwidth) {
                for (double z = 1.0; z >= -1.0; z -= stepwidth) {
                    double r2 = x * x + y * y + z * z;
                    
                    int worldX = (int) Math.round(x * radius + centerX);
                    int worldY = (int) Math.round(y * radius + centerY);
                    int worldZ = (int) Math.round(z * radius + centerZ);

                    if (r2 < 1.0 && r2 > 1.0 - shell) {
                        
    
                        String command = "setblock " + worldX + " " + worldY + " " + worldZ + " minecraft:lapis_ore";
                        String response = rconClient.sendCommand(command);
                    }
                    /* 
                    else {
                        String command = "setblock " + worldX + " " + worldY + " " + worldZ + " minecraft:air";
                        String response = rconClient.sendCommand(command);
                        logger.info(response);
                    }
                    */
                }
            }
        }
    }

    private static void createSphere(int x1, int y1, int z1, int width, int height, int depth, double shellStrength, RconClient rconClient){
        int x2 = x1 + width;
        int y2 = y1 + height;
        int z2 = z1 + depth;

        int xMin = Math.min(x1,x2);
        int xMax = Math.max(x1,x2);
        int yMin = Math.min(y1,y2);
        int yMax = Math.max(y1,y2);
        int zMin = Math.min(z1,z2);
        int zMax = Math.max(z1,z2);

        String command = "forceload add "+xMin+" "+zMin+" "+xMax+" "+zMax;
        String response = rconClient.sendCommand(command);
        System.out.println(response);

        //create a grid around the construction area
        command = "fill "+(x1-1)+" "+(y1-1)+" "+(z1-1)+" "+(x2+1)+" "+(y1-1)+" "+(z1-1)+" diamond_block"; // line 1 front top
        response = rconClient.sendCommand(command);
        System.out.println(command);

        command = "fill "+(x1-1)+" "+(y1-1)+" "+(z1-1)+" "+(x1-1)+" "+(y2+1)+" "+(z1-1)+" diamond_block"; // line 2 front left
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x1-1)+" "+(y1-1)+" "+(z1-1)+" "+(x1-1)+" "+(y1-1)+" "+(z2+1)+" diamond_block"; // line 3 left front to back top
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x1-1)+" "+(y2+1)+" "+(z1-1)+" "+(x2+1)+" "+(y2+1)+" "+(z1-1)+" diamond_block"; // line 4 front bottom
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x1-1)+" "+(y2+1)+" "+(z1-1)+" "+(x1-1)+" "+(y2+1)+" "+(z2+1)+" diamond_block"; // line 5 left front to back bottom
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x2+1)+" "+(y2+1)+" "+(z1-1)+" "+(x2+1)+" "+(y2+1)+" "+(z2+1)+" diamond_block"; // line 6 right front to back bottom
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x2+1)+" "+(y1-1)+" "+(z1-1)+" "+(x2+1)+" "+(y2+1)+" "+(z1-1)+" diamond_block"; // line 7 right front 
        response = rconClient.sendCommand(command);
        System.out.println(response);
        
        command = "fill "+(x2+1)+" "+(y2+1)+" "+(z1-1)+" "+(x2+1)+" "+(y2+1)+" "+(z2+1)+" diamond_block"; // line 8 right front to back bottom
        response = rconClient.sendCommand(command);
        System.out.println(response);
        
        command = "fill "+(x1-1)+" "+(y1-1)+" "+(z2+1)+" "+(x2+1)+" "+(y1-1)+" "+(z2+1)+" diamond_block"; // line 9  upper back 
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x1-1)+" "+(y1-1)+" "+(z2+1)+" "+(x1-1)+" "+(y2+1)+" "+(z2+1)+" diamond_block"; // line 10  left  back 
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x2+1)+" "+(y1-1)+" "+(z2+1)+" "+(x2+1)+" "+(y1-1)+" "+(z2+1)+" diamond_block"; // line 11  right back 
        response = rconClient.sendCommand(command);
        System.out.println(response);

        command = "fill "+(x1-1)+" "+(y2+1)+" "+(z2+1)+" "+(x2+1)+" "+(y2+1)+" "+(z2+1)+" diamond_block"; // line 12 bottom back 
        response = rconClient.sendCommand(command);
        System.out.println(response);


        
        for(int y = yMax; y >= yMin; y--){
            for(int z = zMin; z <= zMax; z++){
                for (int x = xMin; x <= xMax; x++){
                    double cartesianX = (double) (x - xMin) / (double) (xMax - xMin) * 2 -1;
                    double cartesianY = (double) (y - yMin) / (double) (yMax - yMin) * 2 -1;
                    double cartesianZ = (double) (z - zMin) / (double) (zMax - zMin) * 2 -1;
                    double cartesianShellStrength = (double) shellStrength / (double) (xMax - xMin);
                    
                    if(1-cartesianShellStrength < cartesianX*cartesianX + cartesianY*cartesianY + cartesianZ*cartesianZ && 
                        cartesianX*cartesianX + cartesianY*cartesianY + cartesianZ*cartesianZ < 1+cartesianShellStrength){
                            command = "setblock "+ x +" "+y+" "+z+" "+ "amethyst_block replace";
                            response = rconClient.sendCommand(command);
                            System.out.println(response);
                    }
                    else{
                        command = "setblock "+ x +" "+y+" "+z+" "+ "air";
                        response = rconClient.sendCommand(command);
                        System.out.println(response);
                    }
                }
            }
        }
        
    }
    
    private static void erase(int x1, int y1, int z1, int x2, int y2, int z2, RconClient rconClient){
        int xMin = Math.min(x1,x2);
        int xMax = Math.max(x1,x2);
        int yMin = Math.min(y1,y2);
        int yMax = Math.max(y1,y2);
        int zMin = Math.min(z1,z2);
        int zMax = Math.max(z1,z2);
        String command = "forceload add "+(xMin-1)+" "+(zMin-1)+" "+(xMax+1)+" "+(zMax+1);
        String response = rconClient.sendCommand(command);
        System.out.println(response);
        /*
        //create walls arount the area to be erased
        command = "fill " + (xMin-1) + " " + (yMin-1) + " "+ (zMin-1) + " "+ (xMax+1) + " " + yMax + " "+ (zMin-1) +" stone"; //wall 1
        response = rconClient.sendCommand(command);
        System.out.println(response);
        command = "fill " + (xMin -1) + " " + (yMin-1) + " "+ zMin + " "+ (xMin - 1) + " " + yMax + " "+ (zMax + 1) +" stone"; //wall 2 left wall
        response = rconClient.sendCommand(command);
        System.out.println(response);
        command = "fill " + (xMax +1) + " " + (yMin-1) + " "+ zMin + " "+ (xMax + 1) + " " + yMax + " "+ (zMax + 1) +" stone"; //wall 3 right wall
        response = rconClient.sendCommand(command);
        System.out.println(response);
        command = "fill " + (xMin -1) + " " + (yMin-1) + " "+ (zMax+1) + " "+ (xMax + 1) + " " + yMax + " "+ (zMax + 1) +" stone"; //wall 4 back wall
        response = rconClient.sendCommand(command);
        System.out.println(response);
        command = "fill " + (xMin) + " " + (yMin-1) + " "+ (zMin) + " "+ (xMax) + " " + (yMin-1) + " "+ (zMax) +" stone"; //ground
        response = rconClient.sendCommand(command);
        System.out.println(response);
         */
        for(int i = yMax; i >= yMin; i--){
            command = "fill " + xMin + " " + i + " "+ zMin + " "+ xMax + " " + i + " "+ zMax +" air"; //ground
            response = rconClient.sendCommand(command);
            System.out.println("fill air: "+response);
            
        }
    }
    
    /**
     * Load configuration from application.properties
     */
    private static Properties loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                logger.info("Configuration loaded successfully");
            } else {
                logger.warn("Configuration file not found, using defaults");
            }
        } catch (IOException e) {
            logger.warn("Error loading configuration: {}", e.getMessage());
        }
        return properties;
    }
    
    /**
     * Check if a player has reached the specified height
     */
    private static boolean hasPlayerReachedHeight(RconClient rconClient, String playerName, int targetHeight) {
        try {
            // Get player's position
            String command = "execute as " + playerName + " run data get entity @s Pos";
            String response = rconClient.sendCommand(command);
            
            System.out.println("Position data for " + playerName + ": " + response);
            
            // Parse the position from response
            if (response.contains("[")) {
                String posPart = response.substring(response.indexOf("["));
                String[] coords = posPart.replaceAll("[\\[\\]d]", "").split(",");
                
                if (coords.length >= 3) {
                    double x = Double.parseDouble(coords[0].trim());
                    double y = Double.parseDouble(coords[1].trim());
                    double z = Double.parseDouble(coords[2].trim());
                    
                    System.out.println("Player " + playerName + " position: X=" + x + ", Y=" + y + ", Z=" + z);
                    
                    // Check if player has reached the target height
                    boolean reachedHeight = (y >= targetHeight);
                    
                    return reachedHeight;
                }
            }
            
            return false;
        } catch (Exception e) {
            logger.error("Error checking player height: {}", e.getMessage());
            return false;
        }
    }

    static Basis basisLL(double theta, double phi) {
        double cth = Math.cos(theta), sth = Math.sin(theta);
        double cph = Math.cos(phi),   sph = Math.sin(phi);
    
        double[] n = new double[]{ cph*sth, sph*sth, cth };                 // radial
        double[] u = new double[]{ -sph,     cph,     0   };                // Osten
        double[] eTheta = new double[]{ cph*cth, sph*cth, -sth };           // Süden
        double[] v = new double[]{ -eTheta[0], -eTheta[1], -eTheta[2] };    // Norden
    
        return new Basis(u, v, n);
    }
    static Basis basisLL(){
        return basisLL(0,0);
    }
    static Basis basisLLR(double theta, double phi, double radius){
        Basis bLL = basisLL(theta, phi);
        return new Basis(bLL.u, bLL.v, new double[]{bLL.n[0]*radius, bLL.n[1]*radius, bLL.n[2]*radius});
    }
}
    
    /**
      * Create a winding path beneath the player to the ground level
      */
      /*
     private static boolean createWindingPathBeneathPlayer(RconClient rconClient, String playerName) {
                 try {
             System.out.println("🛤️ Starting winding path creation for " + playerName);
             
             // Get player's position
             String posCommand = "execute as " + playerName + " run data get entity @s Pos";
             String posResponse = rconClient.sendCommand(posCommand);
             
             if (!posResponse.contains("[")) {
                 System.out.println("❌ Could not get player position");
                 return false;
             }
             
             // Parse position
             String posPart = posResponse.substring(posResponse.indexOf("["));
             String[] posCoords = posPart.replaceAll("[\\[\\]d]", "").split(",");
             
             if (posCoords.length < 3) {
                 System.out.println("❌ Invalid position data");
                 return false;
             }
             
             double playerX = Double.parseDouble(posCoords[0].trim());
             double playerY = Double.parseDouble(posCoords[1].trim());
             double playerZ = Double.parseDouble(posCoords[2].trim());
             
             System.out.println("Player " + playerName + " position: X=" + playerX + ", Y=" + playerY + ", Z=" + playerZ);
             
             // Find the actual ground level at the player's location
             double groundLevel = findGroundLevel(rconClient, (int)playerX, (int)playerZ);
             
             System.out.println("📍 Creating winding path from Y=" + (int)playerY + " to ground level Y=" + (int)groundLevel);
             
             // Create winding path parameters
             int pathRadius = 3; // Radius of the spiral
             int stepHeight = 1; // Height difference between steps
             double angleStep = Math.PI / 4; // 45 degrees per step
             
             boolean success = true;
             int currentY = (int) playerY - 1; // Start one block below player
             double currentAngle = 0;
             int stepCount = 0;
             
             // Create spiral staircase down to ground level
             while (currentY > groundLevel && stepCount < 100) { // Safety limit
                 // Calculate position on the spiral
                 double offsetX = Math.cos(currentAngle) * pathRadius;
                 double offsetZ = Math.sin(currentAngle) * pathRadius;
                 
                 int pathX = (int) (playerX + offsetX);
                 int pathZ = (int) (playerZ + offsetZ);
                 
                 // Create the step (platform)
                 for (int x = -1; x <= 1; x++) {
                     for (int z = -1; z <= 1; z++) {
                         int blockX = pathX + x;
                         int blockZ = pathZ + z;
                         
                         // Use different materials for visual effect
                         String material = "stone_bricks";
                         if (stepCount % 4 == 0) {
                             material = "quartz_block";
                         } else if (stepCount % 4 == 1) {
                             material = "dark_oak_planks";
                         } else if (stepCount % 4 == 2) {
                             material = "birch_planks";
                         }
                         
                         String setBlockCommand = "setblock " + blockX + " " + currentY + " " + blockZ + " " + material;
                         String response = rconClient.sendCommand(setBlockCommand);
                         
                         if (response.contains("error") || response.contains("Error")) {
                             System.out.println("Warning: Failed to set block at " + blockX + "," + currentY + "," + blockZ);
                         }
                     }
                 }
                 
                 // Add railing on the outer edge
                 double outerAngle = currentAngle + Math.PI / 8; // Slightly offset for railing
                 double railingX = Math.cos(outerAngle) * (pathRadius + 1);
                 double railingZ = Math.sin(outerAngle) * (pathRadius + 1);
                 
                 int railingBlockX = (int) (playerX + railingX);
                 int railingBlockZ = (int) (playerZ + railingZ);
                 
                 // Create railing posts
                 for (int railY = currentY; railY <= currentY + 2; railY++) {
                     String railingCommand = "setblock " + railingBlockX + " " + railY + " " + railingBlockZ + " oak_fence";
                     String railingResponse = rconClient.sendCommand(railingCommand);
                     
                     if (railingResponse.contains("error") || railingResponse.contains("Error")) {
                         System.out.println("Warning: Failed to set railing at " + railingBlockX + "," + railY + "," + railingBlockZ);
                     }
                 }
                 
                 // Add some decorative lighting
                 if (stepCount % 3 == 0) {
                     String lightCommand = "setblock " + pathX + " " + (currentY + 1) + " " + pathZ + " lantern";
                     String lightResponse = rconClient.sendCommand(lightCommand);
                     
                     if (lightResponse.contains("error") || lightResponse.contains("Error")) {
                         // Fallback to torch if lantern fails
                         String torchCommand = "setblock " + pathX + " " + (currentY + 1) + " " + pathZ + " torch";
                         rconClient.sendCommand(torchCommand);
                     }
                 }
                 
                 // Move to next step
                 currentY -= stepHeight;
                 currentAngle += angleStep;
                 stepCount++;
                 
                 // Add a small delay to prevent overwhelming the server
                 Thread.sleep(50);
             }
             
             // Create a landing platform at the bottom
             for (int x = -2; x <= 2; x++) {
                 for (int z = -2; z <= 2; z++) {
                     int landingX = (int) playerX + x;
                     int landingZ = (int) playerZ + z;
                     
                     String landingCommand = "setblock " + landingX + " " + (int)groundLevel + " " + landingZ + " polished_granite";
                     String landingResponse = rconClient.sendCommand(landingCommand);
                     
                     if (landingResponse.contains("error") || landingResponse.contains("Error")) {
                         System.out.println("Warning: Failed to set landing block at " + landingX + "," + (int)groundLevel + "," + landingZ);
                        }
                 }
             }
             
             // Add a decorative centerpiece at the landing
             String centerCommand = "setblock " + (int)playerX + " " + ((int)groundLevel + 1) + " " + (int)playerZ + " beacon";
             String centerResponse = rconClient.sendCommand(centerCommand);
             
             if (centerResponse.contains("error") || centerResponse.contains("Error")) {
                 // Fallback to a different decorative block
                 String fallbackCommand = "setblock " + (int)playerX + " " + ((int)groundLevel + 1) + " " + (int)playerZ + " diamond_block";
                 rconClient.sendCommand(fallbackCommand);
             }
             
             // Notify the player
             String notifyCommand = "tell " + playerName + " A magical winding path has appeared beneath you! Follow it down!";
             String notifyResponse = rconClient.sendCommand(notifyCommand);
             System.out.println("Notify response: " + notifyResponse);
             
             // Add particle effects along the path
             for (int i = 0; i < 5; i++) {
                 double particleAngle = i * Math.PI / 2;
                 double particleX = Math.cos(particleAngle) * pathRadius;
                 double particleZ = Math.sin(particleAngle) * pathRadius;
                 
                 String particleCommand = "particle minecraft:end_rod " + 
                     ((int)playerX + (int)particleX) + " " + ((int)playerY - 2) + " " + 
                     ((int)playerZ + (int)particleZ) + " 1 1 1 0.1 20";
                 String particleResponse = rconClient.sendCommand(particleCommand);
                 System.out.println("Particle response: " + particleResponse);
                 
                 Thread.sleep(100);
             }
             
             return success;
             
         } catch (Exception e) {
             logger.error("Error creating winding path: {}", e.getMessage());
             return false;
         }
     }
     
     private static void createSphere(int centerX, int centerY, int centerZ, int radius, RconClient rconClient){
        createSphere(centerX, centerY, centerZ, radius, .1, rconClient);
     }

    
    /**
* Find the actual ground level at the specified coordinates
*/
/*
    private static double findGroundLevel(RconClient rconClient, int x, int z) {
         try {
             System.out.println("🔍 Finding ground level at X=" + x + ", Z=" + z);
             
             // Since we know sea level is at Y=60, let's check around that area
             // Look for the first solid block from Y=70 down to Y=55
             int startY = 70;
             int endY = 55;
             
             for (int y = startY; y >= endY; y--) {
                 // Try to get block data at this Y level
                 String command = "data get block " + x + " " + y + " " + z;
                 String response = rconClient.sendCommand(command);
                 
                 // If we get a valid response (not an error), this might be a solid block
                 if (!response.contains("error") && !response.contains("Error") && !response.contains("Unknown")) {
                     // Check if it's not air
                     if (!response.contains("air") && !response.contains("cave_air")) {
                         System.out.println("📍 Ground level found at Y=" + y + " (block data: " + response + ")");
                         return y;
                     }
                 }
             }
             
             // If we reach here, default to Y=60 (sea level)
             System.out.println("⚠️ Could not find ground level, defaulting to Y=60 (sea level)");
             return 60;
             
         } catch (Exception e) {
             logger.error("Error finding ground level: {}", e.getMessage());
             // Default to sea level if detection fails
             return 60;
         }
     }
          */
          /*
          private static void createSphere(
              int centerX, int centerY, int centerZ,
              int radius, double shell, RconClient rconClient) {
      
          double stepwidth = 1.0 / (2.0 * radius);
          int batchSize = 50; // Anzahl der setblock-Kommandos pro Paket
          int count = 0;
          StringBuilder batch = new StringBuilder();
          Set<String> placed = new HashSet<>(); // Zum Vermeiden doppelter Blöcke
      
          for (double y = 1.0; y >= -1.0; y -= stepwidth) {
              for (double x = 1.0; x >= -1.0; x -= stepwidth) {
                  for (double z = 1.0; z >= -1.0; z -= stepwidth) {
                      double r2 = x * x + y * y + z * z;
                      if (r2 < 1.0 && r2 > 1.0 - shell) {
                          int worldX = (int) Math.round(x * radius + centerX);
                          int worldY = (int) Math.round(y * radius + centerY);
                          int worldZ = (int) Math.round(z * radius + centerZ);
                          String posKey = worldX + "," + worldY + "," + worldZ;
      
                          // Nicht doppelt setzen
                          if (placed.contains(posKey)) continue;
                          placed.add(posKey);
      
                          String command = "setblock " + worldX + " " + worldY + " " + worldZ + " minecraft:lapis_ore";
      
                          if (batch.length() > 0) {
                              batch.append(" && ");
                          }
                          batch.append(command);
                          count++;
      
                          // Wenn Batch voll: abschicken
                          if (count >= batchSize) {
                              try {
                                  rconClient.sendCommand(batch.toString());
                              } catch (Exception e) {
                                  System.err.println("Fehler beim Senden eines Befehlsbündels: " + e.getMessage());
                              }
                              batch.setLength(0);
                              count = 0;
                          }
                      }
                  }
              }
          }
       
          // Restlicher Batch (falls übrig)
          if (batch.length() > 0) {
              try {
                  rconClient.sendCommand(batch.toString());
              } catch (Exception e) {
                  System.err.println("Fehler beim Senden des letzten Befehlsbündels: " + e.getMessage());
              }
          }
      }
      */
