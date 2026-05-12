# Call Logger APK Installation Guide

## 📱 Your APK is Ready!

**Location**: `app/build/outputs/apk/debug/app-debug.apk`  
**Size**: ~6.7 MB  
**Status**: ✅ Successfully built and ready to install

## Installation Methods

### Method 1: USB Installation (Recommended)

#### Prerequisites:

1. **Enable Developer Options** on your Android device:
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times
   - Developer Options will appear in Settings

2. **Enable USB Debugging**:
   - Go to Settings > Developer Options
   - Enable "USB Debugging"

3. **Connect your device**:
   - Connect phone to computer via USB cable
   - Allow USB debugging when prompted on phone

#### Install using script:

```bash
.\install-apk.bat
```

#### Install manually with ADB:

```bash
C:\Users\URVI\AppData\Local\Android\Sdk\platform-tools\adb.exe install -r app\build\outputs\apk\debug\app-debug.apk
```

### Method 2: Manual Installation (No USB required)

#### Step 1: Transfer APK to Phone

- **Option A**: Email the APK to yourself and download on phone
- **Option B**: Upload to Google Drive/Dropbox and download on phone
- **Option C**: Copy via USB to phone's Downloads folder

#### Step 2: Enable Unknown Sources

- **Android 8+**: Settings > Apps > Special Access > Install Unknown Apps > [Your Browser/File Manager] > Allow
- **Android 7 and below**: Settings > Security > Unknown Sources (enable)

#### Step 3: Install APK

- Open file manager on phone
- Navigate to Downloads (or where you saved the APK)
- Tap on `app-debug.apk`
- Tap "Install" and follow prompts

#### Quick Manual Install:

```bash
.\install-manual.bat
```

This will open the APK folder for easy copying.

## App Configuration

### After Installation:

1. **Open Call Logger app**
2. **Grant Permissions** when prompted:
   - Call Log access
   - Phone State access
   - Notifications (Android 13+)

3. **Configure Settings**:
   - **Your Phone Number**: Enter your device's phone number
   - **ESPO CRM Base URL**: `https://your-espo-crm.com/api/v1/`
   - **ESPO API Key**: Your CRM API key

4. **Start Service**:
   - Tap "Save Configuration"
   - Tap "Start Service"
   - Enable "ESPO Sync" toggle

5. **Verify Operation**:
   - Make a test call
   - Check if it appears in the app's call log list
   - Verify sync status shows "✓ Synced"

## Troubleshooting

### Installation Issues:

- **"App not installed"**: Enable Unknown Sources for your file manager/browser
- **"Parse error"**: APK may be corrupted, re-download
- **"Insufficient storage"**: Free up space on device

### App Issues:

- **No permissions**: Go to Settings > Apps > Call Logger > Permissions
- **Service not starting**: Disable battery optimization for the app
- **Sync failing**: Check ESPO URL and API key in configuration

### Battery Optimization:

To ensure the app works in background:

- Settings > Battery > Battery Optimization
- Find "Call Logger" and set to "Don't optimize"

## ESPO CRM Setup

Ensure your ESPO CRM has:

- ✅ Call entity enabled
- ✅ API access configured
- ✅ Valid API key generated
- ✅ Proper permissions for Call entity

## Security Notes

- App stores API keys securely in encrypted preferences
- All network communication uses HTTPS
- No sensitive data is logged
- Designed for personal/internal use only

## Support

If you encounter issues:

1. Check Android logs: `adb logcat | grep CallLogger`
2. Verify all permissions are granted
3. Ensure ESPO CRM is accessible and API key is valid
4. Check battery optimization settings
