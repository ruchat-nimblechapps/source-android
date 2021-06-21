package com.xdesign.android.mystrica.util;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * @author keithkirk
 */
public class FileUtils {

    public static final String DATA_FILE_EXTENSION = ".csv";
    public static final String GRAPH_FILE_EXTENSION = ".png";

    public static void deleteFile(File data) {
        final File graphFile = getGraphFileFromData(data);
        graphFile.delete();
        data.delete();
    }

    public static File renameFile(File data, String name) {
        final String directoryPath = data.getParentFile().getAbsolutePath();

        final String dataName;
        final String graphName;

        if (!name.endsWith(DATA_FILE_EXTENSION)) {
            dataName = name + DATA_FILE_EXTENSION;
            graphName = name + GRAPH_FILE_EXTENSION;
        } else {
            dataName = name;
            graphName = name.replace(DATA_FILE_EXTENSION, GRAPH_FILE_EXTENSION);
        }

        final File renamedData = new File(directoryPath, dataName);
        final File renamedGraph = new File(directoryPath, graphName);

        final File graph = getGraphFileFromData(data);

        final boolean dataSuccess = data.renameTo(renamedData);
        final boolean graphSuccess = graph.renameTo(renamedGraph);

        return dataSuccess ? renamedData : data;
    }

    public static String getFileNameWithoutExtension(File file) {
        if (file.getName().endsWith(DATA_FILE_EXTENSION)) {
            return file.getName().substring(0, file.getName().length() - DATA_FILE_EXTENSION.length());
        }

        return file.getName();
    }

    public static ArrayList<Uri> getUrisForFile(File data) {
        final ArrayList<Uri> uris = new ArrayList<>();

        uris.add(Uri.fromFile(data));
        uris.add(Uri.fromFile(getGraphFileFromData(data)));

        return uris;
    }

    private static File getGraphFileFromData(File data) {
        final String graphName = data.getName().replace(DATA_FILE_EXTENSION, GRAPH_FILE_EXTENSION);
        final String directoryPath = data.getParentFile().getAbsolutePath();

        return new File(directoryPath, graphName);
    }

    private FileUtils() {
    }
}
