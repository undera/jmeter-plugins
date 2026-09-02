# AutoStop

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-autostop)</span>

AutoStop used when you want to stop test on some runtime criteria.
Currently 6 criteria available: average response time, average latency, error rate, response time percentile, relative window percentile degradation and error count.

This criteria are used in OR logic, the component will ask JMeter to stop test
if one of the criteria has been met.
After 5 tries of "shutdown test" component will switch to more insistent "stop test",
after 5 more it will try to "stop NOW".

On initiating "shutdown test" AutoStop will create environment variable "auto_stopped" = "true" 
which can be checked later in order to take additional actions on test failure (e.g. send alert). 

![](/img/wiki/AutoStop1.png)

## AutoStop on Response Time/Latency

There is combo-box switching which result parameter to use in conditions: response time or latency.
Test will be stopped only if specified response time/latency exceeded for *sequentially* N seconds.
To disable auto-stop on time criteria, just set time value to zero.

## AutoStop on Error Rate

Error rate specified in percent. Rate can be float number.
Test will be stopped only if specified error rate exceeded for *sequentially* N seconds.
To disable auto-stop on rate criteria, just set error rate to zero.

## AutoStop on Percentile Response Time

Percentile rank and threshold specified in milliseconds.
Test will be stopped only if specified percentile response time exceeded for *sequentially* N seconds.

## AutoStop on Relative Window Percentile Degradation

Compares a response time percentile across two adjacent tumbling windows.
Test will be stopped if the current window's percentile grew by more than the specified percentage compared to the previous window.

## AutoStop on Error Count

Error count limit within a fixed time window (tumbling window, not sustained).
Test will be stopped if the specified error count is exceeded *within* N seconds.

## Examples
[Example AutoStop on Response Time JMX](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/AutoStopExample_Time.jmx)

[Example AutoStop on Error Rate JMX](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/AutoStopExample_ErrRate.jmx)

[Example AutoStop on Percentile JMX](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/AutoStopExample_Percentile.jmx)

[Example AutoStop on Error Count JMX](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/AutoStopExample_ErrCount.jmx)

