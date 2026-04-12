# Changelog

!! The changelog in its current form has stopped from being updated in favor of smaller plugins, with Plugins Manager !! 

## 2.0 <i><font color=gray size="1">planning</font></i>
  - all plugin sets are broken up into tens of smaller plugins to be used with Plugins Manager
  - prevent NPEs from sense uploader
  - sense uploader is extracted as separate plugin for pmgr
  - perfmon is extracted as separate plugin for pmgr
  - json components are extracted as separate module for pmgr
  - hadoop set won't be distributed as zip anymore
  - jmxmon : url field can resolves jmeter variables defined during the test and not only at the begining 

## 1.4.0 <i><font color=gray size="1">April 5, 2016</font></i>
  - BlazeMeter Inc. has contributed [XMPP testing plugins](/wiki/XMPPSet/)
  - BlazeMeter Inc. has contributed PhantomJS support for WebDriver
  - BlazeMeter Inc. has contributed few Thread Groups and Controllers into Standard Set
  - [Loadosophia Uploader](/wiki/LoadosophiaUploader/) has been renamed to BM.Sense uploader
  - finally [Loadosophia Uploader](/wiki/LoadosophiaUploader/) works well with distributed tests
  - add 2 text fields in PhantomJS WebDriver GUI for phantomjs.cli.args and phantomjs.ghostdriver.args parameters
  - Modernize project website, change its look a bit
  - fix consequences of PR #80 further
  - make [Dummy Sampler](/wiki/DummySampler/) compatible with JMeter 2.12 and earlier
  - add [custom functions](/wiki/Functions/) strReplace and strReplaceRegex (thanks to Dima Lulko for suggesting)
  - add FilterResultsTool command line tool, filter results by label (regex), offset, success, output file XML or CSV format, see <http://jmeter-plugins.org/wiki/FilterResultsTool/>
  - change HttpSimpleTableServer command ADD a new line for a file with GET (new) or POST protocols, see <http://jmeter-plugins.org/wiki/HttpSimpleTableServer/>
  - change HttpSimpleTableServer no cache for Response, add headers cache control, pragma and expires
  - Add fields to FFW: grpThreads, sampleCount, errorCount, responseHeaderSize, responseSize, URL
  - upgrade JSONPath lib to 2.0.0 to not conflict with core JMeter 3.0, this breaks some cases because of the lib regression
  - fix user agent override for Firefox not enabling the field in UI
  - request clean session from IE webdriver
  - upgrade selenium libraries to 2.52.0
  - Added new option to allow delayed connection or reconnection feature to JMXMon

## 1.3.1 <i><font color=gray size="1">October 12, 2015</font></i>
  - add connectTime field into Flexible File Writer
  - add [Connect Times Over Time](/wiki/ConnectTimeOverTime/) listener to Extras set
  - use JMeter's encoding for converting strings to bytes in FFW
  - fix scheduling bug in TST for sub-rps load
  - fixed permission-denied-error on unix systems in LoadosophiaUploader-reporter when no storeDir is set
  - bump up Selenium dependency to 2.47.0
  - add basic PhantomJS (GhostDriver) capability to Remote WebDriver
  - fix UTG broken by PR #80
  - add Connect Time field to dummy sampler

## 1.3.0 <i><font color=gray size="1">June 30, 2015</font></i>
  - Java 7 or higher required
  - JMeter 2.12 or higher required
  - Add SynthesisReport and AggregateReport generation to GraphGeneratorListener and allow generation of both CSV and PNG in one shot
  - add script language selector to WebDriver Sampler
  - WebDriver Sampler UI offers default script text
  - change default of WebDriver sample type to SampleResult
  - Selenium dependency version updated to 2.46.0
  - fix `__substring` function (thanks to Dima Lulko for reporting)
  - improve thread safety of FFW
  - fix packaging, by mistake many sets were containing Standard Set classes inside
  - add **not** switch to JSONPath Assertion (thanks to Dima Lulko for suggesting)
  - make JSONPath Assertion to compare value as regexp
  - fix reported issues with [JSONPathExtractor](/wiki/JSONPathExtractor/), see <https://groups.google.com/forum/#!topic/jmeter-plugins/fDqEpL3iqi0> and <https://groups.google.com/forum/#!topic/jmeter-plugins/UvKa5KiM2qo>
  - SynthesisReportGui: the 90 percentile is now customizable with JMeter 2.13
  - Make GraphRowSimple Serializable so that you can copy/paste graphs in JMeter
  - Update maven-dependency-plugin to 2.8 and maven-shade-plugin to 2.1 as per <https://cwiki.apache.org/confluence/display/MAVEN/AetherClassNotFound>
  - Add in multiple ColorsDispatcher classes (CycleColors, HueRotatePalette and CustomPalette). CycleColors is original implementation from 1.2.1
  - Configurable hostname patterns for perfmon metrics (for cleaner charts)
  - AggregateReportGui: Apply format to save csv file
  - Make sure WebDriver sample results have timestamp on errors 
  - UltimateThreadGroup: Added parametrization via _threads_schedule_ property
  - Add 'description' column to Parameterized Controller
  - deprecated REST Sampler dropped from the codebase
  - Add `if` function
  - Enable getting output logging from Developer Tool <https://sites.google.com/a/chromium.org/chromedriver/logging/performance-log>

