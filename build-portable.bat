@echo off
REM Build script to create MediTrack portable installer
REM This script compiles the project and creates a portable package

echo.
echo ========================================
echo MediTrack Portable Installer Builder
echo ========================================
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Clean and build
echo Cleaning previous builds...
call mvn clean

echo.
echo Building MediTrack...
call mvn package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo Build completed successfully!
echo ========================================
echo.
echo Portable package created:
echo Location: target\MediTrack-Portable.zip
echo.
echo Next steps:
echo 1. Extract MediTrack-Portable.zip
echo 2. Run run.bat (Windows) or ./run.sh (macOS/Linux)
echo 3. Distribute the extracted folder to users
echo.
pause
