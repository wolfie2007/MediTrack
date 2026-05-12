@echo off
REM Build script to create MediTrack fully self-contained installer
REM Creates a ZIP that includes bundled Java 17 - no dependencies needed!

setlocal enabledelayedexpansion

echo.
echo ================================================
echo MediTrack Self-Contained Installer Builder
echo ================================================
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Step 1: Clean and build
echo [1/3] Cleaning previous builds...
call mvn clean

echo.
echo [2/3] Building MediTrack JAR...
call mvn package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo [3/3] Downloading bundled Java 17...
call download-java.bat

if %errorlevel% neq 0 (
    echo.
    echo WARNING: Java download failed, but JAR is built.
    echo Run download-java.bat separately after fixing the issue.
)

echo.
echo ================================================
echo Creating portable package...
echo ================================================
echo.

REM Create portable folder structure
if exist "meditrack-standalone" rmdir /s /q meditrack-standalone
mkdir meditrack-standalone

REM Copy application files
copy target\MediTrack.jar meditrack-standalone\
copy run.bat meditrack-standalone\
copy README-PORTABLE.md meditrack-standalone\

REM Copy Java if it exists
if exist java (
    echo Copying bundled Java to package...
    xcopy java meditrack-standalone\java\ /E /I /Y >nul
) else (
    echo.
    echo WARNING: Java folder not found. Run download-java.bat to include it.
)

REM Create ZIP
echo Creating ZIP archive...
powershell -Command "& {Add-Type -AssemblyName System.IO.Compression.FileSystem; [System.IO.Compression.ZipFile]::CreateFromDirectory('meditrack-standalone', 'MediTrack-Windows-Portable.zip')}"

REM Cleanup
rmdir /s /q meditrack-standalone

echo.
echo ================================================
echo SUCCESS! Package ready for distribution
echo ================================================
echo.
echo Package: MediTrack-Windows-Portable.zip
echo.
echo Contents:
echo   - MediTrack.jar (application)
echo   - Java 17 (bundled, ~160 MB)
echo   - run.bat (launcher)
echo   - README-PORTABLE.md (instructions)
echo.
echo Total size: ~200-250 MB
echo.
echo DISTRIBUTION:
echo   1. Copy MediTrack-Windows-Portable.zip to another PC
echo   2. Extract the ZIP folder
echo   3. Double-click run.bat
echo   4. App launches immediately - NO Java install needed!
echo.
pause
