# Adaptive Throughput Timer - Usage Guide

## Overview

The Adaptive Throughput Timer is a JMeter plugin that automatically adjusts thread count to achieve target throughput defined in a CSV file. It monitors the 90th percentile latency every second and dynamically scales threads up or down to meet target TPS goals.

## Key Features

✅ **Multi-Format Support** - Load profiles from CSV, TXT, and Excel files  
✅ **Dynamic Thread Adjustment** - Automatically scales threads  
✅ **90th Percentile Monitoring** - Tracks latency metrics every second  
✅ **Intelligent Ramping** - Configurable ramp-up/down steps  
✅ **Real-time Metrics** - Current vs target TPS comparison  

## How It Works

```
1. User provides load profile file (CSV, TXT, or Excel) with time/TPS targets
2. Plugin reads file and initializes with Min Threads count
3. Every second:
   - Calculates current throughput (samples/second)
   - Calculates 90th percentile response time
   - Compares current TPS vs target TPS
   - If deviation > 5%:
     * Increase threads if TPS is too low
     * Decrease threads if TPS is too high AND latency is acceptable
4. Adjust threads and reset metrics for next window
5. Repeat until test completes
```

## Load Profile File Format

Supported formats: **CSV, TXT, Excel (.xlsx, .xls)**

### CSV and TXT Format

**Format:** `stepcount,mm:ss,tps`

Where:
- `stepcount` = Step number in the profile (1, 2, 3, ...)
- `mm` = minutes (0-59 or more)
- `ss` = seconds (0-59)
- `tps` = target transactions per second

**Example: throughput-profile.csv**
```csv
# Ramp up phase - increase from 100 to 500 TPS over 1 minute
1,00:10,100
2,00:30,200
3,01:00,500

# Sustained load phase - hold at 1000 TPS for 3 minutes  
4,02:00,1000
5,03:00,1000
6,04:00,1000

# Ramp down phase - reduce load
7,05:00,500
8,05:30,100
```

### Excel Format (.xlsx or .xls)

Columns:
- **Column A (A)** - Step count (1, 2, 3, ...)
- **Column B (B)** - Time in format `mm:ss`
- **Column C (C)** - Target TPS

**Example: throughput-profile.xlsx**
| Step | Time  | TPS  |
|------|-------|------|
| 1    | 00:10 | 100  |
| 2    | 00:30 | 200  |
| 3    | 01:00 | 500  |
| 4    | 02:00 | 1000 |

### Text File Format (.txt)

Same format as CSV:
```
1,00:10,100
2,00:30,200
3,01:00,500
```

## Configuration Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| **File Path** | - | Full path to load profile file (CSV, TXT, or Excel) (required) |
| **Min Threads** | 1 | Starting number of threads and minimum allowed |
| **Max Threads** | 100 | Maximum allowed threads |
| **Adjustment Interval** | 5000ms | How often to check and adjust threads |
| **Ramp Up Step** | 1 | Threads to add per adjustment |
| **Ramp Down Step** | 1 | Threads to remove per adjustment |
| **P90 Threshold** | 500ms | Max acceptable 90th percentile latency |

## Load Profile Visualization

### Visual Graph

Below is a graphical representation showing how TPS increases over time with ramp-up, sustained load, and ramp-down phases:

```
TPS TARGET PROGRESSION OVER TIME
═════════════════════════════════════════════════════════════

    TPS  │  Phase
  (Trans │  Information
   /Sec) │
─────────┼──────────────────────────────────────────────────────
  1000   ├─────────────────────────────┐
         │                             │ Sustained Phase
   900   │                             │ Steps 4-6: Hold 1000 TPS
   800   │                             │
   700   │                             │
   600   │                             │
   500   │              ╱──────────────┤ Ramp Down ↓
   400   │            ╱                │ (Step 7-8)
   300   │          ╱                  │
   200   │        ╱                    ╲
   100   │      ╱                        ╲
         │    ╱                            ╲
    ─────┴──────────────────────────────────────────── Time
         0  00:30   01:00   02:00   03:00   05:00   05:30
                ▲                   ▲               ▲
            Ramp-up             Sustained      Ramp-down
            Steps 1-3           Load Phase     Steps 7-8
```

### Step-by-Step Progression Table

