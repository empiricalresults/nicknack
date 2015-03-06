package com.er.hadoop;

import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class MultipleLeafValueOutputFormatTest extends BaseOutputTest {

    @Test
    public void testGenFileName() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleLeafValueOutputFormat obj = new MultipleLeafValueOutputFormat();

            String actual = obj.generateFileNameForKeyValue(testKey, testValue, LEAF);
            String expected = pair.getFirst().toString();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGenActualKey() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleValueOutputFormat obj = new MultipleValueOutputFormat();
            Text actual = obj.generateActualKey(testKey, testValue);
            assertNull(actual);
        }
    }
}
