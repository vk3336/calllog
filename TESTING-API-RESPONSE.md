# Testing the API Response Display

## Quick Test Guide

### Step 1: Install the App

```bash
.\install-apk.bat
```

### Step 2: Configure ESPO

1. Open the app
2. Enter your phone number
3. Enter ESPO URL (e.g., `https://your-espo.com/api/v1/Call`)
4. Enter API Key
5. Click "Save Configuration"

### Step 3: Enable Sync

1. Toggle "Enable ESPO Sync" switch to ON
2. Click "Start Service"

### Step 4: Test Scenarios

#### Test A: Successful Sync (if ESPO is configured correctly)

1. Make a test call or wait for existing calls to sync
2. Watch the "📡 ESPO API Response" card
3. You should see:
   - "📤 Syncing..." (blue status)
   - Then "✅ SUCCESS!" (green status)
   - ESPO record ID and details

#### Test B: Configuration Error (intentional test)

1. Change ESPO URL to something wrong (e.g., add `/wrong` at the end)
2. Click "Save Configuration"
3. Wait for sync attempt
4. You should see:
   - "❌ SYNC FAILED" (red status)
   - HTTP error code (404 or similar)
   - Error details from ESPO

#### Test C: Invalid API Key (intentional test)

1. Change API Key to something wrong
2. Click "Save Configuration"
3. Wait for sync attempt
4. You should see:
   - "❌ SYNC FAILED" (red status)
   - 401 Unauthorized error
   - Authentication error message

#### Test D: No Configuration

1. Clear ESPO URL and API Key
2. Click "Save Configuration"
3. You should see:
   - "❌ ESPO not configured" message
   - Prompt to configure first

### Step 5: Verify Response Details

The response box should show:

**For Success:**

- ✅ emoji
- HTTP status code (200)
- ESPO record ID
- Call details (name, direction, status, duration)
- Phone number and contact name
- Timestamp

**For Failure:**

- ❌ emoji
- HTTP error code (400, 401, 404, 500, etc.)
- Error message from ESPO
- Request data that was sent
- Phone number being synced

**For Exception:**

- ❌ emoji
- Exception type (SocketTimeoutException, etc.)
- Error message
- Stack trace (first 500 characters)

### Step 6: Use Clear Button

1. Click "Clear" button
2. Response box should reset to: "Response cleared. Waiting for next sync..."
3. Status should show: "Status: Idle" (gray)

## What to Look For

### ✅ Good Signs

- Green status with "SUCCESS"
- ESPO ID is shown (e.g., `64f8a9b2c3d4e5f6`)
- "Synced" count increases in Statistics
- "Unsynced" count decreases

### ❌ Problem Signs

- Red status with "FAILED" or "EXCEPTION"
- Error codes (400, 401, 404, 500)
- Error messages about invalid fields
- Timeout exceptions

## Common Issues and Solutions

### Issue 1: "❌ ESPO not configured"

**Solution**: Enter ESPO URL and API Key, then save

### Issue 2: "❌ SYNC FAILED - 404 Not Found"

**Solution**: Check ESPO URL is correct (should end with `/Call`)

### Issue 3: "❌ SYNC FAILED - 401 Unauthorized"

**Solution**: Verify API Key is correct in ESPO settings

### Issue 4: "❌ SYNC FAILED - 400 Bad Request"

**Solution**: Check ESPO field requirements (phoneNumbersMap format, etc.)

### Issue 5: "❌ EXCEPTION - SocketTimeoutException"

**Solution**: Check internet connection and ESPO server availability

### Issue 6: No response showing

**Solution**:

- Make sure service is started
- Make sure sync is enabled
- Make a test call to trigger sync
- Check if there are unsynced calls in Statistics

## Debugging Tips

1. **Check Logcat**: Use Android Studio or `adb logcat` to see detailed logs

   ```bash
   adb logcat | findstr "CallLogService"
   ```

2. **Response Box**: The response box shows the same information as logs but in a user-friendly format

3. **Statistics**: Watch the "Synced" and "Unsynced" counts to see if sync is working

4. **Timestamp**: Each response shows when it occurred (HH:mm:ss format)

5. **Selectable Text**: You can long-press and copy text from the response box for sharing

## Expected Behavior

### Normal Flow

1. App starts → "Status: Idle"
2. Call detected → "📤 Syncing..." (blue)
3. API call sent → Shows request details
4. Response received → "✅ SUCCESS!" (green) or "❌ FAILED" (red)
5. Next sync → Process repeats every 30 seconds

### Sync Interval

- Service checks for unsynced calls every 30 seconds
- Each sync attempt is logged in the response box
- Only the latest response is shown (use Clear to reset)

## Success Criteria

✅ You've successfully tested when you see:

1. Response box updates in real-time
2. Status colors change appropriately
3. Detailed information is displayed
4. Clear button works
5. Both success and failure cases are handled
6. Error messages are helpful and actionable

## Need Help?

If the response box shows errors:

1. Read the error message carefully
2. Check the "Common Issues" section above
3. Verify your ESPO configuration
4. Test with a simple API call using Postman or curl
5. Check ESPO server logs for more details

The response box is designed to give you all the information you need to diagnose and fix any sync issues!
