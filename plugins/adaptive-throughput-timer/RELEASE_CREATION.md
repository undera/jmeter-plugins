# Creating GitHub Release v1.0.0 for Adaptive-Throughput-Timer

## Option 1: Manual Web UI (Easiest)

1. Go to: https://github.com/bakthava/Adaptive-Throughput-Timer/releases
2. Click **"Create a new release"**
3. Fill in the form:
   - **Tag version:** `v1.0.0` (should be auto-selected from the tag you just pushed)
   - **Release title:** `Adaptive Throughput Timer v1.0.0`
   - **Description:** Use the text below
   - **Attach binaries:** Drag and drop or select `target/adaptive-throughput-timer-1.0.0.jar`
4. Click **"Publish release"**

## Release Description Template

```markdown
## Adaptive Throughput Timer v1.0.0

First stable release of the Adaptive Throughput Timer JMeter plugin.

### Features

- **CSV-Based Load Profiles** — Define target TPS, step durations, and execution modes
- **Dynamic Thread Scaling** — Auto-adjust thread count based on throughput vs. target
- **24-Hour Infinite Execution** — Cycle load profiles every 24 hours with 24-hour coverage validation
- **Dynamic CSV Reload** — File checked every 60 seconds; modifications applied live without stopping test
- **Multiple Execution Modes** — Step-based (fixed duration), time-based (HH:mm ranges), and default (current time sync)
- **P90 Latency Monitoring** — Thread adjustment respects latency thresholds
- **Load Profile Visualization** — Real-time TPS graph in JMeter GUI
- **Thread Scaling Control** — Configurable ramp-up/ramp-down steps and min/max thread limits
- **Intuitive GUI** — Setup wizard with CSV file browser and profile preview

### Supported Formats

- **CSV/TXT:** `stepcount,mm:ss,tps`
- **Excel:** `xlsx/xls` with columns A=stepcount, B=time(mm:ss), C=tps

### Quick Start

1. Install the plugin via JMeter Plugin Manager
2. Create a CSV with load profile:
   ```
   1,00:10,100
   2,01:00,500
   3,02:00,1000
   ```
3. Add timer to test plan
4. Configure CSV file path, threads (min/max), adjustment interval
5. Enable dynamic reload if modifying CSV during test
6. Run test

### Compatibility

- **Java:** JDK 8+
- **JMeter:** 5.0+
- **Bytecode Version:** 52 (Java 8)

### Downloads

- JAR: `adaptive-throughput-timer-1.0.0.jar`

### Documentation

- Repository: https://github.com/bakthava/Adaptive-Throughput-Timer
- README: See repo for comprehensive usage guide
```

## Option 2: Command Line with GitHub CLI

If you have GitHub CLI installed:

```bash
cd C:\Users\vinod\OneDrive\Adaptive_throughput_timer

# Create release
gh release create v1.0.0 ` 
  -t "Adaptive Throughput Timer v1.0.0" `
  -F release-notes.md `
  target/adaptive-throughput-timer-1.0.0.jar
```

## Option 3: Using GitHub API (cURL)

```bash
# First, get your GitHub token from https://github.com/settings/tokens
# Create a personal access token with 'repo' scope

# Set your token
$GITHUB_TOKEN = "your_token_here"

# Create release
curl -L `
  -X POST `
  -H "Accept: application/vnd.github+json" `
  -H "Authorization: Bearer $GITHUB_TOKEN" `
  -H "X-GitHub-Api-Version: 2022-11-28" `
  https://api.github.com/repos/bakthava/Adaptive-Throughput-Timer/releases `
  -d '{
    "tag_name":"v1.0.0",
    "target_commitish":"master",
    "name":"Adaptive Throughput Timer v1.0.0",
    "body":"First stable release...",
    "draft":false,
    "prerelease":false
  }'

# Then upload the JAR to the release
# (Get upload_url from the response above and use it to upload)
```

## After Release is Created

Once the release is created with the JAR attached:

1. The jmeter-plugins CI/CD will automatically detect and download the JAR
2. Your plugin will be packaged and made available in the repository
3. Users can discover and install via JMeter Plugin Manager

---

**Recommended:** Use **Option 1** (Web UI) - it's the simplest and most straightforward!
