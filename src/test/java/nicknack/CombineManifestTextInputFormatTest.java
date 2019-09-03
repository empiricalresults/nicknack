package nicknack;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapreduce.JobContext;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CombineManifestTextInputFormatTest {
    private static String[] FILES = {"file-a.txt", "file-b.txt"};
    private JobContext jobContext;

    private String stripPath(FileStatus status) {
        String pathStr = status.getPath().toString();
        return pathStr.substring(pathStr.lastIndexOf('/') + 1);
    }

    @Before
    public void setup() {
        URL url = this.getClass().getResource("/manifest.txt");

        File testManifest = new File(url.getFile());
        Configuration conf = new Configuration();
        conf.set("mapreduce.input.fileinputformat.inputdir", testManifest.toString());

        jobContext = mock(JobContext.class);
        when(jobContext.getConfiguration()).thenReturn(conf);
    }

    @Test
    public void testManifestFile() throws IOException {
        CombineManifestTextInputFormat inputFormat = new CombineManifestTextInputFormat();
        List<FileStatus> fileStatuses = inputFormat.listStatus(jobContext);
        ArrayList<String> actual = new ArrayList<String>();
        for (FileStatus status : fileStatuses) {
            actual.add(stripPath(status));
        }
        assertArrayEquals(FILES, actual.toArray(new String[0]));
    }
}
