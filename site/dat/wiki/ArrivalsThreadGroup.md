# Arrivals Thread Group

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-casutg)</span>

## Authorship
<div class="pull-right text-center" style="margin-left: 1em; line-height: 200%;">
[![](https://d3qmoqvtroy6p7.cloudfront.net/logo.png)](http://blazemeter.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki)

feat.

[![](https://d3qmoqvtroy6p7.cloudfront.net/dynatrace.png)](http://www.dynatrace.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki)
</div>
This plugin was inspired by *[Dynatrace LLC](http://www.dynatrace.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki)*, implemented by *[BlazeMeter Inc.](http://blazemeter.com/?utm_source=jmplinnerpages&utm_medium=cpc&utm_content=jmpininnerpgs&utm_campaign=JMeter%2BPlug%2BIn%2BWiki)*, and then contributed to community as Open Source.

## Description

This thread group operates with "arrivals" schedule as a way to express the load. "Arrival" means thread iteration start. It will create new threads if all existing threads are busy in the middle of iterations.

Note that constant arrival rate means increasing concurrency, so be careful with the values you enter. Use "Concurrency Limit" field as safety valve to prevent running out of memory.

![](/img/wiki/ArrivalsThreadGroup.png)