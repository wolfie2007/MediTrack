@echo off
REM MediTrack Application Launcher (Self-Contained Version)
REM Uses bundled Java - no system Java installation required

setlocal enabledelayedexpansion

REM Get the directory where this batch file is located
set SCRIPT_DIR=%~dp0

REM Check for bundled Java first
if exist "%SCRIPT_DIR%java\bin\java.exe" (
    set JAVA_CMD=%SCRIPT_DIR%java\bin\java.exe
    echo Using bundled Java 17
) else (
    REM Fall back to system Java
    set JAVA_CMD=java.exe
    echo Warning: Using system Java. For best experience, run: download-java.bat
)

REM Verify Java exists
%JAVA_CMD% -version >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ============================================
    echo ERROR: Java 17 not found!
    echo ============================================
    echo.
    echo Option 1 (RECOMMENDED):
    echo   Run: download-java.bat
    echo   This will download and bundle Java automatically
    echo.
    echo Option 2:
    echo   Install Java from: https://www.oracle.com/java/technologies/downloads/
    echo   or https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Get Java version
for /f tokens^=2^ delims^=^" %%A in ('%JAVA_CMD% -version 2^>^&1 ^| find "version"') do (
    set JAVA_VERSION=%%A
)

echo Java version: !JAVA_VERSION!

REM Run the application
echo.
echo Starting MediTrack...
echo.

"%JAVA_CMD%" -jar "%SCRIPT_DIR%MediTrack.jar" %*

if %errorlevel% neq 0 (
    echo.
    echo Application exited with error code: %errorlevel%
    pause
)

endlocal
