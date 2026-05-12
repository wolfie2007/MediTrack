# MediTrack Portable Installer - Distribution Guide

## Overview

This guide explains how to build and distribute MediTrack as a portable application that users can run on any computer with Java installed.

---

## Build Portable Installer

### On Windows

```batch
build-portable.bat
```

### On macOS/Linux

```bash
chmod +x build-portable.sh
./build-portable.sh
```

### What Gets Built

The build process creates:

```
target/
â”œâ”€â”€ MediTrack.jar              (Single executable JAR with all dependencies)
â””â”€â”€ MediTrack-Portable.zip     (Ready-to-distribute package)
```

---

## Distribution Package Contents

When you extract `MediTrack-Portable.zip`, users get:

```
MediTrack-Portable/
â”œâ”€â”€ MediTrack.jar              (The application)
â”œâ”€â”€ run.bat                    (Windows launcher)
â”œâ”€â”€ run.sh                     (macOS/Linux launcher)
â””â”€â”€ README-PORTABLE.md         (User instructions)
```

---

## User Installation Steps

### For End Users (No Development Knowledge Required)

1. **Download** `MediTrack-Portable.zip` from your distribution source
2. **Extract** the ZIP file to any location
3. **Run the app:**
   - **Windows:** Double-click `run.bat`
   - **macOS/Linux:** Open Terminal, run `./run.sh`

That's it! No Maven, no IDE, no compilation needed.

---

## System Requirements for End Users

| Requirement | Minimum | Recommended |
|---|---|---|
| Java | 17 or later | Latest LTS |
| RAM | 1 GB | 2 GB |
| Disk Space | 400 MB | 500 MB |
| OS | Windows 10+ / macOS 10.12+ / Ubuntu 18.04+ | - |

---

## How to Distribute

### Option 1: Direct File Share
- Upload `MediTrack-Portable.zip` to Google Drive, Dropbox, etc.
- Share link with users

### Option 2: Website
- Host on a project website for download

### Option 3: Email
- Send the ZIP file directly (usually < 100 MB)

### Option 4: GitHub Releases
- Push to GitHub and create a Release with the ZIP attached

---

## Advanced: Create Windows Installer (.exe)

For a more polished distribution, you can create a Windows installer using `jpackage`:

```bash
mvn clean package -Dpackaging=exe
```

**Note:** Requires JDK 17+ with `jpackage` module (not in all distributions).

---

## File Size

Typical `MediTrack-Portable.zip` size: **50-100 MB**

This includes:
- JavaFX libraries
- All dependencies
- Application JAR

---

## Customization Options

### Update Version
Edit `pom.xml`:
```xml
<version>1.0.1</version>  <!-- Change version number -->
```

### Add Custom Icon (Windows)
Create an `.ico` file and reference in build scripts.

### Add License/Legal Files
Add to portable package in `pom.xml` build section.

---

## Troubleshooting Distribution Issues

### Users Get "Java Not Found"
- Include Java in the ZIP (advanced) or
- Direct users to download Java from oracle.com or adoptium.net

### File Size Too Large
- Remove unused dependencies from `pom.xml`
- Use ProGuard to shrink the JAR (advanced)

### Users Get Permission Errors
- Ensure `run.sh` is executable (included in build)
- On macOS, may need to right-click â†’ Open if code-signed

---

## Version Updates

To release an update:

1. Update version in `pom.xml`
2. Rebuild: `mvn clean package`
3. Distribute new `MediTrack-Portable.zip`

Users can run multiple versions simultaneously by extracting to different folders.

---

## Summary

| Task | Command |
|---|---|
| Build portable package | `build-portable.bat` (Windows) or `build-portable.sh` (Unix) |
| Rebuild from scratch | `mvn clean package` |
| View build output | Check `target/MediTrack-Portable.zip` |
| Distribute to users | Share the ZIP file |

---

**Current Version:** 1.0.0  
**Last Updated:** May 2026
