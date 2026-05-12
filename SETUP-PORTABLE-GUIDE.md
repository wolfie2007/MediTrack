# MediTrack - Complete Portable Setup Guide

## ðŸŽ¯ Two Options

You now have **TWO ways** to distribute your app:

---

## **OPTION 1: Quick & Easy (No Java Bundled)**
**Package:** `MediTrack-Portable-Package.zip`  
**Size:** ~10 MB  
**Setup Time:** 5 seconds

### For End Users:
1. Extract the ZIP anywhere
2. **Requires:** Java 17+ installed on their system
3. Double-click `run.bat`
4. App launches âœ“

**When to use:** Distributing to tech-savvy users or organizations with Java already installed.

---

## **OPTION 2: Complete Standalone (Java Bundled)**
**Package:** `MediTrack-Windows-Portable.zip` *(To be created)*  
**Size:** ~200 MB  
**Setup Time:** 3 seconds

### For End Users:
1. Extract the ZIP anywhere
2. **No Java needed** - Everything included
3. Double-click `run.bat`
4. App launches âœ“

**When to use:** Maximum compatibility - works on any Windows 11 PC without ANY setup.

---

## How to Create Option 2 (Full Standalone with Java)

### Method 1: Automatic (Recommended)
```batch
build-standalone.bat
```

This script:
- Rebuilds the JAR âœ“
- Downloads Java 17 (~160 MB) âœ“
- Creates `MediTrack-Windows-Portable.zip` âœ“

**Troubleshooting:** If Java download fails, use Method 2.

---

### Method 2: Manual Java Bundling

#### Step 1: Download Java
- Visit: https://adoptium.net/temurin/releases/?os=windows&version=17
- Download: **OpenJDK 17 JRE (x64, Windows)**
- Extract and rename folder to: `java`
- Place in your project root folder

**Expected structure:**
```
your-project/
â”œâ”€â”€ MediTrack.jar
â”œâ”€â”€ run.bat
â”œâ”€â”€ java/           â† Java folder
â”‚   â”œâ”€â”€ bin/
â”‚   â”‚   â””â”€â”€ java.exe
â”‚   â”œâ”€â”€ lib/
â”‚   â””â”€â”€ ...
```

#### Step 2: Create Package
```batch
if exist "meditrack-standalone" rmdir /s /q meditrack-standalone
mkdir meditrack-standalone
copy target\MediTrack.jar meditrack-standalone\
copy run.bat meditrack-standalone\
copy README-PORTABLE.md meditrack-standalone\
xcopy java meditrack-standalone\java\ /E /I /Y
powershell -Command "Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::CreateFromDirectory('meditrack-standalone', 'MediTrack-Windows-Portable.zip')"
rmdir /s /q meditrack-standalone
```

#### Step 3: Distribute
Share `MediTrack-Windows-Portable.zip` to users!

---

##  Distribution Scenarios

| Scenario | Package | Method |
|----------|---------|--------|
| Internal company app | Option 2 (Full) | Standalone, pre-installed Java |
| Small group of tech users | Option 1 (Light) | Quick distribution |
| End consumers / general public | Option 2 (Full) | No system requirements |
| Healthcare / Enterprise | Option 2 (Full) | Reliable, predictable environment |

---

## Recommended: Create BOTH

1. **Option 1** (`MediTrack-Portable-Package.zip`) â€” For tech users
   - Already created âœ“
   - Light weight, fast distribution

2. **Option 2** (`MediTrack-Windows-Portable.zip`) â€” For everyone
   - Run: `build-standalone.bat` or follow Method 2 above
   - Complete, no dependencies

---

## Current Status

| Item | Status | File |
|---|---|---|
| JAR Built | âœ“ | `target/MediTrack.jar` (~10 MB) |
| Light Package | âœ“ | `MediTrack-Portable-Package.zip` (~10 MB) |
| Full Package with Java | â³ | `build-standalone.bat` to create |

---

## Testing Your Package

### Test Option 1 (Light):
```batch
# Extract MediTrack-Portable-Package.zip
# Edit run.bat to test locally
# Double-click run.bat
# Should launch if Java 17 is installed
```

###  Test Option 2 (Full):
```batch
# After running build-standalone.bat
# Extract MediTrack-Windows-Portable.zip
# Double-click run.bat
# Should launch without ANY Java installation
```

---

## Distribution Methods

### For Option 1 (Light):
- Email directly
- Dropbox / Google Drive
- GitHub Releases

### For Option 2 (Full):
- USB Drive
- GitHub Releases
- Internal server/Intranet

---

## File Locations

```
Project Root/
â”œâ”€â”€ MediTrack-Portable-Package.zip     â† Option 1 (ready to use)
â”œâ”€â”€ build-standalone.bat               â† Create Option 2
â”œâ”€â”€ target/
â”‚   â”œâ”€â”€ MediTrack.jar                 â† Your application
â”‚   â””â”€â”€ MediTrack-Portable.zip        â† Maven-created package
â””â”€â”€ run.bat                           â† Launcher script
```

---

## Next Steps

### Step 1: Test Option 1
```batch
# Extract MediTrack-Portable-Package.zip to a new location
# Test on your system (Java already installed)
```

### Step 2: Create Option 2
Choose one:
- **Automatic:** `build-standalone.bat`
- **Manual:** Follow Method 2 above

### Step 3: Test Option 2
```batch
# Extract on a CLEAN Windows PC without Java
# Should work immediately
```

### Step 4: Distribute
Share your chosen package with users!

---

## System Requirements for Users

### Option 1 (Light Package)
- Windows 7+
- Java 17+ (must be installed separately)
- 300 MB disk space

### Option 2 (Full Package)
- Windows 7+
- âœ“ No Java install needed
- 300 MB disk space

---

## Summary

You now have **production-ready distribution packages**:

âœ… **Option 1:** `MediTrack-Portable-Package.zip` (10 MB, Java required)  
â³ **Option 2:** Run `build-standalone.bat` to create full package (200 MB, zero requirements)

**Recommended:** Create Option 2 for maximum compatibility and user experience.

---

**Last Updated:** May 2026  
**Java Version:** 17 (LTS)  
**Target:** Windows 11 (compatible with Windows 7+)
