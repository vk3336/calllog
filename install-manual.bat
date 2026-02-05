@echo off
echo Manual APK Installation Guide
echo =============================
echo.

REM Check if APK exists
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ❌ APK not found! Please build the APK first using build-apk.bat
    pause
    exit /b 1
)

echo ✅ APK found: app\build\outputs\apk\debug\app-debug.apk
echo File size: 
dir app\build\outputs\apk\debug\app-debug.apk | findstr app-debug.apk
echo.

echo 📱 MANUAL INSTALLATION STEPS:
echo.
echo 1. COPY APK TO YOUR PHONE:
echo    - Connect your phone to computer via USB
echo    - Copy the APK file to your phone's Downloads folder
echo    - APK location: %CD%\app\build\outputs\apk\debug\app-debug.apk
echo.
echo 2. ENABLE UNKNOWN SOURCES:
echo    - Go to Settings ^> Security ^> Unknown Sources (enable)
echo    - Or Settings ^> Apps ^> Special Access ^> Install Unknown Apps
echo.
echo 3. INSTALL THE APK:
echo    - Open file manager on your phone
echo    - Navigate to Downloads folder
echo    - Tap on app-debug.apk
echo    - Follow installation prompts
echo.
echo 4. CONFIGURE THE APP:
echo    - Open "Call Logger" app
echo    - Enter your phone number
echo    - Enter ESPO CRM URL and API key
echo    - Grant all requested permissions
echo    - Start the service
echo.

echo Opening APK folder...
explorer app\build\outputs\apk\debug\

echo.
echo The APK folder has been opened. Copy app-debug.apk to your phone!
pause