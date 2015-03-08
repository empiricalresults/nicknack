---
title: "Formatters"
bg: section2  #defined in _config.yml, can use html color like '#0fbfcf'
color: white   #text color
fa-icon: magic
---

To use a Nick Nack formatter you must set both the [OUTPUT_PROTOCOL](https://pythonhosted.org/mrjob/guides/writing-mrjobs.html#job-protocols)
and the [HADOOP_OUTPUT_FORMAT](https://pythonhosted.org/mrjob/job.html#mrjob.job.MRJob.HADOOP_OUTPUT_FORMAT) fields in your class definition.
The package name for all the Nick Nack formatters is *nicknack*, so prefix the formatter
name with this.  For example, to use the MultipleValueOutputFormat formatter, your class should have:

{% highlight python %}
OUTPUT_PROTOCOL = mrjob.protocol.RawProtocol
HADOOP_OUTPUT_FORMAT = 'nicknack.MultipleValueOutputFormat'
{% endhighlight %}


## MultipleValueOutputFormat


***Use Case**: You want to split your output into directories specified by a key, and not include the key in the output.*

mrjob Output:

{% highlight python %}
yield "filename1", json.dumps({"some values": "other JSON"})
yield "otherfile", json.dumps({"other values": "more JSON"})
{% endhighlight %}

Hadoop Input:

{% highlight text %}
filename1   {"some values": "other JSON"}
otherfile   {"other values": "more JSON"}
{% endhighlight %}


Output in *[outputdir]/filename1/part-00000*:

{% highlight text %}
{"some values": "other JSON"}
{% endhighlight %}

Output in *[outputdir]/otherfile/part-00000*

{% highlight text %}
{"other values": "more JSON"}
{% endhighlight %}

<div class="spacer"></div>

## MultipleTextOutputFormatByKey

***Use Case**: You want to split your output into directories specified by a key, but also keep the keys in the output.*

* *This is nearly the same as MultipleValueOutputFormat, except both the key and value are included in the output.*

mrjob Output:

{% highlight python %}
yield "filename1", json.dumps({"some values": "other JSON"})
yield "otherfile", json.dumps({"other values": "more JSON"})
{% endhighlight %}

Hadoop Input:

{% highlight text %}
filename1\t{"some values": "other JSON"}
otherfile\t{"other values": "more JSON"}
{% endhighlight %}


Output in *[outputdir]/filename1/part-00000*:

{% highlight text %}
filename1\t{"some values": "other JSON"}
{% endhighlight %}


Output in *[outputdir]/otherfile/part-00000*

{% highlight text %}
otherfile\t{"other values": "more JSON"}
{% endhighlight %}

<div class="spacer"></div>

## MultipleSimpleOutputFormat

***Use Case**: You want to split out output into specific directories, where the directory name isn't a part of the output.
Your key should be a tuple of (filename,key) within a space delimited string (we break on the first space to determine the filename, the remainder is the key.*

<div class="alert alert-info">
  <strong>Pro tip:</strong> Your directory names must not contain spaces.  If you must have spaces, use one of MultipleJSONOutputFormat or MultipleCSVOutputFormat.
</div>

mrjob Output:

{% highlight python %}
yield "dirname1 key1", json.dumps({"some values": "other JSON"})
yield "dirname2 key2", json.dumps({"other values": "more JSON"})
{% endhighlight %}

Hadoop Input:

{% highlight text %}
dirname1 key1\t{"some values": "other JSON"}
dirname2 key2\t{"other values": "more JSON"}
{% endhighlight %}

Output in *[outputdir]/dirname1/part-00000*:

{% highlight text %}
key1\t{"some values": "other JSON"}
{% endhighlight %}

Output in *[outputdir]/dirname2/part-00000*

{% highlight text %}
key2\t{"other values": "more JSON"}
{% endhighlight %}

<div class="spacer"></div>

## MultipleJSONOutputFormat

***Use Case**: You want to split out output into specific directories, where the directory name isn't a part of the output.
Your key should be a JSON array containing exactly two elements, 1) the directory name and 2) the key to write in the output.*

* *This formatter adds a tiny bit of overhead as we need to decode the json key for each line processed.*

mrjob Output:

{% highlight python %}
yield json.dumps(["dirname1", "key1"]), json.dumps({"some values": "other JSON"})
yield json.dumps(["dirname2", "key2"]), json.dumps({"other values": "more JSON"})
{% endhighlight %}

Hadoop Input:

{% highlight text %}
["dirname1", "key1"]\t{"some values": "other JSON"}
["dirname2", "key2"]\t{"other values": "more JSON"}
{% endhighlight %}

Output in *[outputdir]/dirname1/part-00000*:

{% highlight text %}
key1\t{"some values": "other JSON"}
{% endhighlight %}

Output in *[outputdir]/dirname2/part-00000*

{% highlight text %}
key2\t{"other values": "more JSON"}
{% endhighlight %}

<div class="spacer"></div>

## MultipleCSVOutputFormat

***Use Case**: You want to split out output into specific directories, where the directory name isn't a part of the output.
Your key should be a CSV row containing exactly two elements, 1) the directory name and 2) the key to write in the output.*

* *This formatter adds a tiny bit of overhead as we need to decode the csv key for each line processed.*

<div class="alert alert-info">
  <strong>Pro tip:</strong> If you're keys are funky (contain quotes, spaces, ..), consider using the MultipleJSONOutputFormat
  to avoid csv escaping issues.
</div>

mrjob Output:

{% highlight python %}
yield "dirname1,key1", json.dumps({"some values": "other JSON"})
yield "dirname2,key2", json.dumps({"other values": "more JSON"})
{% endhighlight %}

Hadoop Input:

{% highlight text %}
dirname1,key1\t{"some values": "other JSON"}
dirname2,key2\t{"other values": "more JSON"}
{% endhighlight %}

Output in *[outputdir]/dirname1/part-00000*:

{% highlight text %}
key1\t{"some values": "other JSON"}
{% endhighlight %}

Output in *[outputdir]/dirname2/part-00000*

{% highlight text %}
key2\t{"other values": "more JSON"}
{% endhighlight %}

<div class="spacer"></div>

## MultipleLeafValueOutputFormat

<div class="alert alert-warning">
  <strong>Warning!</strong> Advanced usage only. You probably want MultipleValueOutputFormat instead.
</div>

***Use Case**:  You want complete control of the output files, including the leaf file (usually part-00XXX).
This means the job **must** output unique keys per process or else you will lose data. For example, you may use this
format if you guarantee each key is unique per reducer.*

mrjob Output:

{% highlight python %}
yield "full/filename", json.dumps({"some values": "other JSON"})
yield "otherpath/filename", json.dumps({"other values": "more JSON"})
{% endhighlight %}

Hadoop Input:

{% highlight text %}
full/filename\t{"some values": "other JSON"}
otherpath/filename\t{"other values": "more JSON"}
{% endhighlight %}

Output in *[outputdir]/full/filename*:

{% highlight text %}
{"some values": "other JSON"}
{% endhighlight %}

Output in *[outputdir]/otherpath/filename*

{% highlight text %}
{"other values": "more JSON"}
{% endhighlight %}