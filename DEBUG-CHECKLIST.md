# Debug Checklist - Why API Service Not Initialized

## The Error

```
❌ API Service Error
ESPO API service not initialized
```

This means `espoApiService` is `null` when trying to sync.

## Possible Causes

### 1. Service Not Restarted After Config Save

**Check:** Did you see the toast "Service restarted with new configuration"?

- ✅ Yes → Service restarted, go to #2
- ❌ No → Service didn't restart

**Fix:** Manually stop and start service:

1. Click "STOP SERVICE"
2. Wait 2 seconds
3. Click "START SERVICE"

### 2. URL or API Key Missing

**Check:** Are both fields filled in?

- ESPO URL: `https://espo.egport.com/api/v1`
- API Key: (your key)

**Fix:** Make sure both are entered before saving

### 3. URL Format Wrong

**Check:** Does your URL look like this?

- ✅ `https://espo.egport.com/api/v1` (correct)
- ❌ `https://espo.egport.com/api/v1/` (trailing slash)
- ❌ `https://espo.egport.com/api/v1/Call` (has /Call)
- ❌ `http://espo.egport.com/api/v1` (http not https)

**Fix:** Enter exactly: `https://espo.egport.com/api/v1`

### 4. Service Started Before Config Saved

**Check:** Did you start the service before entering config?

- If yes, the service has old (empty) config

**Fix:**

1. Enter config first
2. Save config
3. Then start service (or it auto-restarts)

### 5. Config Not Persisted

**Check:** Close and reopen the app. Are your settings still there?

- ✅ Yes → Config is saved
- ❌ No → Config not persisting

**Fix:** Check SharedPreferences permissions

### 6. Exception During Initialization

**Check:** Look at API Response box after saving config

- Does it show "❌ API Init Failed" with exception details?

**Fix:** Read the exception message for clues

## Step-by-Step Debug

### Step 1: Check Config is Saved

```
1. Enter URL and API Key
2. Click "Save Configuration"
3. Close app completely
4. Reopen app
5. Check if URL and API Key are still there
```

If they're gone → Config not saving properly

### Step 2: Check Service Initialization

```
1. Stop service
2. Enter config
3. Save config
4. Look at API Response box
```

Should show: "✅ API Initialized"

If shows: "❌ API Init Failed" → Read error message

If shows: Nothing → Service not broadcasting

### Step 3: Check Service is Running

```
1. Look at "Service Control" section
2. Check status text
```

Should show: "Service: Running"

If shows: "Service: Stopped" → Click "START SERVICE"

### Step 4: Check Sync is Enabled

```
1. Look at "Enable ESPO Sync" toggle
2. Make sure it's ON (blue)
```

If OFF → Turn it ON

### Step 5: Check for Unsynced Calls

```
1. Look at Statistics section
2. Check "Unsynced" count
```

If 0 → No calls to sync, make a test call first

If > 0 → Should be able to sync

### Step 6: Manual Sync Test

```
1. Click "🔄 TEST SYNC NOW"
2. Watch API Response box
```

Should show: "📤 Syncing..." then "✅ SUCCESS!" or "❌ FAILED"

If shows: "❌ API Service Error" → API not initialized

## Logcat Debug

Run this command and look for errors:

```bash
adb logcat -c && adb logcat | findstr "CallLogService"
```

### What to Look For:

#### Good Initialization:

```
Initializing ESPO API...
Base URL from config: 'https://espo.egport.com/api/v1'
API Key present: true
Final URL for API: 'https://espo.egport.com/api/v1/Call'
✅ ESPO API initialized successfully
```

#### Bad Initialization:

```
Initializing ESPO API...
Base URL from config: ''
API Key present: false
ESPO API not configured - sync will be disabled
```

Or:

```
Initializing ESPO API...
Base URL from config: 'https://espo.egport.com/api/v1'
API Key present: true
❌ Failed to initialize ESPO API
java.lang.IllegalArgumentException: baseUrl must end in /
```

## Common Issues and Solutions

### Issue 1: "baseUrl must end in /"

**Cause:** Retrofit requires base URL to end with /
**Solution:** App should add it automatically, but check the code

### Issue 2: Config fields are empty

**Cause:** Config not saved or not loaded
**Solution:** Check ConfigManager is working

### Issue 3: Service doesn't restart

**Cause:** Auto-restart failed
**Solution:** Manually stop and start service

### Issue 4: API key has spaces

**Cause:** Copy-paste added spaces
**Solution:** Trim the API key: `apiKey.trim()`

### Issue 5: URL has typo

**Cause:** Manual entry error
**Solution:** Copy-paste from working Postman

## Test with Minimal Config

Try this minimal test:

```kotlin
// In your app, just test if this works:
val testUrl = "https://espo.egport.com/api/v1/Call"
val testKey = "your-api-key"

try {
    val service = RetrofitClient.getInstance()
        .createEspoApiService(testUrl, testKey)
    Log.d("TEST", "✅ Service created successfully")
} catch (e: Exception) {
    Log.e("TEST", "❌ Failed to create service", e)
}
```

If this fails, the problem is in RetrofitClient.

If this works, the problem is in how config is passed to the service.

## Check RetrofitClient

The base URL must end with `/`:

```kotlin
// This works:
baseUrl = "https://espo.egport.com/api/v1/Call/"

// This fails:
baseUrl = "https://espo.egport.com/api/v1/Call"
```

Check if the app is adding the trailing slash after adding `/Call`.

## Final Debug Steps

1. **Clear app data completely**

   ```
   Settings → Apps → Call Logger → Storage → Clear Data
   ```

2. **Reinstall app**

   ```bash
   .\install-apk.bat
   ```

3. **Grant all permissions**

4. **Enter config in this exact order:**
   - Phone: `8866791095`
   - URL: `https://espo.egport.com/api/v1`
   - Key: (your key)

5. **Save config**

6. **Wait 2 seconds**

7. **Check API Response box**
   - Should show: "✅ API Initialized"

8. **If still shows error:**
   - Run logcat command
   - Copy the error message
   - Check what URL and key are being used

## Expected Logcat Output

When everything works:

```
D/CallLogService: CallLogService created
D/CallLogService: Initializing ESPO API...
D/CallLogService: Base URL from config: 'https://espo.egport.com/api/v1'
D/CallLogService: API Key present: true
D/CallLogService: Final URL for API: 'https://espo.egport.com/api/v1/Call'
D/CallLogService: ✅ ESPO API initialized successfully
D/CallLogService: CallLogService started
D/CallLogService: Starting to read call logs...
D/CallLogService: Found 2100 unsynced calls
D/CallLogService: 📤 Sending to ESPO:
D/CallLogService: Successfully synced call: +918866791095
```

## If All Else Fails

Send me the logcat output from this command:

```bash
adb logcat -c
# Then in the app: Save config and click Test Sync
adb logcat -d | findstr "CallLogService"
```

This will show exactly what's happening during initialization and sync.

---

The key is to see the "✅ ESPO API initialized successfully" message. If you don't see this, the API service is not being created, and we need to find out why from the logs.
