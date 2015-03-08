package com.er.hadoop;

import org.apache.commons.math3.util.Pair;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class BaseOutputTest {

    public static final String LEAF = "part-00000";

    public List<Pair<Object, Object>> testRows;


    public List<Pair<Object, Object>> testRows() {
        List<Pair<Object, Object>> testRows; testRows = new ArrayList<Pair<Object, Object>>();
        testRows.add(new Pair<Object, Object>("myfilename", "somejunkvalue"));
        testRows.add(new Pair<Object, Object>(902342, 2234234));
        testRows.add(new Pair<Object, Object>("file,with,comma", "value,with,comma"));
        testRows.add(new Pair<Object, Object>("file.with.dot", "value.with.dot"));
        testRows.add(new Pair<Object, Object>("file/with/forward/slash", "value/with/forward/slash"));
        testRows.add(new Pair<Object, Object>("filewith,every/thing.2", "valuewith,every/thing.2"));
        return testRows;
    }

    @Before
    public void setup() {
        testRows = testRows();
    }
}
