# Concurrency Thread Group

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-casutg)</span>

## Authorship
<span class="pull-right" style="margin-left: 1em">
[![](https://d3qmoqvtroy6p7.cloudfront.net/logo.png)](http://blazemeter.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki)
</span>

This plugin was implemented by *[BlazeMeter Inc.](http://blazemeter.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki)* and then contributed to community as Open Source.

## Description

This thread group offers simplified approach for configuring threads schedule. It is intended to maintain the level of concurrency, which means starting additional during the runtime threads if there's not enough of them running in parallel. Unlike standard Thread Group, it won't create all the threads upfront, so extra memory won't be used. It's a good repacement for [Stepping Thread Group](/wiki/SteppingThreadGroup/), since it allows threads to finish their job gracefully.

The preview graph reacts immediately to changes in the fields, showing you the planned concurrency schedule.

![](/img/wiki/ConcurrencyThreadGroup.png)

## Use With Throughput Shaping Timer Feedback Function

When this thread group is used with [Throughput Shaping Timer](/wiki/ThroughputShapingTimer/), you may replace Target Concurrency value with a call to the __tstFeedback function to dynamically maintain thread count required to achieve target RPS. When using this approach, leave Concurrency Thread Group Ramp Up Time and Ramp-Up Steps Count fields blank, but be sure to set Hold Target Rate Time to a value equal or greater than the total Duration specified in the Throughput Shaping Timer schedule. See [ThroughputShapingTimer#Schedule-Feedback-Function Throughput Shaping Timer Schedule Feedback Function]for function usage details.


## Configurable properties

=== dynamic_tg.properties_caching_validity:

For performance reasons, the component caches its properties value for "dynamic_tg.properties_caching_validity" which defaults to 20 milliseconds.

If you know you'll never be changing dynamically those values, you can set this property to a negative value.
If you need to refresh it at a higher rate, decrease its value but be aware of potential performance impacts. 

=== dynamic_tg.temporisation:

This property is used to add sleep after all threads stopped.
Defaults to 10 millis.

=== dynamic_tg.shift_rampup_start:

This property is used to avoid CPU spikes on rampup start when no Dynamic threads have been created yet.
Defaults to 0 millis.
