# JSON/YAML Path Extractor

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-json)</span>

<i>This plugin was originally developed by [Atlantbh d.o.o.](http://www.atlantbh.com/), 
released as [jmeter-components](https://github.com/ATLANTBH/jmeter-components), 
then merged into JP@GC,, and reworked significantly in v.1.2.1</i>

## Description

Allows extracting values from JSON or YAML responses using [JSONPath syntax](http://goessner.net/articles/JsonPath/index.html#e2).
Use `Input Format` radio buttons for switch between JSON/YAML.

It will try to find the JSON path provided and extract corresponding value. If the path is not found, it will use default value. For the `null` value the "null" string will be used.

If result is array, it will additionally set loop variables. This is much like [RegExp Extractor](http://jmeter.apache.org/usermanual/component_reference.html#Regular_Expression_Extractor) or [XPath Extractor](http://jmeter.apache.org/usermanual/component_reference.html#XPath_Extractor) do to support [ForEach Controller](http://jmeter.apache.org/usermanual/component_reference.html#ForEach_Controller) input:
  - varName_1
  - varName_2
  - varName_3
  - ...

![](/img/wiki/JSONPathExtractor.png)

___NOTE: In case when checked YAML Input Format then plugin will convert YAML to JSON.___

## Example

[Download Example Test Plan](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/JSONPathExtractorExample.jmx)