| Step | Time   | TPS  | Phase | Duration | Action |
|------|--------|------|-------|----------|--------|
| 1 | 00:30 | 100 | Ramp-up | 30s | Start ramping: add 2 threads every 5s |
| 2 | 01:00 | 200 | Ramp-up | 30s | Continue ramping: add 2 threads every 5s |
| 3 | 02:00 | 500 | Ramp-up | 60s | Continue ramping: add 2 threads every 5s |
| 4 | 03:00 | 1000 | Sustained | 60s | Hold load: threads remain stable |
| 5 | 04:00 | 1000 | Sustained | 60s | Hold load: threads remain stable |
| 6 | 05:00 | 1000 | Sustained | 60s | Hold load: threads remain stable |
| 7 | 06:00 | 500 | Ramp-down | 60s | Reduce: remove 1 thread every 5s |
| 8 | 06:30 | 100 | Ramp-down | 30s | Reduce: remove 1 thread every 5s |

### Understanding the Phases

**🔺 RAMP-UP PHASE** (Steps 1-3: 00:30 - 02:00)
```
┌─ Gradually increase TPS
│  └─ From 100 TPS → 500 TPS
│     └─ Plugin adds threads to reach each target
│        └─ Controlled by: Ramp Up Step (default: 2 threads per adjustment)
└─ Timeline: About 90 seconds total
```

**➡️ SUSTAINED LOAD PHASE** (Steps 4-6: 02:00 - 05:00)
```
┌─ Hold steady TPS
│  └─ Maintain 1000 TPS consistently
│     └─ Plugin keeps thread count stable
│        └─ Absorbs natural traffic variations
└─ Timeline: About 180 seconds total
```

**🔻 RAMP-DOWN PHASE** (Steps 7-8: 05:00 - 06:30)
```
┌─ Gradually reduce TPS
│  └─ From 1000 TPS → 100 TPS
│     └─ Plugin removes threads gradually
│        └─ Controlled by: Ramp Down Step (default: 1 thread per adjustment)
└─ Timeline: About 90 seconds total
```

## JMeter Setup Steps

### 1. Prepare Your Load Profile File

Create `throughput-profile.csv` (or `.txt` / `.xlsx`):
```csv
1,00:30,100
2,01:00,250
3,02:00,500
4,03:00,1000
```

### 2. Build the Plugin

```bash
cd adaptive-throughput-timer
mvn clean package
```

This creates: `target/adaptive-throughput-timer-1.0.0.jar`

### 3. Install in JMeter

```bash
# Copy JAR to JMeter plugins directory
cp target/adaptive-throughput-timer-1.0.0.jar $JMETER_HOME/lib/ext/

# Restart JMeter
```

### 4. Configure in JMeter

1. Open JMeter and create a Thread Group
2. Add a Sampler (e.g., HTTP Request)
3. **Add > Timers > Adaptive Timer From CSV**
4. Configure the timer:
   - **File Path**: `/path/to/throughput-profile.csv` (or `.txt` / `.xlsx`)
   - **Min Threads**: `1` (starting thread count)
   - **Max Threads**: `50`
   - **Adjustment Interval**: `5000` (check every 5 seconds)
   - **Ramp Up Step**: `2` (increase by 2 threads each time)
   - **Ramp Down Step**: `1` (decrease by 1 thread each time)
   - **P90 Threshold**: `500` (max acceptable latency)

5. Set Thread Group to match or be greater than **Min Threads**

6. Run the test

## Thread Adjustment Algorithm

Every adjustment interval, the plugin:

```
current_tps = samples_in_last_interval / interval_duration
target_tps = from_csv_for_current_time

tps_deviation = (target_tps - current_tps) / target_tps * 100

if |tps_deviation| > 5%:
  if tps_deviation > 0:
    threads += ramp_up_step  // Too slow, add threads
  else if tps_deviation < -10% AND p90_latency < threshold:
    threads -= ramp_down_step  // Too fast and latency is good, reduce threads

threads = clamp(threads, min_threads, max_threads)
```

## Example Scenarios

### Scenario 1: Gradual Ramp-up Test

**CSV:**
```csv
1,00:30,100
2,01:00,200
3,02:00,500
4,03:00,1000
```

**Config:**
- Min Threads: 1
- Max Threads: 50
- Ramp Up Step: 2
- Adjustment Interval: 5000ms

