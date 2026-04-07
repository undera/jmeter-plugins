# Dummy Sampler

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-dummy)</span>


Dummy Sampler is the most obedient of the JMeter samplers:
it generates sample just with the values that was defined for it.
It is extremely convenient when you need to debug
a [BeanShell Post-Processor](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#BeanShell_PostProcessor)
or [RegExp Extractor](http://jakarta.apache.org/jmeter/usermanual/component_reference.html#Regular_Expression_Extractor)
without repeating whole test or waiting for exact condition in application under test.

![](/img/wiki/dummy_sampler.png)

## Add Dummy Subresult Post-Processor

In addition to Sampler component, you can use a post-processor that adds dummy samples as subresults. An obvious use-case
is to have Dummy Sampler with Dummy Subresults to simulate HTTP Request with embedded resources.

## Example

[Download Example Test Plan](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/DummySamplerExample.jmx)
