# Install and Test - Quick Start

## 1. Install the APK

```bash
.\install-apk.bat
```

Wait for "Success" message.

## 2. Open the App

You'll see the Call Logger main screen.

## 3. Configure (CRITICAL!)

Enter these values **exactly**:

### Phone Number

```
8866791095
```

### ESPO CRM Base URL (WITHOUT /Call!)

```
https://espo.egport.com/api/v1
```

**IMPORTANT**: Do NOT add `/Call` at the end. The app adds it automatically.

### ESPO API Key

```
(paste your actual API key here)
```

## 4. Save Configuration

Click the blue **"SAVE CONFIGURATION"** button.

## 5. Check API Response Box

Scroll down to the **"📡 ESPO API Response"** section.

You should see:

```
✅ API Initialized
ESPO URL: https://espo.egport.com/api/v1/Call
API Key: abc123def4...
```

If you see this, configuration is correct! ✅

If you see an error, check your URL and API key.

## 6. Enable Sync

In the **"🎛️ Service Control"** section:

- Toggle **"Enable ESPO Sync"** to ON (blue)
- You should see: "Sync enabled"

## 7. Start Service

Click the green **"START SERVICE"** button.

You should see: "Service: Running"

## 8. Test Sync Now!

Click the orange **"🔄 Test Sync Now"** button.

## 9. Watch the Response

The **"📡 ESPO API Response"** box will update in real-time:

### First (Syncing):

```
📤 Syncing...
URL: https://espo.egport.com/api/v1/Call
Phone: +918866791095
Type: Incoming
Duration: 125s
```

### Then (Success):

```
✅ SUCCESS!
Status: 200 OK
ESPO ID: 69858d0aacb0a8374
Call Name: Incoming call with John Doe
Direction: Inbound
Status: Held
Duration: 125s
Created: 2026-02-06 12:09:20

Phone: +918866791095
Contact: John Doe
```

### Status Color:

- **Green** = Success ✅
- **Red** = Failed ❌
- **Blue** = Syncing 📤

## 10. Check Statistics

Scroll down to **"📊 Statistics"**:

- **Total Calls**: Should show your call count
- **Synced**: Should increase after successful sync
- **Unsynced**: Should decrease after successful sync

## Troubleshooting

### "❌ API Service Error"

**Problem**: URL format wrong
**Fix**: Enter URL as `https://espo.egport.com/api/v1` (without `/Call`)

### "❌ SYNC FAILED - 401 Unauthorized"

**Problem**: Wrong API key
**Fix**: Check your API key in ESPO settings

### "❌ SYNC FAILED - 404 Not Found"

**Problem**: Wrong URL
**Fix**: Verify URL is `https://espo.egport.com/api/v1`

### "No unsynced calls to test"

**Problem**: All calls already synced
**Fix**: Make a test call, or wait for new calls

### "Please enable sync first"

**Problem**: Sync toggle is OFF
**Fix**: Turn on the "Enable ESPO Sync" toggle

### "Please configure ESPO first"

**Problem**: URL or API key missing
**Fix**: Enter URL and API key, then save

## Expected Timeline

1. **Install**: 10 seconds
2. **Configure**: 30 seconds
3. **Test**: 5 seconds
4. **See Result**: Immediate

Total time: **Less than 1 minute** to see if it works!

## Success Indicators

You know it's working when you see:

- ✅ "API Initialized" after saving config
- ✅ "Sync enabled" after toggling sync
- ✅ "Service: Running" after starting service
- ✅ "SUCCESS!" with ESPO ID after test sync
- ✅ Green status color
- ✅ Synced count increases

## What's Different Now

### Before:

- ❌ Had to add `/Call` manually to URL
- ❌ No way to test immediately
- ❌ No visibility into what's happening
- ❌ "API Service Error" with no details

### Now:

- ✅ App adds `/Call` automatically
- ✅ "Test Sync Now" button for immediate testing
- ✅ Real-time response display
- ✅ Detailed error messages
- ✅ Color-coded status
- ✅ See exactly what's sent and received

## Quick Test Command

After installing, just do this:

1. Enter URL: `https://espo.egport.com/api/v1`
2. Enter API Key: `(your key)`
3. Click "SAVE CONFIGURATION"
4. Toggle "Enable ESPO Sync" ON
5. Click "START SERVICE"
6. Click "🔄 Test Sync Now"
7. Watch for "✅ SUCCESS!"

That's it! 🎉

## Need Help?

Check these files:

- `FIXED-ESPO-SYNC.md` - Detailed explanation of fixes
- `CONFIGURATION-GUIDE.md` - Visual guide with examples
- `API-RESPONSE-DISPLAY.md` - How the response box works
- `TESTING-API-RESPONSE.md` - Complete testing guide

## Your Postman vs App

### Postman (works):

```
POST https://espo.egport.com/api/v1/Call
{"name":"vivek"}
```

### App (now works too):

```
POST https://espo.egport.com/api/v1/Call
{
  "name": "Incoming call with John Doe",
  "status": "Held",
  "duration": 125,
  "direction": "Inbound",
  ...
}
```

Both use the same endpoint, both should work! ✅

---

**Ready?** Run `.\install-apk.bat` and let's test it! 🚀
