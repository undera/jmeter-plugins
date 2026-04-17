# Pull Request: Add Adaptive Throughput Timer Plugin to jmeter-plugins

## PR Creation Instructions

Your branch `add-adaptive-throughput-timer` has been successfully created and pushed to your fork at:
https://github.com/bakthava/jmeter-plugins

### To Create the PR on GitHub:

1. **Visit the PR creation link:**
   https://github.com/bakthava/jmeter-plugins/pull/new/add-adaptive-throughput-timer

2. **Or manually create a PR:**
   - Go to: https://github.com/bakthava/jmeter-plugins
   - Click "Pull requests" tab
   - Click "New pull request"
   - Select base: `undera:master` and compare: `bakthava:add-adaptive-throughput-timer`
   - Click "Create pull request"

## PR Template Content

### Title
```
Add Adaptive Throughput Timer plugin
```

### Description
```markdown
## Adaptive Throughput Timer

A dynamic JMeter timer plugin that adjusts throughput (TPS) and thread count in real-time based on CSV-defined load profiles.

### Features

• **CSV-Based Load Profiles** — Define target TPS, start/end times, and step-based profiles
• **Dynamic Thread Scaling** — Automatically adjusts thread count based on current vs. target throughput
• **24-Hour Infinite Execution** — Cycle test profiles every 24 hours with validation
• **Dynamic CSV Reload** — File checked every 60 seconds; modifications applied without stopping test
• **Load Profile Modes** — Step-based (fixed duration), time-based (HH:mm ranges), and default (current time alignment)
• **P90 Latency Monitoring** — Thread adjustment considers latency percentiles
• **Load Profile Visualization** — Real-time graph showing TPS over time
• **GUI Configuration** — Intuitive panel for all settings with file browser and preview

### Use Cases

- **Load Testing Ramp-up/Ramp-down** — Gradually increase and decrease TPS
- **Sustained Load Testing** — Maintain constant TPS for duration
- **24-Hour Load Cycles** — Repeat load profile every 24 hours for continuous testing
- **Live Load Adjustments** — Modify CSV during test to change load profile (reload happens every minute)

### Links

• GitHub: https://github.com/bakthava/Adaptive-Throughput-Timer
• Release JAR: https://github.com/bakthava/Adaptive-Throughput-Timer/releases/download/v1.0.0/adaptive-throughput-timer-1.0.0.jar

Java 8 compatible (bytecode version 52).

### Changes Made

- Added plugin entry to `site/dat/repo/various.json`
- Plugin ID: `adaptive-throughput-timer`
- Version: `1.0.0` (initial release)
- Main class: `com.adaptive.jmeter.plugins.AdaptiveTimerFromCSV`
- Component classes registered for GUI discovery
```

## Commit Details

**Branch:** `add-adaptive-throughput-timer`  
**Commit:** Click on the commit in the PR to view details  
**Modified File:** `site/dat/repo/various.json` (19 insertions)

## Version Information

- **Plugin Version:** 1.0.0
- **Java Compatibility:** JDK 8+ (bytecode version 52)
- **JMeter Version:** Compatible with latest JMeter versions

## Next Steps

1. Create the PR using one of the methods above
2. The jmeter-plugins maintainers will review
3. After merge, your plugin will be available in the jmeter-plugins repository
4. Users can discover and install via JMeter plugin manager

---

**Reference PR:** https://github.com/undera/jmeter-plugins/pull/800 (LLM Metrics Visualizer - used as template)
