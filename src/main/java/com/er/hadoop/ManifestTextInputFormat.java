package com.er.hadoop;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manifest files are inputs with a list of paths to use as the real input.
 * Paths may be directories, globs, or files and will be expanded appropriately
 * Unlike most InputFormats, this class will silently ignore missing and
 * unmatched paths in the manifest file.
 */
public class ManifestTextInputFormat extends KeyValueTextInputFormat {


    /**
     * Given a manifest Path, return a list of the paths it contains.
     *
     * @param job the job to list input paths for
     * @return array of FileStatus objects
     * @throws IOException if zero items.
     */
    @Override
    protected FileStatus[] listStatus(JobConf job) throws IOException {
        FileStatus[] manifests = super.listStatus(job);
        List<FileStatus> paths = new ArrayList<FileStatus>();
        for(int i = 0; i < manifests.length; i++) {
            List<Path> globPaths = this.readManifest(manifests[i].getPath(), job);
            for (Path globPath : globPaths) {
                paths.addAll(this.expandPath(globPath, job));
            }
        }
        return paths.toArray(new FileStatus[1]);
    }

    private List<Path> readManifest(Path manifestPath, JobConf job) throws IOException {
        FileSystem fs = manifestPath.getFileSystem(job);
        List<Path> paths = new ArrayList<Path>();
        DataInputStream dataStream = fs.open(manifestPath);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(dataStream));
            String line = reader.readLine();
            while(line != null) {
                paths.add(new Path(line));
                line = reader.readLine();
            }
        } finally {
            dataStream.close();
        }

        return paths;
    }

    /**
     * Takes the nominal job input paths as manifest files and returns all the FileStatus
     * objects within those files.
     *
     * @param globPath
     * @param conf
     * @return
     * @throws IOException
     */
    private List<FileStatus> expandPath(Path globPath, JobConf conf) throws IOException {
        FileSystem fs = globPath.getFileSystem(conf);
        FileStatus[] matches = fs.globStatus(globPath);

        List<FileStatus> paths = new ArrayList<FileStatus>();
        if (matches != null) {
            for (FileStatus match : matches) {
                if (match.isDirectory()) {
                    FileStatus[] childStatuses = fs.listStatus(match.getPath());
                    Collections.addAll(paths, childStatuses);
                } else {
                    paths.add(match);
                }
            }
        }

        return paths;
    }
}
