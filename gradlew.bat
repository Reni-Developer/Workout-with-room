@echo off
REM *******************************************************************************************
REM Gradle start up script for Windows.
REM *******************************************************************************************

REM Set the GRADLE_HOME environment variable to the directory where Gradle is located
set GRADLE_HOME=%~dp0\gradle

REM Add the Gradle bin directory to the system PATH
set PATH=%GRADLE_HOME%\bin;%PATH%

REM Check if the Gradle binary exists
if not exist "%GRADLE_HOME%\bin\gradle" (
    echo Gradle home directory does not contain a Gradle binary.
    echo Please run 'gradle wrapper' to generate the wrapper files.
    exit /b 1
)

REM Execute the Gradle task with any arguments passed to this script
"%GRADLE_HOME%\bin\gradle" %*
