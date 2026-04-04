# HDFS Operations

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-hadoop)</span>

<i>This plugin was originally developed by [Atlantbh d.o.o.](http://www.atlantbh.com/), 
released as [jmeter-components](https://github.com/ATLANTBH/jmeter-components), 
then merged into JP@GC.</i>

## Description

HDFS (Hadoop Distributed File System) is Hadoop’s primary storage system, as users, during testing, need to possibility to copy a file from local directory or a location on the server to the hdfs directory, we have created this sampler.

You need to define the following:
  - Input file destination: local directory of the input file
  - Output directory on HDFS: location on hdfs to which you want to copy the file.

Response shows the name of the file and the location to which it has been copied, if the file already exists it will show the message that the file already exists.

![](/img/wiki/HDFSOperations1.png)