# Graphs Generator Listener

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-ggl)</span>

## Description
Graphs Generator Listener generates the following graphs at end of test:
  - [Active Threads Over Time](/wiki/ActiveThreadsOverTime/)
  - [Response Times Over Time](/wiki/ResponseTimesOverTime/)
  - [Transactions per Second](/wiki/TransactionsPerSecond/)
  - [Server Hits per Seconds](/wiki/HitsPerSecond/)
  - [Response Codes per Second](/wiki/ResponseCodesPerSecond/)
  - [Response Latencies Over Time](/wiki/LatenciesOverTime/)
  - [Bytes Throughput Over Time](/wiki/BytesThroughput/)
  - [Response Times vs Threads](/wiki/ResponseTimesVsThreads/)
  - [Transaction Throughput vs Threads](/wiki/TransactionThroughputVsThreads/)
  - [Response Times Distribution](/wiki/RespTimesDistribution/)
  - [Response Times Percentiles](/wiki/RespTimePercentiles/)
    
It also generates since version 1.2.2:
  - a CSV file containing the Aggregate Report data
  - a CSV file containing the Summary Report data
Graphs Generator Listener requires JMeter 2.10 or superior.
You can use this listener in 2 ways:
  - to generate CSV or PNG or both formats  for current test results
  - to generate CSV or PNG or both formats for existing/previous test results

### Generate CSV / PNG for current test results

To do this, you have to ensure Results are flushed to file so that when Graphs Generator Listener runs, it does on a complete file.

You have 2 options:

#Option 1:  
  - Configure a View Results Tree so that it outputs results to a file (Use this listener only in NON-GUI mode for performance purposes)
  - Ensure View Results Tree listener is **BEFORE** Graphs Generator Listener

#Option 2:
Change JMeter settings in user.properties to ensure autoflush is used:
  - jmeter.save.saveservice.autoflush=true
In this case, you just need to put Graphs Generator Listener as only listener and run in NON-GUI mode
   

### Generate CSV / PNG for existing/previous test results

To do this, you will have to create a "Fake" test to trigger listener on existing file.

This is very simple:
  - Create one thread group with 1 thread and 1 iteration
  - Put a debug sampler as child of it
  - Run test
  - PNGs or CSVs or Both files will be generated for the results file you have configured in Graphs Generator Listener

![](/img/wiki/listener/GraphsGeneratorListener.png)

## Examples
