package com.er.hadoop;


import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.Text;
import org.codehaus.jettison.json.JSONArray;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MultipleSimpleOutputFormatTest extends BaseOutputTest {


    @Override
    public List<Pair<Object, Object>> testRows() {
        List<Pair<Object, Object>> testRows; testRows = new ArrayList<Pair<Object, Object>>();
        testRows.add(new Pair<Object, Object>("filename1 actual-key", "somejunkvalue"));
        testRows.add(new Pair<Object, Object>("filename2 actual-key", "value2"));
        return testRows;
    }

    @Test
    public void testGenFileName() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleSimpleOutputFormat obj = new MultipleSimpleOutputFormat();

            String actual = obj.generateFileNameForKeyValue(testKey, testValue, LEAF);
            String expected = pair.getFirst().toString().split(" ")[0] + "/" + LEAF;
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGenActualKey() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleSimpleOutputFormat obj = new MultipleSimpleOutputFormat();
            Text actual = obj.generateActualKey(testKey, testValue);
            String expected = "actual-key";
            assertEquals(expected, actual.toString());
        }
    }
}
