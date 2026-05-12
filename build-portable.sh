#!/bin/bash

# Build script to create MediTrack portable installer
# This script compiles the project and creates a portable package

echo ""
echo "========================================"
echo "MediTrack Portable Installer Builder"
echo "========================================"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    echo "Please install Maven from: https://maven.apache.org/download.cgi"
    exit 1
fi

# Clean and build
echo "Cleaning previous builds..."
mvn clean

echo ""
echo "Building MediTrack..."
mvn package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Build failed!"
    exit 1
fi

echo ""
echo "========================================"
echo "Build completed successfully!"
echo "========================================"
echo ""
echo "Portable package created:"
echo "Location: target/MediTrack-Portable.zip"
echo ""
echo "Next steps:"
echo "1. Extract MediTrack-Portable.zip"
echo "2. Run ./run.sh (Linux/macOS) or run.bat (Windows)"
echo "3. Distribute the extracted folder to users"
echo ""
