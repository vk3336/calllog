@echo off
echo ========================================
echo Call Logger - Detailed Logs
echo ========================================
echo.
echo Filtering logs for CallLogService...
echo Press Ctrl+C to stop
echo.
adb logcat -s CallLogService:D MainActivity:D *:E
