# Adaptive Throughput Timer - Technical Architecture

## Overview

The Adaptive Throughput Timer plugin is built as a JMeter Timer component that dynamically adjusts thread count to achieve target throughput. It consists of several interconnected components working together.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    JMeter Test Execution                     │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
         ┌───────────────────────────────┐
         │  AdaptiveTimerFromCSV         │
         │  (Main Timer Component)       │
         ├───────────────────────────────┤
         │ - delay()                     │
         │ - initializeTest()            │
         │ - adjustThreadCount()         │
         │ - recordResponseTime()        │
         └─────┬───────────────┬─────────┘
               │               │
        ┌──────▼─┐      ┌──────▼──────────┐
        │ CSV    │      │  Throughput     │
        │Reader  │      │  Metrics        │
        ├────────┤      ├─────────────────┤
        │ Parse  │      │ - Record times  │
        │ Time   │      │ - Calculate P90 │
        │ TPS    │      │ - Track TPS     │
        └────────┘      │ - Error rate    │
                        └─────────────────┘
```

## Core Components

### 1. AdaptiveTimerFromCSV (Main)

**File**: `src/main/java/com/adaptive/jmeter/plugins/AdaptiveTimerFromCSV.java`

**Responsibilities**:
- Implements JMeter's `Timer` interface
- Manages test lifecycle (init, delay, adjustment)
- Orchestrates thread count decisions
- Thread-safe singleton metrics tracking

**Key Methods**:

```java
delay()  // Called by every sampler before execution
  ├─ Initialize on first call
  ├─ Check adjustment interval
  ├─ Calculate target TPS for current time
  └─ Return delay based on target TPS

adjustThreadCount()  // Called periodically
  ├─ Calculate current TPS
  ├─ Get 90th percentile latency
  ├─ Compare vs target TPS
  ├─ Decide thread adjustment
  └─ Update thread count

recordResponseTime(ms)  // Called by compatible samplers
  └─ Add response time to metrics window

recordError()  // Called for failed samples
  └─ Increment error counter
```

**Thread Safety**: Uses `volatile` fields and double-checked locking for initialization.

### 2. CSVThroughputReader

**File**: `src/main/java/com/adaptive/jmeter/plugins/CSVThroughputReader.java`

**Purpose**: Parses load profile files (CSV, TXT, Excel) and retrieves target TPS for given timestamps.

**Supported Formats**:
- CSV files (.csv) - Comma-separated text format
- Text files (.txt) - Comma-separated text format
- Excel files (.xlsx, .xls) - Using Apache POI library

**Key Methods**:

```java
readFile(String filePath)  // Auto-detects format and parses
  ├─ Determine file extension
  ├─ Route to appropriate parser:
  │  ├─ readTextFile() for .csv and .txt
  │  └─ readExcelFile() for .xlsx and .xls
  └─ Return List<CSVThroughputEntry>

getTargetTpsForTime(List<entries>, long elapsedMs)  // Lookup by time
  ├─ Find highest entry <= current time
  └─ Return TPS or 0 if no entry reached
```

**File Format Support**:

*CSV/TXT Format*:
- Uses regex: `(\d+):(\d+)` for time format
- Supports comments: lines starting with `#`
- Format: `stepcount,mm:ss,tps`

*Excel Format*:
- Column A: Step count (1, 2, 3, ...)
- Column B: Time (mm:ss format)
- Column C: Target TPS

### 3. CSVThroughputEntry

**File**: `src/main/java/com/adaptive/jmeter/plugins/CSVThroughputEntry.java`

**Purpose**: Data model for a single load profile entry.

**Attributes**:
- `stepCount`: Sequential step number in profile (1, 2, 3, ...)
- `minutes`: Time in minutes
- `seconds`: Time in seconds (0-59)
- `targetTps`: Target transactions per second

**Key Method**:
```java
getTotalTimeMs()  // Returns (minutes * 60 + seconds) * 1000
toString()        // Returns "Step {stepCount}: {time} - {TPS} TPS"
```

### 4. ThroughputMetrics

**File**: `src/main/java/com/adaptive/jmeter/plugins/ThroughputMetrics.java`

**Purpose**: Tracks samples and calculates metrics for a time window.

**Data Structures**:
```java
ConcurrentLinkedQueue<Long> responseTimes  // Thread-safe response time buffer
volatile long sampleCount  // Total samples recorded
volatile long errorCount   // Total errors recorded
volatile long lastResetTime  // Timestamp of last reset
```

**Key Methods**:

```java
recordResponseTime(long ms)  // Add response time
getCurrentThroughput()       // samples * 1000 / elapsed_ms
getPercentile(double pct)    // Nth percentile calculation
get90thPercentile()           // Shortcut for 90th percentile

reset()  // Clear buffer for new window
```

**Percentile Calculation**:
1. Copy response times to sorted list
2. Calculate index: `ceil((percentile / 100) * size) - 1`
3. Return value at index

### 5. AdaptiveTimerFromCSVGui

**File**: `src/main/java/com/adaptive/jmeter/plugins/AdaptiveTimerFromCSVGui.java`

**Purpose**: Swing GUI for configuration in JMeter.

**Components**:
- CSV file path text field + browse button
- Thread range spinners (min/max)
- Adjustment interval input
- Ramp-up/down step controls
- P90 threshold field

**Integration Points**:
- `configure(TestElement)` - Load from saved test element
- `modifyTestElement(TestElement)` - Save to test element
- `createTestElement()` - Create new timer instance

## Data Flow

