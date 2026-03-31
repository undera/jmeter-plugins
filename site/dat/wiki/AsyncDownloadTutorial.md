# Asynchronous Download Tutorial for JMeter

## Preface

This tutorial is the solution for the following request on JMeter mailing list:
<http://jmeter.512774.n5.nabble.com/Asynchronous-HTTP-Request-Sampler-td5718271.html>

The user has asked how can he do an asynchronous download in JMeter, making main thread still iterating while lengthy operation is in progress. The solution that use [Inter-Thread Communication features](/wiki/InterThreadCommunication/) from JMeter-Plugins.org is described below. It will take MySQL Workbench website as a sample service.

Prerequisites: JMeter 2.8 or newer with the latest Standard Set from [JMeter-Plugins.org](http://JMeter-Plugins.org).

The whole idea is to have 2 separate Thread Groups: first for main threads, iterating over site pages and second as asynchronous download workers pool. The solution will use single FIFO queue from [Inter-Thread Communication](/wiki/InterThreadCommunication/) plugins to pass download URLs for asynchronous download.

This tutorial will also be interesting for those who wants to see the typical way to program dynamic scenario in JMeter, parsing responses and passing parsed values for firther requests. See the "Adding Variety" section below.


## Step 1: Creating Thread Groups

Let's open fresh JMeter instance and add first Thread Group. Rename it to "Main TG", set up this thread group to have 1 thread and do 10 loops. Select *Start Next Thread Loop on error* option.

Add second Thread Group and rename it to "Async Workers". Configure threads count for it to 10. That's important, because worker threads will serve long download requests while first thread group will be iterating again and again on short page requests. Set iteration count to 1.

![](/img/wiki/async_tutorial/undera-d399.png)

## Step 2: Creating Main TG Scenario

### Simple Requests
Let's create a scenario that will start from <http://dev.mysql.com/downloads/>, will find a download link on the page and will pass it for download.

First of all, add *HTTP Request Defaults* element to save some time on configuring requests. Put the element on test plan root and set Web Server Name to "dev.mysql.com".

Next, add the *HTTP Request* as a child of Main TG and rename it to "Downloads". Set *Path* field to "/downloads/". This sampler will just add some "real" work for our test.

Then we'll simulate navigating via "MySQL Workbench" link, adding one more *HTTP Request* to "Main TG". Rename it to "MySQL Workbench" and set *Path* field to "/downloads/tools/workbench/".

Next step is simulating the choice of OS family from combo-box. Using Chromium "Developer Tools" or "Firebug" plugin in Firefox we may find that a POST request is made to the same "/downloads/tools/workbench/" URL with OS id as a param. Let's add a *HTTP Request* and name it "Chosen OS" with desired URL in *Path* field. "Developer Tools" show that 2 POST fields are present: `current_os=3` and `version=6.0`. Let's try adding just `current_os=3` to POST parameters in the sampler and check if it is enough.

Add a *View Results Tree* listener as a child of Main TG and run the test. You should see successful samples appearing in *View Results Tree*, assuring the Main TG does its work. Check one of the "Chosen OS" samples and look into "Response Data" tab in "HTML" mode. You should see the page with Windows downloads.


### Adding Variety

Now let's introduce some variety into our test. Each request will use different OS family for download. To do this, let's return to previous MySQL website's page and find which "OS id" used for different OS families on that site. Searching through the page's HTML source shows this block:

```
<label for="current_os">Select Platform:</label><br />
<select name="current_os" id="current_os" class="select stacked" onchange="this.form.submit()" >
<option label="Select Platform&hellip;" value="0">Select Platform&hellip;</option>
<option label="Microsoft Windows" value="3" selected="selected">Microsoft Windows</option>
<option label="Ubuntu Linux" value="22">Ubuntu Linux</option>
<option label="Fedora" value="20">Fedora</option>
<option label="Oracle &amp; Red Hat Linux 6" value="31">Oracle &amp; Red Hat Linux 6</option>
<option label="Mac OS X" value="5">Mac OS X</option>
<option label="Source Code" value="src">Source Code</option>
</select>
```

That's it, now we'll use *Regular Expression Extractor*'s feature to fetch random regexp match from previous request. Let's add *Regular Expression Extractor* as a child of "MySQL Workbench" sampler. Using "RegExp Tester" feature of *View Results Tree* we have found the value for Regular Expression field: `<option &#91;^>&#93;+ value="(&#91;1-9&#93;&#91;0-9&#93;*)">`

<img src="/img/wiki/async_tutorial/undera-0e30.png" width="700"/>

Other fields for *Regular Expression Extractor* are filled with:
  - *Reference Name* set to "OS_ID" - this is our variable name
  - *Template* set to `$1$` - that's the reference to our "value catch group" in braces
  - *Match No.* set to 0 - that's the random choice feature
  - *Default Value* set to "FAILED" - I usually set this to detect possible unexpected issues with extractor during test run

![](/img/wiki/async_tutorial/undera-0a4f.png)

Now go to "Chosen OS" sampler and replace constant `current_os` value with {{{ ${OS_ID} }}} variable reference. Next, run the test and make sure that different "Chosen OS" samples in *View Results Tree* have different OS Families inside.

BTW, rename the *Regular Expression Extractor* to "Extract OS_ID", this will make your test plan more obvious.

![](/img/wiki/async_tutorial/undera-514d.png)

### Reaching the Download URL

Now we have a "total variety" situation: we have dynamic "Chosen OS" page and should make next request in scenario only to appropriate OS family links. That's easier than it sound. Just look into "Chosen OS" page HTML and you'll find that the next page is "/downloads/mirror.php?id=414361", varying only the "id" parameter. We'll use the same trick with *Regular Expression Extractor* random choice. Add another *Regular Expression Extractor* as a child of
"Chosen OS" sampler, setting:
  - *Reference Name* set to "MIRROR"
  - *Regular Expression* set to `href="(/downloads/mirror.php&#91;^"&#93;+)"`
  - *Template* set to `$1$`
  - *Match No.* set to 0
  - *Default Value* set to "FAILED"

Again, rename it to "Extract MIRROR".

Now add next *HTTP Request*, rename it to "No, Thanks" and set {{{ ${MIRROR} }}} variable reference in *Path* field. Then add another *Regular Expression Extractor* and name it "Extract DOWNLOAD". Set reference name to "DOWNLOAD" and regexp to ` <a href="(&#91;^"&#93;+)">No thanks ` . You already know how to fill all other fields.

That's the life of JMeter user: do a request, extract some value, do the next request with extracted value. Quite boring, but the resulting test plan with its flexibility worth some patience. So tighten the belt, we already have our download URL in {{{ ${DOWNLOAD} }}} variable!

### Putting the URL into FIFO

That's the easiest part: right-click the "No, Thanks" sampler and add *Inter-Thread Communication PostProcessor* as a child. Set *FIFO Queue Name* to "URLS" and set {{{ ${DOWNLOAD} }}} in value field.

![](/img/wiki/async_tutorial/undera-03c2.png)

## Step 3: Creating Async Worker Scenario

Now let's create a consumer on another side of the pipe. We already have the "Async Workers" thread group, just add new *HTTP Request* inside it. Name it "Do Download" and set {{{ ${URL} }}} variable reference in its *Path* field.

Then add an *Inter-Thread Communication PreProcessor* as a child of *HTTP Request*. Set  queue name to "URLS" and variable name to "URL". Set timeout to some huge value, like thousand or million seconds.

Finally, add a *View Results Tree* listener as a child of Async Workers.

![](/img/wiki/async_tutorial/undera-fe26.png)

## Running the Test

**Caution!** Oracle Inc. will not be happy if you will send them requests as fast as you can. They will at least ban you. To prevent this, add some pauses between your requests in Main TG. To do this, add a *Gaussian Random Timer* as a child of Main TG. And better increase both deviation and constant fields of timer, at least to the value of 1000.

With all the precautions and a popcorn bucket let's run the test. All the action from the Main TG should be visible as a successful samples in its *View Results Tree* listener. Async Worker's results will be in *View Results Tree*, too, depending on the speed of your Internet connection.

Here's what happens inside JMeter engine:
  1. Main TG starts its single thread and Async Workers starts ten threads
  1. All Async Workers threads try to execute the "Do Download" sampler and call PreProcessor to prepare the sampler. All threads are freezed, waiting when a value in FIFO will appear.
  1. Main TG thread do its first loop, reaching "No, Thanks" sampler, calls RegExp Extractor to get download URL, then puts extracted value into FIFO
  1. One of Async Workers threads gets unlocked with URL value from FIFO, and starts download request.
  1. Main TG thread starts second loop, everything repeats, one more Async Workers thread is unlocked with every new URL value in FIFO
  1. ... again and again ...
  1. We end up with Main TG done its work, and Async Workers with all 10 threads downloading different MySQL Workbench packages

Beware that FIFO queue will wait a thousand or million seconds before timeout. So any error samples in Main TG may lead to missing URL to wake a thread in Async Workers. To overcome this, you may increase iteration count for Main TG, so more URLs will be passed via FIFO, ensuring all Async Workers threads will receive one to download.

## Download Resulting Test Plan

Remember that you'll need a Standard Set of plugins installed to open and run this tutorial: [AsyncDownloadTutorial.jmx](/editor/?utm_source=jpgc&utm_medium=openurl&utm_campaign=examples#/img/examples/AsyncDownloadTutorial.jmx)