# ⚠️ ACTION REQUIRED: Upload JAR to GitHub Release v1.0.0

## Problem
- ✅ Release v1.0.0 created on Adaptive-Throughput-Timer repo
- ❌ **JAR file NOT uploaded to the release**
- ❌ jmeter-plugins CI/CD workflow failed trying to download it

## Solution: Upload JAR to Release

### Quick Method (Web UI):

1. **Go to the release:** https://github.com/bakthava/Adaptive-Throughput-Timer/releases/tag/v1.0.0

2. **Click "Edit" button** (top right of the release)

3. **Scroll down to "Attach binaries by dropping them here or selecting them"**

4. **Select the JAR file from:**
   ```
   C:\Users\vinod\OneDrive\Adaptive_throughput_timer\target\adaptive-throughput-timer-1.0.0.jar
   ```

5. **Click "Update release"**

### Alternative: Upload via Command Line

If you prefer CLI, use this PowerShell command:

```powershell
# Get your GitHub token from https://github.com/settings/tokens (create one with 'repo' scope)
$token = "your_github_token_here"
$headers = @{
    "Authorization" = "Bearer $token"
    "X-GitHub-Api-Version" = "2022-11-28"
}

# First, get the release to get the upload URL
$release = Invoke-RestMethod -Uri "https://api.github.com/repos/bakthava/Adaptive-Throughput-Timer/releases/tags/v1.0.0" -Headers $headers

$uploadUrl = $release.upload_url -replace '\{.*\}', "?name=adaptive-throughput-timer-1.0.0.jar"

# Upload the JAR
$jar = Get-Item "C:\Users\vinod\OneDrive\Adaptive_throughput_timer\target\adaptive-throughput-timer-1.0.0.jar"

Invoke-RestMethod -Uri $uploadUrl `
  -Method POST `
  -Headers $headers `
  -ContentType "application/octet-stream" `
  -InFile $jar.FullName
```

## After Upload

Once the JAR is uploaded to the release:

1. **GitHub Actions will automatically re-run** the jmeter-plugins workflow
2. **JAR will be downloaded successfully** and packaged
3. **Plugin will be added to jmeter-plugins repository**
4. **Users can install via JMeter Plugin Manager** ✅

## Status Check

After uploading, check the workflow here:
https://github.com/undera/jmeter-plugins/actions

Look for "Run 698" (or next run) for "Build Automation. Add Adaptive Throughput Timer plugin..."

**Expected:** ✅ **PASSED** (green checkmark)

---

**⏱️ Estimated time to completion:** 5-10 minutes after JAR upload