**Expected Behavior:**
- Starts with 1 thread and scales up
- If actual TPS < 100, adds 2 threads every 5 seconds
- Repeats for each profile step
- Adjusts to achieve 200 TPS in next stage, then 500 TPS, then 1000 TPS

### Scenario 2: Sustained Load with Latency Control

**CSV:**
```csv
1,00:10,500
2,01:00,1000
3,02:00,1000
4,03:00,1000
```

**Config:**
- Min Threads: 5
- Max Threads: 100
- Ramp Up Step: 5
- Ramp Down Step: 2
- P90 Threshold: 300ms
- Adjustment Interval: 2000ms

**Expected Behavior:**
- Starts with 5 threads
- Ramps up faster (5 threads at a time)
- Maintains 1000 TPS while keeping P90 < 300ms
- If P90 exceeds threshold, may not ramp down even if TPS is above target
- More aggressive adjustment (every 2 seconds)

### Scenario 3: Stress Testing

**CSV:**
```csv
1,00:30,1000
2,01:00,2000
3,02:00,3000
```

**Config:**
- Min Threads: 10
- Max Threads: 200
- Ramp Up Step: 10
- Ramp Down Step: 5
- P90 Threshold: 1000ms (higher tolerance)
- Adjustment Interval: 2000ms

**Expected Behavior:**
- Starts with 10 threads
- Aggressive ramp-up (10 threads at a time)
- Accepts higher latency (1000ms)
- Continuously increases threads to reach higher TPS targets (1000 → 2000 → 3000 TPS)
- Good for finding system limits

## Monitoring

Watch the JMeter console output for thread adjustment logs:

```
[5000ms] Adjustment: Current TPS=85.5, Target=100, P90=245.2ms, Threads: 5 -> 7
[10000ms] Adjustment: Current TPS=102.3, Target=100, P90=252.1ms, Threads: 7 -> 7
[15000ms] Adjustment: Current TPS=1050.5, Target=1000, P90=280.5ms, Threads: 15 -> 14
```

## Best Practices

1. **Start conservatively** - Begin with low Min Threads (1-5)
2. **Set realistic min/max** - Don't set Max Threads too high initially
3. **Use small ramp steps** - Prevents overshooting target TPS
4. **Monitor latency** - Set P90 Threshold based on SLA requirements
5. **Test load profile file first** - Ensure format and time values are correct
6. **Enable Response Time graph** - Visualize latency changes
7. **Run for sufficient duration** - Let adjustment algorithm stabilize

## Troubleshooting

### Issue: Threads not increasing
- Check file path is correct and file exists
- Verify format is `stepcount,mm:ss,tps` for CSV/TXT
- Verify file has correct columns for Excel (A=stepcount, B=time, C=tps)
- Check Min/Max thread settings
- Ensure Ramp Up Step > 0

### Issue: TPS never reaches target
- Increase Max Threads
- Increase Ramp Up Step
- Check if system is actually capable
- Verify sampler is working correctly

### Issue: Threads keep decreasing
- Increase P90 Threshold (allow more latency)
- Reduce Ramp Down Step
- Check if workload has natural variability

### Issue: File not loading
- Use absolute file path (not relative)
- Check file permissions
- Verify file is UTF-8 encoded (for CSV/TXT)
- Ensure CSV/TXT format is `stepcount,mm:ss,tps`
- For Excel: ensure step numbers in column A, times in column B, TPS in column C
- Ensure time format is exactly `mm:ss` (with leading zeros if < 10)

## Integration with Other JMeter Components

The Adaptive Timer works well with:

- **HTTP Sampler** - For web application testing
- **Database Sampler** - For database load testing
- **Listeners** - Aggregate Report, Response Time Graph, etc.
- **Assertions** - Verify response data quality
- **Extractors** - Session/token extraction for stateful tests

## Data Collection

To export metrics:

1. Add **Aggregate Report** listener
2. Add **Response Time Percentiles Over Time** listener
3. Run test
4. Export results for analysis

## Files Generated

- `target/adaptive-throughput-timer-1.0.0.jar` - Plugin JAR
- CSV files remain in your working directory
- JMeter generates standard `.jtl` results files

---

For more information, see [README.md](README.md)
