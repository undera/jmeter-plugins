# Stepping Thread Group

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-casutg)</span>

JMeter have only one out-of-the-box option for threads (users) scheduling: simple ramp-up. But many users, especially with [HP LoadRunner](http://en.wikipedia.org/wiki/HP_LoadRunner) experience miss more flexible thread scheduling algorythm. Stepping Thread Group adds to JMeter thread scheduling similar to [LoadRunner](http://en.wikipedia.org/wiki/HP_LoadRunner)'s.

In JMeter version 2.4 new plugin type was introduced: custom Thread Groups. Stepping Thread Group is the first custom thread group in the world.

However, time goes by and probably better alternative has been offered: [Concurrency Thread Group](/wiki/ConcurrencyThreadGroup/)

## Features
  - preview graph showing estimated load (see example screen below)
  - initial thread group delay to combine several thread group activities
  - increase load by portions of threads (users) with ramp-up period
  - configurable hold time after all threads started
  - decrease load by portions

## Example
[Download Example Test Plan](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/SteppingThreadGroupExample.jmx)

Following screenshot shows Stepping Thread Group set to generate load increasing by 10 users each 2 minutes:

![](/img/wiki/stepping_thread_group1.png)


Actual test run shows following activity graph:

![](/img/wiki/active_threads_over_time_stepping.png)

## See Also

  1. [Concurrency Thread Group](/wiki/ConcurrencyThreadGroup/) for possible replacement
  1. [Ultimate Thread Group](/wiki/UltimateThreadGroup/) for freeform load profiles