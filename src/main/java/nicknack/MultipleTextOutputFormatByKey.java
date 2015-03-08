package nicknack;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

/**
 * Writes to the directories specified by the key.
 * The key of your job output will be used as the file path.  Both the key and
 * the value will be written to the resulting tab delimited text files.
 */
public class MultipleTextOutputFormatByKey extends MultipleTextOutputFormat<Text, Text> {

    /**
     * Generate the file output file name based on the given key and the leaf file
     * name.
     * This function joins the partial-path with the leaf file (typically part-0000x).
     * Developer is responsible for ensuring the resulting path is valid.
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
}
