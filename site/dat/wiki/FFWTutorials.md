# Writing Custom Logs in JMeter

## Native JMeter Logging Capabilities

Frequent task for JMeter users is to set up some custom logging 
([example user case](http://jmeter.512774.n5.nabble.com/Create-custom-log-files-td5067691.html), 
[another case](http://jmeter.512774.n5.nabble.com/Custom-Reporting-Print-variables-to-file-td4738621.html),
[one more case](http://jmeter.512774.n5.nabble.com/Store-extracted-data-in-a-file-td4757868.html)).
Out-of-the-box JMeter offers the way to have some custom data logged into file: 
[sample variables](http://jakarta.apache.org/jmeter/usermanual/listeners.html#sample_variables). 
After setting *sample variables* for your JMeter you'll have those variables saved into
your JTL files together with the other test results data. The major drawbacks of that 
approach is that you can't change sample variables list without reconfiguring and 
restarting the application and can't change log format easily.

Another option is using [BeanShell Listener](http://jmeter.apache.org/usermanual/component_reference.html#BeanShell_Listener), 
which requires Java programming skills and have huge problems with concurrent file writing.

[Flexible File Writer](/wiki/FlexibleFileWriter/) was created to fight that drawbacks and
offers free format logging, works correct and efficient for thousands of parallel 
threads. There is two approaches suggested for writing custom data into files with FFW.

## "Fake Sampler" Approach
Please, download [tutorial Test Plan](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/FFWTutorial_FakeSampler.jmx)
and open it in your JMeter. In this test plan we will query http://google.com/ web page, extract some data 
from the response and store that data into tab separated text file.

Test Plan Overview:
  1. Root Test Plan Element has no changes from default
  1. Thread group has simple setup to start 10 threads immediately, each thread does 10 iterations.
  1. First sampler named *Real HTTP Request* does actual request
  1. *Real HTTP Request* has two Regular Expression Extractors
    1. first parses response body for kEXPI parameter (I don't know what is that, it doesn't matter for this tutorial)
    1. second parses response headers for some *S* parameter in Set-Cookie header
  1. An instance of [Dummy Sampler](/wiki/DummySampler/) named *Fake Sampler For Logging* generates samples to be saved into custom log
  1. Flexible File Writer attached as a child of Fake Sampler writes custom log
  1. View Results Tree used to see debug output and illustrate fake samples impact

The idea is quite simple: *Dummy Sampler* generates JMeter samples with any content you like. 
You may specify extracted variables, function calls or text constants in 
Request/Response Data Fields and then Flexible File Writer will get those samples 
and write them into file in specified format. FFW attached as a child of Fake Sampler 
to see only *custom* samples.

Let's run this test plan. JMeter will create *customLog.txt* file inside the directory
it was started (well, here's some confusion may occur since JMeter may use different
working directories in different platforms). It is suggested to change FFW writing 
path to absolute before running the test. If you open this file after test you
should see 100 lines like this:

```
33492   M1s-TYnItNn_Fsht
33492   lb0clO9ZAPMhOZUJ
33492   Oz3W-Fnw2SUwdC8G
33492   3igY9emoPfbUOj6o
33492   LfEbbvaEK5EgRHTb
33492   cDrmi1q1MlEV6261
33492   8qnK0fTfP-WF8cZ9
33492   LEPK1YIUyLq3DzZ5
33492   NiIaFiRb2C1mWtQc
33492   CdEv4swRRA1xpnGZ
33492   7W69OWsx_Z18QMSv
33492   EeG41j3QjDRjunrL
...
```

The advantage of this approach is its great flexibility, Dummy Samplers offers 
freeform data composing, and FFW offers again flexible writing format.

The drawback of this approach is fake samplers' results, they may affect reports
and graphs with their response time value and counts (View Results Tree has those fake samples). 
To mitigate the problem you
may use [scoping rules](http://jmeter.apache.org/usermanual/test_plan.html#scoping_rules)
and not expose fake samplers to reporting listeners by the scope.

## Sample Variables Approach

As a second approach you may use *sample variables* set up for JMeter, the only
advantage you get is flexible file writing format in FFW. 
See [FlexibleFileWriter#Saving_JMeter_Variables_with_Flexible_File_Writer FFW Help] 
for more details on using sample variables in file format specification.