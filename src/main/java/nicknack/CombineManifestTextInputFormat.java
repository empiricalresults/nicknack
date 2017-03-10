package nicknack;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.JobContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert manifest file inputs into FileStatus objects and combines splits based on maxSplitSize.
 */
public class CombineManifestTextInputFormat extends CombineTextInputFormat {

    /**
     * @return List of FileStatus objects from manifest file.
     */
    @Override
    protected List<FileStatus> listStatus(JobContext job) throws IOException {
        ManifestTextInputFormat manifestInput = new ManifestTextInputFormat();
        return Arrays.asList(manifestInput.listStatus(new JobConf(job.getConfiguration())));
    }
}
