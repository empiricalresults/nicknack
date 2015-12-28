package nicknack;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class ManifestTextInputFormatTest {
    public static String[] FILES = {"file-a.txt", "file-b.txt"};
    
    public File testManifest;
    public Configuration conf;
    public JobConf job;

    private String stripPath(FileStatus status) {
	String pathStr = status.getPath().toString();
	return pathStr.substring(pathStr.lastIndexOf('/') + 1);
    }
    
    @Before
    public void setup() {
	URL url = this.getClass().getResource("/manifest.txt");
	testManifest = new File(url.getFile());

	conf = new Configuration();
	conf.set("mapred.input.dir", testManifest.toString());

	job = new JobConf(conf);
    }

    @Test
    public void testManifestFile() throws IOException {
	ManifestTextInputFormat inputFormat = new ManifestTextInputFormat();
	FileStatus[] fileStatuses = inputFormat.listStatus(job);
	ArrayList<String> actual = new ArrayList<String>();
	for (FileStatus status : fileStatuses) {
	    actual.add(stripPath(status));
	}
	assertArrayEquals(FILES, actual.toArray(new String[0]));
    }
}
