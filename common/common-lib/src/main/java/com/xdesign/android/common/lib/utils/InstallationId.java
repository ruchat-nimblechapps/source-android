package com.xdesign.android.common.lib.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

/**
 * Utility class which takes care of generating a unique id for an installation
 * of the app. The id is persisted on disk, and returned the next time when
 * requested instead of being generated again.
 */
public final class InstallationId {

    private static final String TAG = InstallationId.class.getSimpleName();
    private static final String FILE = "installation";

    public static String get(File directory) throws IOException {
        final File file = new File(directory, FILE);

        final String id;
        if (!file.exists()) {
            id = UUID.randomUUID().toString();

            write(file, id);
        } else {
            id = read(file);
        }

        return id;
    }

    private static String read(File file) throws IOException {
        BufferedReader bufferedReader = null;
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException exception) {
                    Log.w(TAG, exception.getMessage(), exception);
                }
            }
        }

        return stringBuilder.toString();
    }

    private static void write(File file, String id) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            bufferedWriter.write(id);

            bufferedWriter.flush();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException exception) {
                    Log.w(TAG, exception.getMessage(), exception);
                }
            }
        }
    }

    private InstallationId() {}
}
