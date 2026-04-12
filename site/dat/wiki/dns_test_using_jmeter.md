# Measuring DNS Server Performance with JMeter
Author: Andrey Pohilko

Technology tags: JMeter, PowerDNS, Ubuntu, VirtualBox, DNSPerf, UDP, DNS, Linux

## Preface
For a long time people around the world have been using [JMeter](http://jakarta.apache.org/jmeter/) mostly as 
[HTTP](http://en.wikipedia.org/wiki/HTTP)
performance testing tool. There is a lot of articles on doing such
tests, for beginners and advanced users. And only few notes may be
found on making non-mainstream performance testing with JMeter. 

Nevertheless, JMeter developers have chosen so powerful
architecture for the tool, that I bless them almost everyday because
of pleasure extending JMeter. As experienced JMeter plugins developer
I see infinite testing universe where JMeter is applicable with
custom functionality plugins or just witty workarounds.


One of such rarely used JMeter applications is testing servers
that use [UDP protocol](http://en.wikipedia.org/wiki/User_Datagram_Protocol) for communication. UDP protocol itself provide
lightweight network transport which is expected to work at
very high request rates, maybe tens of thousands requests per second.
Sounds a little bit high for JMeter, and this is the challenge that I
accept. 

The most common UDP application we see in our everyday life is 
[DNS](http://en.wikipedia.org/wiki/Domain_Name_System)
requests. There is DNS server somewhere inside your [ISP](http://en.wikipedia.org/wiki/Internet_Service_Provider)
that serve all your requests for [IP addresses](http://en.wikipedia.org/wiki/IP_address)
 of Twitter, Facebook, Google. Thousands of Internet
users send their queries to ISP's DNS server and it sends recursive
queries to external servers. There's something to stress-test for
sure!

Last word for preface: I did this tests inside [Yandex](http://en.wikipedia.org/wiki/Yandex)
company, so I'm unable to tell you too much of [NDA](http://en.wikipedia.org/wiki/Non-disclosure_agreement)
covered real case. But I'll say enough to feel the power of JMeter.
Also this article use a lot of anti-patterns in load environment,
such as virtual box on the same PC as load generator. I understand
it, but the aim of the article is to show the path of JMeter advanced
usage, not give accurate measurement numbers.

==DNS Load Profile & KPIs==
Time to pay some attention to data domain of our case. How you
do feel the performance quality of DNS service, from the point of DNS
user and DNS owner?

As a user I feel 2 main [KPIs](http://en.wikipedia.org/wiki/Key_performance_indicator):
response time and fail rate. UDP protocol offers non-guaranteed
delivery, so request or response may be lost somewhere on the network.
Client application will wait inside its timeout, possibly will retry,
and, finally will report persistent error to the user. The faster
client receive responses the happier user is. The lower request
loss rate the happier user is. 

To measure response time and fail rate properly, system should be
tested under constant load for some time period. My admin says that
production request rate is approximately 1 000 requests per second
(RPS), with peaks up to 2 000 RPS. I will take this as my primary
testing mode.

As DNS server owner (or admin) I care
if my server resources is nearly exhausted, and I can't serve the
crowd that will come after next ISP's advertisement campaign. I want to
know the capacity limit of my current server and nearest bottleneck
resource. For this sake, I will monitor server's [CPU](http://en.wikipedia.org/wiki/Central_processing_unit)
and other server resources. Also I will test my server with
infinitely increasing load, to find its capacity limit.

## Setting Up Servers

In the real case I used to test [PowerDNS](http://www.powerdns.com/)
instance set up for caching with disabled recursive queries. For
demonstration purposes I deployed [VirtualBox](http://www.virtualbox.org/)
with [Ubuntu-mini 11.04](https://help.ubuntu.com/community/Installation/MinimalCD) at my laptop (so I know that performance should be poor,
but enough for demonstration). 

While real-case DNS server was configured and filled with [cache data](http://en.wikipedia.org/wiki/Cache) *by my admin*, at my virtual box I have to install and
configure *pdns-recursor* myself, nothing unexpected here. I
have no data to fill cache, so I'll have to run my queries once, the
server will cache them and I'll measure cached performance in further
test runs.

The hardware in real case was rack-mount server with 16-core CPU
and tens of gigabytes of [RAM](http://en.wikipedia.org/wiki/Random-access_memory)
(or something like this, actually I didn't asked my admin what
hardware I have). Virtual demo, of course, have its poor virtual
hardware CPU, hard drive and 512MB or RAM. Real case load generator
hardware was similar to DNS server's.

Network setup in real case is also not known for sure, something
like Ethernet to several switches in office building [LAN](http://en.wikipedia.org/wiki/Local_area_network),
thick channel to [Data Center](http://en.wikipedia.org/wiki/Data_center), 
some routers inside DC and, finally, my server's [NIC](http://en.wikipedia.org/wiki/Network_interface_controller).
## Setting Up the Test

My preferred way to create [Test Plans](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#Test_Plan) in JMeter is iterative, so I'll describe it that
way.
### Iteration 1: Proof of Concept

First of all, I need to verify that JMeter can properly simulate DNS
requests at all.

I opened JMeter, added simple [Thread Group](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#Thread_Group) to it, left it with 1 thread and 1 iteration. Also I need
debug print to see if DNS requests works fine, so I add [View Results Tree](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#View_Results_Tree) in *Test Plan* root.

Now I need a *UDP Sampler*. Searching in Google and Yandex
displayed that none is available in public access. Ok, I wrote my own
(already ;-)) and will show how to use it. I added [UDP Request](http://jmeter-plugins.org/wiki/UDPRequest) as a child of *Thread Group*.

Now I need example DNS request data to send via JMeter. I'll just
use [Wireshark](http://www.wireshark.org/) network sniffer
to grab one DNS request. Wireshark have "capture filter" feature,
and I configured it as "port 53" to see only DNS requests. Then I
use [nslookup](http://en.wikipedia.org/wiki/Nslookup)
utility to make a request to my ISP's server. Wireshark caught the
request and response.

[Wireshark captured data](/img/wiki/dns/screenshot1.png)

I right-click *"Domain Name System (query)"* in bottom
panel and choose *"Copy ⇒ Bytes (Hex Stream)"*, then paste
it into *Request Data* field of *UDP Request*.

Last detail: configure *Hostname* and *UDP port* in
*UDP Request*, setting it to my virtual PowerDNS instance.

[UDP Request HEX request settings](/img/wiki/dns/screenshot2.png)

Hitting *Ctrl+R* (Run Test)  in JMeter gives me the first
result in *View Results Tree*. The result is *500* with
*SocketTimeoutException*, WTF? Calm down, it's OK, because I
have 1000 ms timeout in Sampler and the server haven't cached my
request yet. I just repeat test run and see my successful result.

Comparing *[HEX](http://en.wikipedia.org/wiki/Hexadecimal)-encoded*
data from *Response data* tab with sample response data in
Wireshark prove that request goes OK.

Time to make something serious and scalable, single static request
is not real case.
### Iteration 2: DNSJava Decoder and CSV Data Source

My admin gave me 3.5MB file that contains 150 000 unique DNS
queries like *"A www.apc.kg."*. *UDP Request* can
consume that data with [DNSJava](http://www.dnsjava.org/)
encode/decode class.

I set up proper *Encode/Decode class* (don't forget to place
the library into lib/ext) and changed request into _"apc.kg. A
IN"._

[UDP Request using DNSJava decoder](/img/wiki/dns/screenshot3.png)

Test runs OK, *Response Data* tab
shows that DNSJava decodes response bytes back into human-readable
text info.

The next step is reading request data from admin's large file. The
file has [CSV](http://en.wikipedia.org/wiki/Comma-separated_values)-compatible
format, so I can use robust [CSV Data Set Config](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#CSV_Data_Set_Config) element in JMeter. I configured *CSV Data Set*
to read record type into variable *recType*, and host name into
*hostName.*

[Reading data from CSV file](/img/wiki/dns/screenshot4.png)

Note that separator field not empty, it contains single space
character. Also note [EOF](http://en.wikipedia.org/wiki/End-of-file)
flag settings, I'll use and change them later.
Now I use those variables in Sampler's request data field:
*${hostName} ${recType} IN*. One more test run to verify the
data feed to Sampler. Oh, I'm tired of first failed non-cached
request, so I increase sampler timeout to 3 seconds. 

Let's increase iteration count in *Thread Group*, say, to 50,
and see how it works with loops. *View Results Tree* now full
with successful results, and it is bad, because *View Results Tree* listener
have too poor performance and will slow down our high-rate test. I
configure it to catch only errors to see only the problems that may
occur in test.

Let's also check multi-thread work: increase thread count in
*Thread Group* to 10 and run the test. No errors in _View
Results Tree_, and no successful results data or response time
statistics. Time to add our test some visualization.
### Iteration 3: Visualize Test Results

First of all, I should see basic summary that comes with [Aggregate Report](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#Aggregate_Report). Added it and ran test – all 500 requests are present in
successful. Let's increase iteration count in *Thread Group* to
500 to have test that lasts for several seconds, I need a results
stream to visualize.

Next useful Listener is [Transactions per Second](http://jmeter-plugins.org/wiki/TransactionsPerSecond), since transaction rate is the main KPI for DNS server
and our whole test. Another intuitively required listener is [Response Times Over Time](http://jmeter-plugins.org/wiki/ResponseTimesOverTime).

Important point: average response times is not enough to monitor.
If you want to evaluate quality of the system, you should operate with
[Response Times Distribution](http://jmeter-plugins.org/wiki/RespTimesDistribution) and [Percentiles](http://jmeter-plugins.org/wiki/RespTimePercentiles).

To summarize all graphs, I add [Composite Graph](http://jmeter-plugins.org/wiki/CompositeGraph), run test once and add *Transactions Per Second* and
*Response Times Over Time* to composite view.
You may say that it is also interesting to see how many threads
were generating load to server. I'll tell why it is not applicable
for DNS testing: DNS service is of "[open workload](http://www.google.com/search?q=%22open%20workload%22)" type. But OK, let's add [Active Threads Over Time](http://jmeter-plugins.org/wiki/ActiveThreadsOverTime) to test tree and *Composite Graph*, just
to play with it.

Now I'll fill server's cache by querying it full requests set from
admin's data file. For that sake I set loop count to *Infinite*
in *Thread Group*. _CSV Data Set _settings will stop the
test when end of file reached. *Composite Graph* shows how it
goes:

[Composite Graph view](/img/wiki/dns/screenshot5.png)
### Iteration 4: Server Performance Monitoring

The only remaining thing is server resources monitoring, I want to
see what becomes bottleneck in my tests.

[JP@GC plugins set](http://jmeter-plugins.org/) have [Servers Performance Monitoring](http://jmeter-plugins.org/wiki/PerfMon) graph with server agent in version 0.4.1, but I'll use not
yet released 0.4.2 *PerfMon Metrics Collector* graph, because I want to be at the cutting edge of JMeter plugins.

I uploaded *serverAgent* part to virtual box and started
*startAgent.sh*, also added  *PerfMon Metrics Collector* to
*Test Plan*. Then started local agent to monitor state of my load
generator. I repeat test run and add perfmon rows to _composite
graph_.

Playing with several perfmon metrics I see that only CPU makes
sense in my tests. I will monitor it primarily.

[PerfMon Metrics Collector View](/img/wiki/dns/screenshot6.png)
### Iteration 5: Throughput Shaper & AutoStop

DNS performance is all about how much requests system can serve
simultaneously, so I need to manipulate request rate. JMeter unable
to do this by default, it has only indirect manipulation with Timers
but, fairly, it is totally inconvenient. I have [Throughput Shaping Timer](http://jmeter-plugins.org/wiki/ThroughputShapingTimer) for this purpose and add it as a child of my
sampler. Hitting *"Add Row"* button gives me some
default request rate profile, then I run test just to check how it goes.

*Composite Graph* shows that it goes as expected.

[Increasing load Composite View](/img/wiki/dns/screenshot7.png)

Also I want JMeter to stop test automatically when the server
obviously inadequate and failing too much requests. For this sake I
add [AutoStop Listener](http://jmeter-plugins.org/wiki/AutoStop) to *Test Plan* and configure it to stop when error
rate is higher than 50% for 10 seconds in a row.
==Running the Tests==

Before running actual tests I need to change *CSV Data Set*
settings to *recycle on EOF*, to have "infinite" data set.
Done this.
### Performance Test

My first task is to measure service KPIs at constant load and
answer question "How the server performs under usual or increased
constant load?"

I configure *Throughput Shaping Timer* to [climb](http://en.wikipedia.org/wiki/Climb)
up to 1 000 RPS in one minute, then hold 1 000 RPS for 10 minutes.
Gradual climb prevents server from strange transition effects.

[Throughput Shaper settings](/img/wiki/dns/screenshot8.png)

After running the test I'm looking at *Composite Graph*. My notes:
  - Load was generated as scheduled
  - Response times was mainly near 1-2 milliseconds – pretty good
  - Server's CPU was approximately 20% busy
  - Local CPU was approximately 60% busy (it's all because VirtualBox, I'm sure)
  - There was no failures

[1000 RPS Composite View](/img/wiki/dns/screenshot9.png)

It seems we can serve 1 000 RPS easily. Let's check how it will
serve double load 2 000 RPS. I change the rates in shaper to work
with 2 000 RPS. Notes looking at the graph:

  - Load generated OK
  - Response times still mainly 1-2 milliseconds (verified with	*Percentiles* graph)
  - Server CPU 20-25%, seems no difference with 1 000 RPS
  - Local CPU 80%, seems VirtualBox spend too much of it for network traffic processing
  - Still no failures, it's OK

[2000 RPS Composite View](/img/wiki/dns/screenshot10.png)

In real case, with good rack-mount load generator and server I had
even better results, with response times 99% below 1ms.
### Stress Test

Second task is to check server's capacity limit and see how it
will die in agony.

I reconfigure shaper to provide constantly increasing load up to
10 000 RPS in 10 minutes. 

[Throughput Shaper ramp-up setting](/img/wiki/dns/screenshot11.png)

Also I think I'll have not enough threads to generate 10K RPS. I
increase threads to 100, this will be enough  for response times up
to 10 milliseconds. Running the test! 

*Oops!* Test shows that JMeter failed to generate request
rate that we configured. Investigating perfmon I found that bottleneck is my local CPU.
Seems that placing virtual test server at the same machine as load
generator was very bad decision. Alas, I don't have ability to place
my virtual box somewhere else now, but I think it is still enough for
our demonstration.

[Failed Climb](/img/wiki/dns/screenshot12.png)

In real case, when I had rack-mount load generator for JMeter runs
I was able to produce 20 000-25 000 requests per second and still
PowerDNS server was serving perfectly. So I failed to shoot it down!
:)
## What about DNSPerf?

Actually there was more action behind this test in real case.
First tool that we took for DNS performance tests was [DNSPerf](http://www.google.com/search?q=dnsperf).
But server monitoring showed that request rate is pretty unstable
with DNSPerf. Then we wrote small shell script that collects [ifconfig](http://en.wikipedia.org/wiki/Ifconfig)
RX/TX packets statistics from the load generator NIC. It revealed that
DNSPerf generates load poorly. NIC monitoring of JMeter tests showed
that JMeter generates load as its graphs show. Here's small
screenshot from Yandex internal task tracker, comparison of the tools
at 12 000 RPS:
 
[DNSPerf vs JMeter](/img/wiki/dns/screenshot13.png)

Well, I'm sorry to say it, but DNSPerf sucks...
## Conclusion

JMeter with custom plugins set becomes powerful test and reporting
tool, load generation profiles can be easily configured in GUI and it shapes as
predicted for performance and stress testing.

JMeter is able to generate at least 20 000 DNS requests per
second, more than enough for regular DNS  server instance.
PowerDNS can serve up to 4 000 DNS requests per second even in poor
infrastructure (virtual box placed at the same PC with load
generator). In good hardware insfrastructure DNSPerf serve perfectly
more than 20 000 RPS and its limit is still not known.

Using JMeter for DNS (and all other UDP) performance testing gives
us nice opportunity to have single tool for different technologies.
No need to adapt another tool into existing process, all habitual
features like reporting, data sources and test logic are at their
usual places.
## Links

  - [Discuss this article](https://groups.google.com/forum/#!topic/jmeter-plugins/QfI0AcCeFF8)
  - [Download Final Test Plan](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/DNS_Test_Example.jmx)
