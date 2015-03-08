package nicknack;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * Writes to the directories specified by the first element in a row.
 * The output key of your job must be a comma separated row, fields optionally
 * enclosed by double quotes.  The first element will be used as the
 * subdirectory, and the written row will not include that first element.
 */
public class MultipleCSVOutputFormat extends MultipleTextOutputFormat<Text, Text> {


    private static final CsvPreference STANDARD_SURROUNDING_SPACES_NEED_QUOTES =
            new CsvPreference.Builder('"', ',', "")
                    .surroundingSpacesNeedQuotes(true)
                    .build();


    /**
     * Generate the file output file name based on the given key and the leaf file
     * name. akey is a CSV string, the first element of which is the partial-path.
     * This function reads the CSV, joins the partial-path with the leaf file
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
        CsvListReader reader = new CsvListReader(new StringReader(key.toString()), STANDARD_SURROUNDING_SPACES_NEED_QUOTES);

        List<String> columns;
        try {
            columns = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (columns.size() == 1) {
            // if only 1 column, this isn't a proper csv
            return name;
        }

        String first = columns.get(0);
        return new Path(first, name).toString();
    }

    /**
     * Generate the actual key from the given key/value. akey is a CSV row, the
     * first element of which is the partial-path.
     * This function reads the CSV and removes the parital-path.  Note: optional
     * quotes in the original key may be removed if they are not required.
     *
     * @param key
     *          the key of the output data
     * @param value
     *          the value of the output data
     * @return the actual key derived from the given key/value
     */
    @Override
    protected Text generateActualKey(Text key, Text value) {
        CsvListReader reader = new CsvListReader(new StringReader(key.toString()), STANDARD_SURROUNDING_SPACES_NEED_QUOTES);

        List<String> columns;
        try {
            columns = reader.read();
            if (columns.size() == 1) {
                // don't mess with it if size is 1
                return key;
            }

            StringWriter stringWriter = new StringWriter();
            CsvListWriter writer = new CsvListWriter(stringWriter, STANDARD_SURROUNDING_SPACES_NEED_QUOTES);
            writer.write(columns.subList(1, columns.size()));
            writer.flush();

            String csvData = stringWriter.toString();
            return new Text(csvData);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
