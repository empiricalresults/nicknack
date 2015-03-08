package com.er.hadoop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;


public class MultipleSimpleOutputFormat extends MultipleTextOutputFormat<Text, Text> {

    /**
     * Generate the file output file name based on the given key and the leaf file
     * name. akey is a JSON string of two elements:
     * [partial-path, actual-key]
     * This function parses the JSON, joins the partial-path with the leaf file
     * (typically part-0000x).  Developer is responsible for ensuring the resulting
     * path is valid.
     *
     * @param key
     *          the key of the output data
     * @param name
     *          the leaf file name
     * @return generated file name
     */
    @Override
    protected String generateFileNameForKeyValue(Text key, Text value, String name) {
        return new Path(key.toString().split(" ")[0], name).toString();
    }


    /**
     * Generate the actual key from the given key/value. akey is a JSON string of
     * two elements:
     * [partial-path, actual-key]
     * This function parses the JSON then sets and returns the actual key.
     *
     * @param key
     *          the key of the output data
     * @param value
     *          the value of the output data
     * @return the actual key derived from the given key/value
     */
    @Override
    protected Text generateActualKey(Text key, Text value) {
        return new Text(key.toString().split(" ")[1]);
    }

}
