# Quick Start Guide - Adaptive Throughput Timer

## 5-Minute Setup

### Step 1: Create CSV File
Save as `throughput.csv`:
```csv
1,00:30,100
2,01:00,250
3,02:00,500
4,03:00,1000
5,04:00,500
6,04:30,100
```

### Step 2: Build Plugin
```bash
cd adaptive-throughput-timer
mvn clean package -DskipTests
```

Output: `target/adaptive-throughput-timer-1.0.0.jar`

### Step 3: Install in JMeter
```bash
cp target/adaptive-throughput-timer-1.0.0.jar $JMETER_HOME/lib/ext/
```

### Step 4: Restart JMeter
Restart JMeter completely so it loads the new plugin.

### Step 5: Create Test Plan

1. Create new Test Plan
2. Add Thread Group:
   - Number of Threads: 10
   - Ramp-Up Period: 10
   - Loop Count: -1 (infinite)

3. Add HTTP Sampler:
   - Server: example.com
   - Path: /api/endpoint

4. Add "Adaptive Timer From CSV":
   - **Add > Timers > Adaptive Timer From CSV**
   - CSV File Path: `/absolute/path/to/throughput.csv` (supports .csv, .txt, .xlsx, .xls)
   - Min Threads: `1` (starting threads - automatically derived)
   - Max Threads: `50`
   - Adjustment Interval: `5000ms`
   - Ramp Up Step: `2`
   - Ramp Down Step: `1`
   - P90 Threshold: `500ms`

5. Add Listeners:
   - Aggregate Report
   - Response Time Graph
   - View Results Tree

### Step 6: Run Test
- Click Start
- Watch console for thread adjustments
- Test will run for total duration in CSV

## What Happens

```
Timeline:        0:00         0:30         1:00         2:00         3:00
CSV Target:   100 TPS ──→ 100 TPS ──→ 250 TPS ──→ 500 TPS ──→ 1000 TPS
Threads:         1   ↗  3   ↗  5   ↗  10   ↗  20   ↗  35   ↗  40 TPS→ Done
System:       Ramp UP          UP           UP           UP       Sustain
```

## Example Output Console

```
AdaptiveTimerFromCSV initialized:
  File: /path/to/throughput.csv
  Total entries: 6
  Starting threads (min): 1
  Entries: [Step 1: 00:30 - 100 TPS, Step 2: 01:00 - 250 TPS, Step 3: 02:00 - 500 TPS, ...]

[5000ms] Adjustment: Current TPS=85.3, Target=100, P90=245.1ms, Threads: 1 -> 3
[10000ms] Adjustment: Current TPS=97.2, Target=100, P90=251.3ms, Threads: 3 -> 3
[15000ms] Adjustment: Current TPS=101.5, Target=100, P90=252.1ms, Threads: 3 -> 3
[30000ms] Adjustment: Current TPS=245.8, Target=250, P90=280.5ms, Threads: 3 -> 5
[35000ms] Adjustment: Current TPS=250.1, Target=250, P90=282.3ms, Threads: 5 -> 5
[60000ms] Adjustment: Current TPS=495.3, Target=500, P90=310.2ms, Threads: 5 -> 7
[120000ms] Adjustment: Current TPS=998.7, Target=1000, P90=401.5ms, Threads: 20 -> 20
```

## CSV Format Reference

| Time | TPS | Stepcount | Meaning |
|------|-----|-----------|---------|
| 00:10 | 50 | 1 | Step 1: At 10 seconds, target 50 TPS |
| 00:30 | 100 | 2 | Step 2: At 30 seconds, target 100 TPS |
| 01:00 | 500 | 3 | Step 3: At 1 minute, target 500 TPS |
| 02:00 | 1000 | 4 | Step 4: At 2 minutes, target 1000 TPS |
| 05:00 | 200 | 5 | Step 5: At 5 minutes, ramp down to 200 TPS |

**Format**: `stepcount,mm:ss,tps`  
**Supported files**: .csv, .txt, .xlsx, .xls  
**Important**: Times are cumulative from test start

## Configuration Quick Reference

### For Load Testing
```
Adjustment Interval: 5000ms  (check every 5 seconds)
Ramp Up Step: 2              (gradual increase)
Ramp Down Step: 1
P90 Threshold: 500ms         (accept some latency)
```

### For Stress Testing
```
Adjustment Interval: 2000ms  (check every 2 seconds - aggressive)
Ramp Up Step: 5              (faster ramp)
Ramp Down Step: 2
P90 Threshold: 1000ms        (high tolerance)
```

### For Precision Testing
```
Adjustment Interval: 10000ms (check every 10 seconds)
Ramp Up Step: 1              (conservative)
Ramp Down Step: 1
P90 Threshold: 200ms         (strict latency)
```

## Verify Installation

1. Start JMeter
2. Create a Thread Group
3. Go to: **Add > Timers**
4. Look for: **Adaptive Timer From CSV**
5. If you see it → Success! ✅

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Timer not showing | Restart JMeter, check JAR in `/lib/ext/` |
| CSV not loading | Use absolute path, verify `mm:ss` format |
| Threads not changing | Check Min/Max range, increase Ramp Up Step |
| Test completes immediately | Ensure CSV file has entries spanning test duration |
| High latency spikes | Lower P90 Threshold to be more aggressive on ramp downs |

## Files Provided

```
adaptive-throughput-timer/
├── target/
│   └── adaptive-throughput-timer-1.0.0.jar    ← COPY THIS TO JMeter
├── src/
│   ├── main/java/com/adaptive/jmeter/plugins/
│   │   ├── AdaptiveTimerFromCSV.java           (Main timer)
│   │   ├── ThroughputMetrics.java              (Metric calculations)
│   │   ├── CSVThroughputReader.java            (CSV parsing)
│   │   └── AdaptiveTimerFromCSVGui.java        (Configuration UI)
│   └── test/java/...                           (Unit tests)
├── example-throughput.csv                      (Example file)
├── README.md                                    (Full documentation)
├── USAGE.md                                     (Detailed usage guide)
└── ARCHITECTURE.md                              (Technical details)
```

## Next Steps

1. ✅ Build: `mvn clean package`
2. ✅ Install: Copy JAR to JMeter
3. ✅ Configure: Create CSV and JMeter test plan
4. ✅ Run: Click Start in JMeter
5. ✅ Analyze: Review metrics in listeners

## Getting Help

- Check **README.md** for complete feature list
- See **USAGE.md** for detailed examples
- Review **ARCHITECTURE.md** for technical details
- Check JMeter console output for adjustment logs

---

**Ready to run your first test? Let's go!** 🚀
