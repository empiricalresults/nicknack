package nicknack;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

/**
 * Writes to the directories specified by the first element in the key.
 * The output key of your job must be a JSON formatted array.  The first element
 * will be used as the subdirectory, and the second element will be used for key
 * written to the file.
 */
public class MultipleJSONOutputFormat extends MultipleTextOutputFormat<Text, Text> {

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
        JSONArray array;
        try {
            array = new JSONArray(key.toString());
            return new Path(array.getString(0), name).toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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
        JSONArray array;
        try {
            array = new JSONArray(key.toString());
            return new Text(array.getString(1));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