### Test Execution Flow

```
1. JMeter creates AdaptiveTimerFromCSV instance
   ├─ Load from saved .jmx or create new
   └─ Set properties from GUI
   
2. Thread Group starts threads
   └─ Each thread runs sampler loop
   
3. For each sample:
   a. delay() called
      └─ Check if first call → initialize
   
   b. Sampler executes (HTTP, etc.)
      └─ Measures response time
   
   c. Record metrics:
      ├─ recordResponseTime(elapsed_ms)
      └─ recordError() if failed
   
   d. Check adjustment interval
      └─ If interval passed:
         ├─ Get current TPS
         ├─ Get target TPS from CSV
         ├─ Decide thread change
         └─ Reset metrics window
```

### Metrics Window

```
Window Duration: 1 second (1000ms)

Timeline:
[Start Window] ──── T1 ──── T2 ──── T3 ──── [Calculate] ──── [Reset]
  └─ Record resp times ──────────────────────────────────┘
                          └─ Sum responses per second = TPS
```

## Configuration Persistence

JMeter stores timer configuration in test plan `.jmx` files:

```xml
<Timer guiclass="com.adaptive.jmeter.plugins.AdaptiveTimerFromCSVGui" 
       testclass="com.adaptive.jmeter.plugins.AdaptiveTimerFromCSV">
  <elementProp name="filePath">
    <stringProp name="filePath">/path/to/profile.csv</stringProp>
  </elementProp>
  <longProp name="minThreads">1</longProp>
  <longProp name="maxThreads">50</longProp>
  <!-- ... more properties ... -->
</Timer>
```

## Thread Adjustment Algorithm

```
adjustThreadCount():
  
  Step 1: Get current metrics
    ├─ current_tps = samples_count / (time_elapsed / 1000)
    ├─ p90_latency = sorted_response_times[90th_percentile]
    └─ error_rate = error_count / total_count * 100
  
  Step 2: Get target from CSV
    └─ target_tps = getTargetTpsForTime(elapsed_time)
  
  Step 3: Calculate deviation
    ├─ tps_difference = target_tps - current_tps
    ├─ tps_percentage_diff = (tps_difference / target_tps) * 100
    └─ threshold = 5% (configurable via code)
  
  Step 4: Decision logic
    if |tps_percentage_diff| > threshold:
    
      if tps_percentage_diff > 0:
        // Too slow - increase threads
        new_threads = min(current_threads + ramp_up_step, max_threads)
      
      else if tps_percentage_diff < -10% AND p90_latency < threshold:
        // Too fast AND latency acceptable - reduce threads
        new_threads = max(current_threads - ramp_down_step, min_threads)
    
    Step 5: Update
      if new_threads != current_threads:
        updateThreadCount(new_threads)
        LOG adjustment event
      
      reset metrics for next window
```

## Concurrency Considerations

### Thread Safety

- **Static fields**: `csvEntries`, `metrics`, `testStartTime`, `initialized`
  - Protected by `INIT_LOCK` during initialization
  - Marked `volatile` for visibility
  
- **ThroughputMetrics**: Uses `ConcurrentLinkedQueue` for thread-safe buffer

- **Per-thread storage**: Each JMeter thread has its own `AdaptiveTimer` instance

### Synchronization

```java
if (!initialized) {
  synchronized (INIT_LOCK) {  // Double-checked locking
    if (!initialized) {
      initializeTest();
    }
  }
}
```

## Extension Points

### Adding New Metrics

Extend `ThroughputMetrics` class:

```java
public long getMedianLatency() {
  List<Long> sorted = new ArrayList<>(responseTimes);
  Collections.sort(sorted);
  return sorted.get(sorted.size() / 2);
}
```

### Custom Adjustment Logic

Override `adjustThreadCount()` in `AdaptiveTimerFromCSV`:

```java
protected void adjustThreadCount() {
  // Custom algorithm here
}
```

### GUI Enhancements

Extend `AdaptiveTimerFromCSVGui`:

```java
public class EnhancedGui extends AdaptiveTimerFromCSVGui {
  private JChart performanceChart;
  // Add visualization
}
```

## Performance Considerations

- **No blocking I/O**: CSV parsed once at startup
- **Memory**: Metrics stored in queue (configurable window size)
- **CPU**: Percentile calculation is O(n log n) but only per window
- **Concurrency overhead**: Minimal with volatile fields + queue

## Testing Strategy

Test classes:
- `AdaptiveTimerFromCSVTest` - Component functionality (6 tests)
- `CSVThroughputReaderTest` - Multi-format parsing & TPS lookup (6 tests)
- `ThroughputMetricsTest` - Metric calculations (5 tests)
- `AdaptiveTimerTest` - Base timer properties (5 tests)

**Total: 22 tests**

## Known Limitations

1. **Thread pool incompatibility** - Works best with standard Thread Group
2. **Sampling bias** - Metrics based on actual samples, not strict time windows
3. **No session management** - Cannot correlate user sessions
4. **Manual thread update** - Thread adjustment is advisory, requires implementation
5. **Single metric window** - All threads share one metrics window

## Future Enhancements

- [ ] Multiple metrics windows per thread
- [ ] Distributed metrics collection (master-slave)
- [ ] ML-based prediction of thread needs
- [ ] Integration with external monitoring systems
- [ ] Support for time-series profiles with variance
- [ ] Export metrics to JSON/CSV for analysis
- [ ] JSON format support for load profiles

---

For implementation details, review the source code comments and JavaDoc annotations.
