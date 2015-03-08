package nicknack;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;


/**
 * Writes to the directories specified by the key, and only writes the value.
 * The key of your job output will be used as the file path.  Only the value will
 * be written to the resulting files.
 */
public class MultipleValueOutputFormat extends MultipleTextOutputFormat<Text, Text> {

    /**
     * Generate the file output file name based on the given key and the leaf file
     * name. akey is a string which is the partial-path.
     * This function parses the Key, joins the partial-path with the leaf file
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
        return new Path(key.toString(), name).toString();
    }

    /**
     * Generate the actual key from the given key/value. key is a Key row, the
     * first element of which is the partial-path.
     * This function parses the Key and removes the parital-path.
     *
     * @param key
     *          the key of the output data
     * @param value
     *          the value of the output data
     * @return the actual key derived from the given key/value
     */
    @Override
    protected Text generateActualKey(Text key, Text value) {
        return null;
    }
}
