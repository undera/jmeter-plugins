# Set Variables Action

<span class=''>[<i class='fa fa-download'></i> Download](/?search=jpgc-prmctl)</span>

Let's create empty test plan and add first Thread Group to it.
Add a a *Set Variables Actions* sampler and [Debug Sampler](http://jmeter.apache.org/usermanual/component_reference.html#Debug_Sampler) to Thread Group.

Add a [Simple Controller](http://jmeter.apache.org/usermanual/component_reference.html#Simple_Controller) to Thread Group,
then add a *Set Variables Actions* sampler and [Debug Sampler](http://jmeter.apache.org/usermanual/component_reference.html#Debug_Sampler) to Simple Controller.
Let's rename it to "Step 1" and copy this controller.

Go to first *Set Variables Actions* sampler and add 2 variables with values,
e.g. "step=none" and "var=value". Go to the second *Set Variables Actions* sampler
and add 2 variables with values, e.g. "step=1" and "var1=value".
We defined 2 parameters, and our module will be called with their different values.

Add [View Results Tree](http://jmeter.apache.org/usermanual/component_reference.html#View_Results_Tree)
listener to test plan to visualize results. Now your test plan should look like this:

![](/img/wiki/setVariablesAction.png)

Run the test. Go to the View Results Tree and investigate Response data tabs for both samples.
You'll see that step and var1 had different values after each *Set Variables Action*.

[Download Example Test Plan](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/SetVariablesAction.jmx)
