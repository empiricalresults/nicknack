package com.er.hadoop;

import org.apache.commons.math3.util.Pair;
import org.apache.hadoop.io.Text;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class MultipleTextOutputFormatByKeyTest  extends BaseOutputTest {


    @Test
    public void testGenFileName() {
        for (Pair<Object,Object> pair : this.testRows) {
            Text testKey = new Text(pair.getFirst().toString());
            Text testValue = new Text(pair.getSecond().toString());
            MultipleTextOutputFormatByKey obj = new MultipleTextOutputFormatByKey();
            String actual = obj.generateFileNameForKeyValue(testKey, testValue, LEAF);
            assertEquals(pair.getFirst() + "/" + LEAF, actual);
        }
    }


}
