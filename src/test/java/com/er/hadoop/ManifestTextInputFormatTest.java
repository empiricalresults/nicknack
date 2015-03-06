package com.er.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapred.JobConf;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;


public class ManifestTextInputFormatTest {


    @Test
    public void testListStatus() throws Exception {

        Configuration conf = new Configuration();
        // legacy
        //conf.set("mapred.input.dir", "manifest.txt");

        conf.set("mapreduce.input.fileinputformat.inputdir", getClass().getClassLoader().getResource("manifest.txt").getFile());

        JobConf jobConf = new JobConf(conf);

        ManifestTextInputFormat input = new ManifestTextInputFormat();
        FileStatus[] fileStatuses = input.listStatus(jobConf);

        Set<String> expected = new HashSet<String>();
        expected.add("file-a.txt");
        expected.add("file-b.txt");

        Set<String> actual = new HashSet<String>();
        for (FileStatus fs : fileStatuses) {
            String name = new File(fs.getPath().toUri()).getName();
            actual.add(name);
        }

        assertEquals(expected, actual);
    }
}
