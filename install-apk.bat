@echo off
echo Installing Call Logger APK to connected Android device...
echo.

REM Check if APK exists
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ❌ APK not found! Please build the APK first using build-apk.bat
    pause
    exit /b 1
)

REM Set ADB path
set ADB_PATH=C:\Users\%USERNAME%\AppData\Local\Android\Sdk\platform-tools\adb.exe

REM Check if ADB exists
if not exist "%ADB_PATH%" (
    echo ❌ ADB not found at expected location!
    echo Looking for ADB in Android SDK...
    echo Expected path: %ADB_PATH%
    echo.
    echo Please ensure Android SDK Platform Tools are installed.
    pause
    exit /b 1
)

REM Check if device is connected
echo Checking for connected devices...
"%ADB_PATH%" devices

echo.
echo Installing APK...
"%ADB_PATH%" install -r app\build\outputs\apk\debug\app-debug.apk

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ INSTALLATION SUCCESSFUL!
    echo The Call Logger app has been installed on your device.
    echo.
    echo Next steps:
    echo 1. Open the Call Logger app on your device
    echo 2. Grant the required permissions
    echo 3. Configure your phone number and ESPO CRM settings
    echo 4. Start the service to begin monitoring calls
) else (
    echo.
    echo ❌ INSTALLATION FAILED!
    echo Make sure:
    echo - Your device is connected via USB
    echo - USB debugging is enabled
    echo - You've allowed installation from unknown sources
)

pause