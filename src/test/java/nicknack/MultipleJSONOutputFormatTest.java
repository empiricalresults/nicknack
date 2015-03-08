package nicknack;


import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.Text;
import org.codehaus.jettison.json.JSONArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultipleJSONOutputFormatTest extends BaseOutputTest {


    private Text makeJsonKey(String input) {
        return new Text(new JSONArray().put(input).put("actual-key").toString());
    }

    @Test
    public void testGenFileName() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = makeJsonKey(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleJSONOutputFormat obj = new MultipleJSONOutputFormat();

            String actual = obj.generateFileNameForKeyValue(testKey, testValue, LEAF);
            String expected = pair.getFirst().toString() + "/" + LEAF;
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGenActualKey() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = makeJsonKey(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleJSONOutputFormat obj = new MultipleJSONOutputFormat();
            Text actual = obj.generateActualKey(testKey, testValue);
            String expected = "actual-key";
            assertEquals(expected, actual.toString());
        }
    }
}
