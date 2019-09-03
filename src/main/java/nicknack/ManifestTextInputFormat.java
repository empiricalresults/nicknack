package nicknack;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manifest files are inputs with a list of paths to use as the real input.
 *
 * Paths may be directories, globs, or files and will be expanded appropriately.
 * Unlike most InputFormats, this class will silently ignore missing and
 * unmatched paths in the manifest file.
 */
public class ManifestTextInputFormat extends KeyValueTextInputFormat {
    private static final Log log = LogFactory.getLog(ManifestTextInputFormat.class);

    /**
     * Takes the nominal job input paths as manifest files and returns all the FileStatus
     * objects within those files.
     * @param job
     *          the JobConf object for this job
     * @return array of FileStatus objects
     */
    @Override
    protected FileStatus[] listStatus(JobConf job) throws IOException {
        FileStatus[] manifests = super.listStatus(job);
        ArrayList<FileStatus> allFileStatuses = new ArrayList<FileStatus>();
        for (FileStatus manifest : manifests) {
            allFileStatuses.addAll(expandManifest(manifest.getPath(), job));
            if (allFileStatuses.size() % 10007 == 0) {
                log.info("Processed " + allFileStatuses.size() + " input paths so far.");
            }
        }
        log.info("Total input paths from manifest : " + allFileStatuses.size());
        return allFileStatuses.toArray(new FileStatus[0]);
    }

    /**
     * Given a manifest Path, return a list of the paths it contains.
     * @param manifest
     *          the Path representing the manifest file to read
     * @param job
     *          the JobConf object for this job
     * @return an ArrayList of Path objects, one for each line in the given manifest file
     */
    private ArrayList<FileStatus> expandManifest(Path manifest, JobConf job) throws IOException {
        FileSystem fs = manifest.getFileSystem(job);
        FSDataInputStream stream = fs.open(manifest);
        BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
        ArrayList<FileStatus> fileStatuses = new ArrayList<FileStatus>();

        String line = buf.readLine();
        while (line != null) {
            Path p = new Path(line);
            fileStatuses.addAll(expandPath(p, job));
            line = buf.readLine();
        }
        return fileStatuses;
    }

    /**
     * Expand a path to FileStatuses of:
     *  - the contents if a directory
     *  - matches if a glob
     *  - just the given path, if a file.
     * @param path
     *          the Path representing one line of the manifest file
     * @param job
     *          the JobConf object for this job
     * @return an ArrayList of FileStatus objects, expanded from the line from the manifest
     */
    private ArrayList<FileStatus> expandPath(Path path, JobConf job) throws IOException {
        FileSystem fs = path.getFileSystem(job);
        FileStatus[] matches = fs.globStatus(path);
        if (matches == null) {
            return new ArrayList<FileStatus>();
        }
        ArrayList<FileStatus> expanded = new ArrayList<FileStatus>();
        for (FileStatus match : matches) {
            if (match.isDir()) {
                expanded.addAll(Arrays.asList(fs.listStatus(match.getPath())));
            } else {
                expanded.add(match);
            }
        }
        return expanded;
    }

}
