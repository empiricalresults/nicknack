package com.er.hadoop;


import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultipleCSVOutputFormatTest extends BaseOutputTest {

    private String firstStringInList(String csvInput) {
        return csvInput.split(",")[0];
    }

    private String everythingButFirst(String csvInput) {
        String[] tokens = csvInput.split(",");
        if (tokens.length == 1) {
            return tokens[0];
        }

        String combined = "";
        for (int i = 1; i < tokens.length; i++) {
            combined += tokens[i];
            combined += ",";
        }

        return combined.substring(0, combined.length() -1);
    }

    @Test
    public void testGenFileName() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleCSVOutputFormat obj = new MultipleCSVOutputFormat();
            String actual = obj.generateFileNameForKeyValue(testKey, testValue, LEAF);

            String expected = LEAF;
            if (pair.getFirst().toString().contains(",")) {
                expected = firstStringInList(pair.getFirst().toString()) + "/" + LEAF;
            }

            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGenActualKey() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleCSVOutputFormat obj = new MultipleCSVOutputFormat();
            Text actual = obj.generateActualKey(testKey, testValue);
            if (pair.getFirst().toString().contains(",")) {
                String expected = everythingButFirst(pair.getFirst().toString());
                assertEquals(expected, actual.toString());
            } else {
                assertEquals(pair.getFirst().toString(), actual.toString());
            }

        }
    }
}
