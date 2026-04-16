# Adaptive Throughput Timer Plugin

A JMeter plugin that dynamically adjusts thread count to achieve target throughput defined in a CSV file. It calculates 90th percentile latency per second and automatically increases or decreases thread count to meet specified TPS targets.

## Features

- **CSV-based Configuration**: Define time and target TPS in a CSV file
- **Dynamic Thread Adjustment**: Automatically adjusts threads to meet TPS targets
- **Percentile Monitoring**: Calculates 90th percentile latency every second
- **Configurable Thresholds**: Control ramp-up/down steps, min/max threads, and adjustment intervals
- **Real-time Metrics**: Tracks current throughput vs target and latency metrics

## Project Structure

```
adaptive-throughput-timer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/adaptive/jmeter/plugins/
│   │   │       ├── AdaptiveTimerFromCSV.java          (Main timer component)
│   │   │       ├── AdaptiveTimerFromCSVGui.java       (GUI configuration)
│   │   │       ├── ThroughputMetrics.java             (Metrics tracking)
│   │   │       ├── CSVThroughputReader.java           (CSV file reading)
│   │   │       └── CSVThroughputEntry.java            (Data model)
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/adaptive/jmeter/plugins/
│               ├── AdaptiveTimerFromCSVTest.java
│               ├── CSVThroughputReaderTest.java
│               └── ThroughputMetricsTest.java
├── pom.xml
├── example-throughput.csv
└── README.md
```

## CSV/TXT/Excel Format

CSV/TXT files use comma-separated values with format: `stepcount,mm:ss,tps`

Excel files (.xlsx, .xls) should have:
- Column A: stepcount (step number)
- Column B: time (mm:ss format)
- Column C: tps (transactions per second)

**Supported file formats**:
- `.csv` (Comma-Separated Values)
- `.txt` (Text file - same format as CSV)
- `.xlsx` (Excel 2007-2016)
- `.xls` (Excel 97-2003)

**Example CSV/TXT**:
```csv
# Load testing profile
# Format: stepcount,mm:ss,tps

1,00:30,100
2,01:00,250
3,02:00,500
4,03:00,1000
5,05:00,500
```

**Example Excel File**:
```
A         B        C
stepcount time     tps
1         00:30    100
2         01:00    250
3         02:00    500
4         03:00    1000
5         05:00    500
```

## Configuration Options

| Parameter | Default | Description |
|-----------|---------|-------------|
| CSV File Path | - | Path to CSV/TXT/Excel file with time/TPS targets |
| Min Threads | 1 | **Starting thread count** (was called Initial Threads) |
| Max Threads | 100 | Maximum thread count |
| Adjustment Interval (ms) | 5000 | How often to check and adjust threads |
| Ramp Up Step | 1 | Threads to add per adjustment |
| Ramp Down Step | 1 | Threads to remove per adjustment |
| P90 Threshold (ms) | 500 | Maximum acceptable 90th percentile latency |

**Note**: The plugin starts with **Min Threads** as the initial thread count (no separate Initial Threads parameter).

## Building

```bash
mvn clean package
```

The compiled JAR will be located in `target/adaptive-throughput-timer-1.0.0.jar`

## Installation

1. Build the plugin: `mvn clean package`
2. Copy JAR to JMeter: `cp target/adaptive-throughput-timer-*.jar $JMETER_HOME/lib/ext/`
3. Restart JMeter
4. The "Adaptive Timer From CSV" will appear in: Thread Group > Add > Timers

## Thread Adjustment Algorithm

Every `Adjustment Interval` milliseconds, the plugin:

1. Calculates current throughput (samples/second)
2. Calculates 90th percentile response time
3. Compares current TPS vs target TPS
4. If deviation > 5%:
   - **Increase threads** if current TPS < target (ramp up)
   - **Decrease threads** if current TPS >> target AND P90 latency is acceptable (ramp down)
5. Resets metrics for next window

## Usage Example

1. Create a CSV file `throughput-profile.csv`:
   ```csv
   00:30,100
   01:00,500
   02:00,1000
   05:00,500
   ```

2. In JMeter:
   - Add Thread Group
   - Add "Adaptive Timer From CSV" to the thread group
   - Configure CSV file path
   - Set initial threads and min/max ranges
   - Run test

## Components

### AdaptiveTimerFromCSV
Main timer component that implements JMeter's `Timer` interface. Manages thread adjustment logic and metrics collection.

### ThroughputMetrics
Tracks response times in a sliding window and calculates percentiles, throughput, and error rates.

### CSVThroughputReader
Reads and parses CSV files containing time/TPS definitions.

### AdaptiveTimerFromCSVGui
Swing GUI component for configuring the timer in JMeter's UI.

## Testing

Run tests:
```bash
mvn test
```

Test coverage includes:
- CSV file parsing
- Percentile calculations
- Thread adjustment logic
- Property management

## License

Apache License 2.0
