# MediTrack - Personal Health Management System

A comprehensive JavaFX-based desktop application for managing personal health information, medicines, prescriptions, and health records.

## Features

- **Patient Profiles** - Manage family member health records
- **Medicine Management** - Track medicines and their details  
- **Prescription Tracking** - Organize and monitor prescriptions
- **Dose Logging** - Record daily medicine doses with timestamps
- **Health Reports** - View analytics and health insights
- **Appointment Management** - Schedule and track medical appointments
- **Data Persistence** - Automatic local data storage with serialization

## Technologies

- **Language**: Java 17
- **UI Framework**: JavaFX 21.0.3
- **Build Tool**: Maven 3.9.6
- **Data Storage**: File-based (serialized Java objects)
- **Target Platform**: Windows 11 (cross-platform compatible)

## Quick Start

### Option 1: Pre-built Portable Package (Recommended)

**No installation required! Just extract and run:**

1. Download `MediTrack-Windows-Portable.zip` (50 MB)
2. Extract the ZIP file to any folder
3. Double-click `run.bat`
4. MediTrack launches immediately

**What's Included:**
- Complete JavaFX application
- Java 17 runtime (bundled)
- Everything you need - no additional software required

### Option 2: Build from Source

**Requirements:**
- Java 17 or later
- Maven 3.6+

```bash
# Clone the repository
git clone https://github.com/wolfie2007/MediTrack.git
cd MediTrack

# Build the project
mvn clean package

# Run the application
java -jar target/MediTrack.jar
```

## Documentation

- [QUICK-START.md](QUICK-START.md) - Fast setup guide
- [SETUP-PORTABLE-GUIDE.md](SETUP-PORTABLE-GUIDE.md) - Detailed installation guide
- [DISTRIBUTION-GUIDE.md](DISTRIBUTION-GUIDE.md) - Deployment information

## Project Structure
```
MediTrack/
├── src/main/java/com/meditrack/
│   ├── App.java                    # Application entry point
│   ├── Main.java                   # Launcher
│   ├── model/                      # Data models (Patient, Medicine, Prescription, etc.)
│   ├── storage/                    # Data persistence layer
│   ├── ui/                         # UI controllers (JavaFX)
│   └── util/                       # Utility classes
│
├── src/main/resources/
│   ├── fxml/                       # JavaFX UI layouts
│   ├── css/                        # Application stylesheets
│   └── fonts/                      # Custom fonts
│
├── pom.xml                         # Maven build configuration
├── run.bat                         # Windows launcher
├── build-standalone.bat            # Build portable package with Java
└── README.md                       # This file
```
## Distribution Options

### Pre-built Packages Available

1. **MediTrack-Portable-Package.zip** (8.7 MB)
   - Lightweight option
   - Requires Java 17+ on target system
   - Good for distribution in environments with Java already installed

2. **MediTrack-Windows-Portable.zip** (50 MB)
   - Complete package with bundled Java 17
   - Works on ANY Windows 11 PC
   - No Java installation needed
   - Recommended for end users

### Building Custom Packages

```bash
# Create package with bundled Java (recommended)
.\build-standalone.bat

# This will create MediTrack-Windows-Portable.zip
# Ready for distribution to end users
```

## System Requirements

| Requirement | Minimum | Recommended |
|---|---|---|
| OS | Windows 7+ | Windows 11 |
| RAM | 1 GB | 2 GB |
| Disk Space | 300 MB | 500 MB |
| Java (if not bundled) | 17 LTS | Latest LTS |

## Usage

### First Launch

On first run, MediTrack creates:
- User data folder: `C:\Users\YourUsername\.meditrack\`
- Database files for patients, medicines, and prescriptions
- Automatic data backup location

### Data Management

**Backup Your Data:**
```
Copy folder: C:\Users\YourUsername\.meditrack\
To: External drive or cloud storage
```

**Restore Data:**
```
1. Copy backed up .meditrack folder
2. Paste to: C:\Users\YourUsername\
3. Restart MediTrack
```

**Uninstall:**
- Simply delete the extracted MediTrack folder
- Your data in `.meditrack/` remains (delete if you want full removal)

## Development

### Build from Source

```bash
# Clean build
mvn clean package

# Run with Maven
mvn javafx:run

# Create JAR for distribution
# JAR is located at: target/MediTrack.jar
```

### Project Modules

- **model/** - Data classes for Patient, Medicine, Prescription, Appointment, DoseLog
- **storage/** - File persistence and data repository
- **ui/** - JavaFX controllers for all application screens
- **util/** - Helper classes for ID generation, events, and alerts

## Troubleshooting

### Application won't start
- Ensure Java 17+ is installed (if using light package)
- Check that `.meditrack` folder has write permissions
- Try running with Administrator privileges

### "Java not found" error
- For portable package: Java should be bundled
- For light package: Install Java from https://adoptium.net/

### Data not saving
- Verify `.meditrack` folder exists and is writable
- Check disk space availability
- Ensure no file permission issues

### UI display issues
- Adjust window size using application menus
- Check Windows display scaling settings
- Try running on different display resolution

## Version Info

- **Version**: 1.0.0
- **Release Date**: May 2026
- **Status**: Production Ready
- **Java Target**: Java 17+
- **Platform**: Windows (Java 17+ cross-platform compatible)

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Support

For issues, questions, or suggestions:
- Open an [Issue](https://github.com/wolfie2007/MediTrack/issues)
- Check the [Documentation](./SETUP-PORTABLE-GUIDE.md)
- Review the [Quick Start Guide](./QUICK-START.md)

## Project Highlights

- **100% Portable** - Extract and run on any Windows 11 PC
- **No Installation** - Bundled Java eliminates setup complexity
- **Zero Dependencies** - Everything included in one folder
- **User-Friendly** - Intuitive JavaFX interface
- **Data Privacy** - All data stored locally on your computer
- **Cross-Platform Ready** - Java codebase works on macOS and Linux too

---

**Start using MediTrack today!** Download the portable package and manage your health with ease.

Visit: https://github.com/wolfie2007/MediTrack
