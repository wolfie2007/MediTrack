# MediTrack - Personal Health Management System

A comprehensive JavaFX-based desktop application for managing personal health information, medicines, prescriptions, and health records.

## ðŸŽ¯ Features

- **ðŸ‘¤ Patient Profiles**: Manage family member health records
- **ðŸ’Š Medicine Management**: Track medicines and their details
- **ðŸ“‹ Prescription Tracking**: Organize and monitor prescriptions
- **ðŸ“… Dose Logging**: Record daily medicine doses with timestamps
- **ðŸ“Š Health Reports**: View analytics and health insights
- **ðŸ“ Appointment Management**: Schedule and track medical appointments
- **ðŸ’¾ Data Persistence**: Automatic local data storage with serialization

## ðŸ›  Technologies

- **Language**: Java 17
- **UI Framework**: JavaFX 21.0.3
- **Build Tool**: Maven 3.9.6
- **Data Storage**: File-based (serialized Java objects)
- **Target Platform**: Windows 11 (cross-platform compatible)

## ðŸ“¦ Quick Start

### Option 1: Pre-built Portable Package (Easiest)

**No installation required! Just extract and run:**

1. Download `MediTrack-Windows-Portable.zip` (50 MB)
2. Extract the ZIP file
3. Double-click `run.bat`
4. MediTrack launches immediately âœ“

**Requirements**: None! Java is bundled.

### Option 2: Build from Source

**Requirements**:
- Java 17 or later
- Maven 3.6+

```bash
# Clone the repository
git clone https://github.com/yourusername/MediTrack.git
cd MediTrack

# Build the project
mvn clean package

# Run the application
java -jar target/MediTrack.jar
```

## ðŸ“š Documentation

- **[QUICK-START.md](QUICK-START.md)** - Fast setup guide
- **[SETUP-PORTABLE-GUIDE.md](SETUP-PORTABLE-GUIDE.md)** - Detailed portable installation
- **[DISTRIBUTION-GUIDE.md](DISTRIBUTION-GUIDE.md)** - Distribution and deployment

## ðŸ“ Project Structure

```
MediTrack/
â”œâ”€â”€ src/main/java/com/meditrack/
â”‚   â”œâ”€â”€ App.java                    # Main application entry point
â”‚   â”œâ”€â”€ Main.java                   # Launcher
â”‚   â”œâ”€â”€ model/                      # Data models (Patient, Medicine, etc.)
â”‚   â”œâ”€â”€ storage/                    # Data persistence layer
â”‚   â”œâ”€â”€ ui/                         # UI controllers (JavaFX)
â”‚   â””â”€â”€ util/                       # Utility classes
â”œâ”€â”€ src/main/resources/
â”‚   â”œâ”€â”€ fxml/                       # JavaFX layouts
â”‚   â”œâ”€â”€ css/                        # Stylesheets
â”‚   â””â”€â”€ fonts/                      # Custom fonts
â”œâ”€â”€ pom.xml                         # Maven build configuration
â”œâ”€â”€ run.bat                         # Windows launcher
â”œâ”€â”€ build-standalone.bat            # Create portable package with Java
â””â”€â”€ README.md                       # This file
```

## ðŸš€ Distribution

### Create Portable Package

The project includes pre-built portable installers:

1. **MediTrack-Portable-Package.zip** (8.7 MB)
   - Requires Java 17+ on target system
   - Lightweight, fast distribution

2. **MediTrack-Windows-Portable.zip** (50 MB)
   - Includes bundled Java runtime
   - Works on ANY Windows 11 PC without any setup
   - Recommended for end users

### Building Custom Packages

```bash
# Create package with bundled Java (recommended)
.\build-standalone.bat

# Create lightweight package (requires Java on target)
.\build-portable.bat

# Both commands generate ZIP files in the root directory
```

## ðŸ“Š System Requirements

| Requirement | Minimum | Recommended |
|---|---|---|
| OS | Windows 7+ | Windows 11 |
| RAM | 1 GB | 2 GB |
| Disk Space | 300 MB | 500 MB |
| Java (if not bundled) | 17 LTS | Latest LTS |

## ðŸ“ Usage

### First Launch
On first run, the application creates:
- `~\.meditrack\` folder (Windows) - stores all user data
- Patients database
- Medicines inventory
- Prescription records
- Dose logs

### Data Backup
To backup your health data:
```
Copy: C:\Users\YourUsername\.meditrack\
To: External drive or cloud storage
```

To restore:
```
Copy backup files back to: C:\Users\YourUsername\.meditrack\
Restart the application
```

## ðŸ”§ Development

### Build from Source
```bash
mvn clean package -DskipTests
```

### Run with Maven
```bash
mvn javafx:run
```

### Create Executable JAR
```bash
# JAR is automatically created in target/MediTrack.jar
java -jar target/MediTrack.jar
```

## ðŸ› Troubleshooting

### Application won't start
- Ensure Java 17+ is installed: `java -version`
- Check `.meditrack` folder has write permissions
- Try running with Administrator privileges

### Data loss
- Check backup in `~\.meditrack\` folder
- Restore from backup if available

### UI issues on different displays
- Adjust window size via application menus
- Check system display scaling settings

## ðŸ“„ License

[Add your license here - MIT, Apache 2.0, etc.]

## ðŸ‘¨â€ðŸ’» Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## ðŸ“ž Support

For issues, questions, or suggestions:
- Open an [Issue](https://github.com/yourusername/MediTrack/issues)
- Check [Documentation](./SETUP-PORTABLE-GUIDE.md)

## ðŸŽ‰ Changelog

### v1.0.0 (Initial Release)
- Patient profile management
- Medicine and prescription tracking
- Dose logging system
- Health reports
- Appointment management
- Portable Windows installer with bundled Java

---

**Version**: 1.0.0  
**Last Updated**: May 2026  
**Platform**: Windows (Java 17+)  
**Status**: Production Ready âœ“
