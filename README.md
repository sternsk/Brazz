# Java RCON Project

A Gradle-based Java project with RCON (Remote Console) functionality and logging support.

## Prerequisites

- Java 11 or higher
- Gradle (or use the included Gradle wrapper)

## Project Structure

```
java-rcon-project/
├── build.gradle              # Gradle build configuration
├── settings.gradle           # Gradle settings
├── gradlew                   # Gradle wrapper script (Unix/Linux/macOS)
├── gradlew.bat              # Gradle wrapper script (Windows)
├── gradle/wrapper/          # Gradle wrapper files
├── src/
│   ├── main/java/
│   │   └── com/example/
│   │       ├── Main.java     # Main application class
│   │       └── RconClient.java # RCON client implementation
│   └── test/java/
│       └── com/example/
│           └── MainTest.java  # Test class
└── README.md                # This file
```

## Building the Project

### Using Gradle Wrapper (Recommended)

**On Windows:**
```bash
.\gradlew.bat build
```

**On Unix/Linux/macOS:**
```bash
./gradlew build
```

### Using Gradle directly
```bash
gradle build
```

## Running the Application

### Using Gradle Wrapper

**On Windows:**
```bash
.\gradlew.bat run
```

**On Unix/Linux/macOS:**
```bash
./gradlew run
```

### Using Gradle directly
```bash
gradle run
```

## Running Tests

### Using Gradle Wrapper

**On Windows:**
```bash
.\gradlew.bat test
```

**On Unix/Linux/macOS:**
```bash
./gradlew test
```

### Using Gradle directly
```bash
gradle test
```

## Dependencies

- **SLF4J API**: Logging facade
- **Logback Classic**: Logging implementation
- **JUnit Jupiter**: Testing framework

## Configuration

The project is configured to use:
- Java 11 as the target version
- Maven Central as the repository
- JUnit 5 for testing

## Usage

### Setting up RCON Connection

1. **Configure the server settings** in `src/main/resources/application.properties`:
   ```properties
   server.host=voidrunner.zapto.org
   server.port=22570
   server.rcon.password=your_actual_rcon_password
   ```

2. **Replace the placeholder password** with the actual RCON password for your server.

3. **Run the application**:
   ```bash
   gradle run
   ```

### Available Commands

The application will automatically try these commands:
- `players` - Query for active players
- `status` - Get server status
- `info` - Get server information

### Customizing Commands

You can modify the `Main.java` file to send different commands to the server.

## Next Steps

1. Add more comprehensive error handling and retry logic
2. Implement command queuing and batch processing
3. Add support for different game server types
4. Create a web interface for server management
5. Add configuration for multiple servers
6. Implement event-driven responses

## License

This project is open source and available under the Apache License 2.0. 