@echo off
echo Building Call Logger APK...
echo.

REM Check if APK already exists
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo APK already exists at: app\build\outputs\apk\debug\app-debug.apk
    echo File size: 
    dir app\build\outputs\apk\debug\app-debug.apk | findstr app-debug.apk
    echo.
    echo To rebuild, delete the existing APK first or run: gradlew clean assembleDebug
    pause
    exit /b 0
)

echo Starting Gradle build...
gradlew.bat assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ BUILD SUCCESSFUL!
    echo APK generated at: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo To install on device:
    echo adb install app\build\outputs\apk\debug\app-debug.apk
) else (
    echo.
    echo ❌ BUILD FAILED!
    echo Check the error messages above.
)

pause