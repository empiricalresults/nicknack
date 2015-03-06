package com.er.hadoop;

import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MultipleValueOutputFormatTest extends BaseOutputTest {


    @Test
    public void testGenFileName() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleValueOutputFormat obj = new MultipleValueOutputFormat();
            String actual = obj.generateFileNameForKeyValue(testKey, testValue, LEAF);
            assertEquals(pair.getFirst() + "/" + LEAF, actual);
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