## 1.2.1 <i><font color=gray size="1">March 9, 2015</font></i>
  - add [TestPlanCheckTool](/wiki/TestPlanCheckTool/) to check JMX file consistency without running it
  - add requestHeaders field to FFW (and fix the bug with responseHeaders that nobody noticed)
  - add webdriver.sampleresult_class property for WebDriverSampler
  - Added Remote Driver to the WebDriver plugins
  - add iterationNum function
  - add NTLM support for Firefox
  - add ability to load extensions and set preferences for Firefox
  - add Internet Explorer Driver
  - rework internals of [JSON Path Assertion](/wiki/JSONPathAssertion/) and [JSON Path Extractor](/wiki/JSONPathExtractor/), handle arrays and null values
  - add operation on variables into [JSON Path Extractor](/wiki/JSONPathExtractor/)
  - maximize browser window by default in WebDriver
  - add two ways to change color of graph lines and legend.  See [SettingsPanel](/wiki/SettingsPanel/) for doc.

## 1.2.0 <i><font color=gray size="1">September 16, 2014</font></i>
  - fix JMeter startup with Extras (https://groups.google.com/forum/#!topic/jmeter-plugins/YlnXE_WOXXo)
  - Improved JsonAssertion (https://github.com/undera/jmeter-plugins/pull/40)
  - add `__env` function (https://github.com/undera/jmeter-plugins/pull/44)
  - allow multiline quoted values in VariablesFromCSV config (https://github.com/undera/jmeter-plugins/pull/49)
  - Graphs Generator : New field Output Folder for better control on output of CSV or PNG
  - Graphs Generator : Bug fix on Filter field, default Undefined selection of Combo on Graphs Generator addition lead to empty graphs if not reselected by user, now defaults to empty.
  - Included Dev-Mode for WebDriver, which keeps the browser window open between runs.
  - New HTTP Simple Table Server to manage the dataset (https://github.com/undera/jmeter-plugins/pull/54)
  - Graphs plugin : Add filter regular expression and period time (https://github.com/undera/jmeter-plugins/pull/56)
  - add subSampleStart() and subSampleEnd() to sampleResult of WebDriver Sampler
  - Firefox WebDriver allows its User Agent to be overridden.
  - Extract property for FFW buffer size (https://groups.google.com/forum/#!topic/jmeter-plugins/Mldv8vs2L3g)
  - GraphsGeneratorListener: add filter regular expression and period time (https://github.com/undera/jmeter-plugins/pull/57)
  - New Synthesis Report with filter panel
  - add base64Encode and base64Decode [functions](/wiki/Functions/)
  - REST Sampler is now deprecated in favor of HTTP Request or [HTTP Raw Request](/wiki/RawRequest/)
  - New Merge Results tool to simplify the comparison of two or more load tests

## 1.1.3 <i><font color=gray size="1">February 17, 2014</font></i>
  - New Redis Data Set for distributed equivalent of CSV Data Set
  - add --paint-markers option for JMeterPluginsCMD
  - New Graphs Generator Listener to generate Graphs or CSVs after test is finished or by running a fake test
  - Flexible File Writer allows using \t\r\n in header and footer
  - Fix NPE in Flexible File Writer (https://groups.google.com/forum/#!topic/jmeter-plugins/4DWidWMrfVk)
  - Add default value for JSON Path Extractor
  - Supports latest (v.27) Firefox by upgrading to Selenium 2.39.0
  
## 1.1.2 <i><font color=gray size="1">October 27, 2013</font></i>
  - Add **HtmlUnitDriver** for the WebDriver
  - [JMX Monitoring Collector](/wiki/JMXMon/) added
  - Fix JMXMon Samples Collector use one jmx connection for the same URL and close connections when test ended
  - Fix DbMon Samples Collector use one jdbc connection for the same Pool Name and close connections when test ended
  - Fix unnecessary console error message in CMDTool
  - Disable UDP in PerfMon and enable it only via property setting
  - allow variables in PerfMon params helper dialog
  - AutoStop plugin is now printing the reason it stopped the test to the JMeter log file
  - fix Loadosophia Uploader not working without additional libs
  - fix Extras With Libs set work with JMeter 2.10 (commons-lang is now shipped)

## 1.1.1 <i><font color=gray size="1">July 14, 2013</font></i>
  - **Project has moved to new homepage, http://jmeter-plugins.org/**
  - add Loadosophia.org active tests support

## 1.1.0 <i><font color=gray size="1">July 1, 2013</font></i>
  - **add WebDriver sampler**
  - add Loadosophia.org active tests support 
  - add `__chooseRandom` function
  - initial thread burst in stepping thread group ([pull request](https://github.com/undera/jmeter-plugins/pull/1)) 
  - fifo queues capacity limit property introduced
  - add ability to use CMDTool outside lib/ext dir (issue 240)
  - add std dev to aggregate report for CMDtool CSV export (issue 259)
  - more parsing options for VariablesFromCSV <https://github.com/undera/jmeter-plugins/pull/5>
  - add threadsCount value to Flexible File Writer
  - Add PATCH HTTP method to OAuth Sampler (https://github.com/undera/jmeter-plugins/pull/11)


## 1.0.0 <i><font color=gray size="1">February 26, 2013</font></i>
    - **merge [AtlantBH's jmeter-components](https://github.com/ATLANTBH/jmeter-components) into JP@GC, 13 new plugins added**
    - Add option to use either Regular Expressions or Strings for labels in PageDataExtractor 
    - fix charts x legend if cmdline tool used (empty granularity)
    - fix issue 223
    - fix copying issues with JMeter 2.9
    - publish artifacts on maven (http://search.maven.org/#search%7Cga%7C1%7Ckg.apc), issue 218
    - move sources to GitHub (https://github.com/undera/jmeter-plugins / https://github.com/undera/perfmon-agent / https://github.com/undera/cmdrunner )
    - distribute separate archives for plugins, plugin-libs and perfmon
    - remove old and deprecated perfmon parts

## 0.5.6 <i><font color=gray size="1">January 16, 2013</font></i>
  - **Database monitoring** with [DbMon Sample Collector](/wiki/DbMon/). This listener graph data retrieved from databases with sql queries (thanks to Marten Bohlin for the patch). System queries provides db performance metrics. Thanks to  Marten Bohlin for this wonderful feature.
  - Add delta option to page data extractor
  - Minor charts polish (better looking legend)
  - Implement new-style Loadosophia.org upload with color flag, test title and direct report link
  - fix issue 219
  - add `--success-filter` option to cmd tool (issue 214)
  - add "Close UDP Socket" option to UDP sampler (issue 133)
  - add fifoSize function

## 0.5.5 <i><font color=gray size="1">October 19, 2012</font></i>
  - **JMeter 2.8 compatible**
  - some issues fixed (issue 204, issue 205)


## 0.5.4 <i><font color=gray size="1">September 3, 2012</font></i>
  - Enable command line tool for Page Data Extractor
  - Add option to set globally line width and markers in line charts
  - Some issues fixed (issue 103, issue 134, issue 166, issue 177, issue 179)
  - Some bug fixes:
    - Removed space at end of labels in perfmon (if default metric used)
    - Fix percent value returned by agent for CPU per Process (x100)
    - Agent again JRE 1.4 compatible


## 0.5.3 <i><font color=gray size="1">June 13, 2012</font></i>
  - **[Loadosophia.org Uploader](/wiki/LoadosophiaUploader/) tries to send [PerfMon](/wiki/PerfMon/) data too**
  - PerfMon *unit* option to select Bytes, KB, or MB in relevant metrics
  - Some issues fixed (issue 145, issue 154, issue 159, issue 163)
  - Create directories in cmdtool if needed
  - Some graph improvements (better axis labels display for big values)
  - New graph allowing to plot data extracted from page results (status, health pages, etc.)
  - Option to maximize graph panel

## 0.5.2 <i><font color=gray size="1">May 14, 2012</font></i>
  - **[Inter-Thread Communication](/wiki/InterThreadCommunication/): plugins & functions**
  - added JMX metrics for PerfMon (see [forums topic](https://groups.google.com/forum/?fromgroups#!topic/jmeter-plugins/ypRS6Y-GkAo))
  - An option to ignore some samples by sample Label - at Rows tab
  - file header and footer in [Flexible File Writer](/wiki/FlexibleFileWriter/)
  - added a LockFile plugin to prevent simultaneous tests to run on same machine
  - ability to disable Composite Graph auto zoom
  - new perfmon wizard
  - some bugs fixed (issue 102 and others)
  - allow *label* parameter for PerfMon metrics
  - sent JMeter maintainers a patch for undo/redo feature (for their issue [42248](https://issues.apache.org/bugzilla/show_bug.cgi?id=42248))
  - added kg.apc.jmeter.samplers.FileReadChunkSize property to control direct file sending performance
  - Calculate aggregate values only for selected rows - via include/exclude sample labels at Rows tab
  - [Raw Data Source](/wiki/RawDataSource/) can read binary data
  - ability to set line thickness (0 = no line) and markers display in chart settings tab
  - added a property to change CSV export time format (jmeterPlugin.csvTimeFormat)
    
## 0.5.0 <i><font color=gray size="1">Dec 11, 2011</font></i>
  - **New [PerfMon agent](/wiki/PerfMonAgent/)**
    - 76 metrics
    - ability to configure PerfMon to monitor single process name/pid/filesystem/network interface.
    - can use UDP or TCP transport
    - ability to monitor custom values
    - old agent still supported transparently
  - Fix Issue 75, Issue 76, Issue 79, Issue 81, Issue 82, Issue 85, Issue 87, Issue 90, Issue 91, and some others
  - Start option (-!JforcePerfmonFile=true) to automatically save PerfMon metrics in a file named perfmon_yyyyMMdd-HHmmss.csv if no file is specified and test is run in non GUI mode
  - Checkbox "overwrite existing file" in [Flexible File Writer](/wiki/FlexibleFileWriter/)
  - Saving sample_variables in [Flexible File Writer](/wiki/FlexibleFileWriter/)
  - Option to change graph type for [Response Time Distribution](/wiki/RespTimesDistribution/) (bar, line or new **cubic spline** chart)
  - New Variable from CSV Config - Rich gui with preview, correct scope of variables (will work with perfmon, previous was not)
  - The plugins now use a caching mechanism to manipulate charts. This can provide big performance improvements in some cases.


## 0.4.2 <i><font color=gray size="1">Aug 30, 2011</font></i>
  - fixed issue 39, issue 48, issue 49, issue 51, issue 55, issue 64, issue 67, issue 72, issue 74
  - no more need to set up saving active threads - it is done automatically
  - added [Loadosophia.org Uploader](/wiki/LoadosophiaUploader/)
  - additional ramp-up time for steps in [Stepping Thread Group](/wiki/SteppingThreadGroup/) (issue 60)
  - Throughput Over Time graph now deprecated in favor of [Transactions Per Second](/wiki/TransactionsPerSecond/)
  - [Raw Request](/wiki/RawRequest/) sending large files efficiently via special field
  - eliminated bright yellow color from graph palette
  - added [custom functions](/wiki/Functions/): doubleSum, strLen, uppercase, lowercase, MD5, substring, isDefined 
  - introduced data read limit for samplers: `kg.apc.jmeter.samplers.ResultDataLimit` property
  - add latency and sampler data fields to [DummySampler](/wiki/DummySampler/)
  - PerfMon reingeneering - regular listener architecture
  - PerfMon agent option to limit monitoring of a single PID for mem and cpu
  - InfiniteGetTCPClientImpl class for testing video streams (FLV)
  - User interface polish
  - Graph hover info showing hovered points values

## 0.4.1 <i><font color=gray size="1">Apr 24, 2011</font></i>
  - **[Throughput Shaping Timer](/wiki/ThroughputShapingTimer/) - another "killer" feature**
  - [Command-line tool](/wiki/JMeterPluginsCMD/) to save csv and/or image for JTL file.
  - [HTTP Raw Request](/wiki/RawRequest/) Sampler
  - [Raw Data Source](/wiki/RawDataSource/) !PreProcessor
  - [Flexible File Writer](/wiki/FlexibleFileWriter/)
  - [AutoStop](/wiki/AutoStop/) listener - using average response time or error rate as stop criteria
  - [PerfMon](/wiki/PerfMon/): Keep collecting and showing metrics for other servers if one of the servers lost connection
  - [PerfMon](/wiki/PerfMon/): Ability to auto stop the Server Agent at end of the test
  - [UDP Sampler](/wiki/UDPRequest/)
  - [console runtime status logger](/wiki/ConsoleStatusLogger/) listener
  - Added "Bytes Sent" in [Bytes Throughput](/wiki/BytesThroughput/) graph
  - Better display of trimmed charts if maxY is forced in settings
  - added prefix for all plugin menu items
  - default description with link to help page for every plugin
  - sample test plans at wiki pages
  - copy row ability in UltimateThreadGroup and PerfMon GUI
  - proper handling of transaction controllers and samplers which download embedded resources in hit/sec, response codes and latency graphs
  - changed row colors generation to new algorithm, more regular
  - [percentiles graph](/wiki/RespTimePercentiles/) use fractions like 99.9
  - important fix: issue 38

## 0.4.0 <i><font color=gray size="1">Feb 13, 2011</font></i>
  - **Possibility to [compose several graphs to one](/wiki/CompositeGraph/)**
  - [Response Times Distribution](/wiki/RespTimesDistribution/) graph
  - [Response Times Percentiles](/wiki/RespTimePercentiles/) graph (Y-axis: Resp Time & X-axis: 0-100%)
  - [Server Hits per Seconds](/wiki/HitsPerSecond/) graph
  - [Bytes Throughput Over Time](/wiki/BytesThroughput/) graph
  - [Latencies Over Time](/wiki/LatenciesOverTime/) graph
  - [Response Codes per Second](/wiki/ResponseCodesPerSecond/) graph
  - Save perfmon metrics in a file if jmeter is used in non gui mode or in distributed test mode
  - Load perfmon files in Perfmon graphs
  - Export graph data to CSV
  - Setting to hide non representative values for graphs with threads count as X axis
  - Setting to force graph's maximum Y axis value 
  - Setting to aggregate all rows/bars in a graph
  - Preview of graph while changing settings
  - Axis labels on all graphs, with granularity information for time based graphs
  - Better graph scaling, especially when limit number of points is selected
  - Ability to display relative test time in graphs

## 0.3.0 <i><font color=gray size="1">Nov 4, 2010</font></i>
  - **[Performance Monitoring](/wiki/PerfMon/) plugin and server agent**
  - [Transactions per second](/wiki/TransactionsPerSecond/) graph
  - Total Successful/Failed Transactions per second graph
  - Possibility to [configure](/wiki/PluginInstall/) part of the plugin
  - Charts display improved (auto scaling + beautification)
  - Added save as PNG image functionality on all graphs
  - Some bugs fixed (issue #3, graphs display bugs)
  - "Settings" tab, containing multiple options, including grouping values on timeline graphs

## 0.2.1 <i><font color=gray size="1">Aug 9, 2010</font></i>
  - **[Ultimate Thread Group](/wiki/UltimateThreadGroup/), the real killer**
  - **the most wanted [Response Times Over Time](/wiki/ResponseTimesOverTime/) Graph**
  - Transaction Throughput Over Time Graph
  - Auto-update GUI for [Stepping Thread Group](/wiki/SteppingThreadGroup/) (thanks to St �phane Hoblingre for the patch)
  - Copy to Clipboard on all graphs (thanks to St �phane Hoblingre for the patch)
  - Anti-aliasing on all graphs (thanks to St �phane Hoblingre for the patch)

## 0.2.0 <i><font color=gray size="1">Apr 9, 2010</font></i>
  - displayable rows selection in [graph listeners](http://code.google.com/p/jmeter-plugins/w/list?q=label:Graph)
  - new [Dummy Sampler](/wiki/DummySampler/) helps debugging and programming tests
  - **new [Stepping Thread Group](/wiki/SteppingThreadGroup/) with alternative users scheduling**

## 0.1.0 <i><font color=gray size="1">Jan 28, 2010</font></i>
  - Samples vs Active Threads listener split to [Response Times vs Threads](/wiki/ResponseTimesVsThreads/) and [Transaction Throughput vs Threads](/wiki/TransactionThroughputVsThreads/)
  - **new listener [Active Threads Over Time](/wiki/ActiveThreadsOverTime/)**
  - unique [Parameterized Controller](/wiki/ParameterizedController/) added, helping re-use test plan modules
  - DCERPC TCPClient class allowing to test DCE RPC
  - [Variables from CSV](/wiki/VariablesFromCSV/) config item

## 0.0.2 <i><font color=gray size="1">Dec 13, 2009</font></i>
  - added graph mode switch "response times/throughput"
  - improved graph resizing
  - added Y axis limit

## 0.0.1 <i><font color=gray size="1">Jul 5, 2009</font></i>
  - **[Response times vs active threads](/wiki/ResponseTimesVsThreads/) graph implemented**
