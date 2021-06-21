package com.xdesign.android.mystrica.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.xdesign.android.common.lib.utils.ExceptionLogger;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.interfaces.FileManagerListener;
import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.models.LogEntry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author keithkirk
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FileManager implements FileManagerListener {

    private static final String HEADER_TIME = "Time";
    private static final String HEADER_TRANSMITTANCE = "Transmittance";
    private static final String HEADER_ABSORBANCE = "Absorbance";
    private static final String HEADER_COLOUR = "Colour";
    private static final String HEADER_NOTE = "Note";
    private static final String HEADER_EVENT_TYPE = "Event Type";

    private static final String DIRECTORY_NAME = "mystrica_sessions";
    private static final String DIRECTORY_PATH = "/" + Environment.DIRECTORY_DOCUMENTS + "/" + DIRECTORY_NAME;
    private static final String DEFAULT_FILENAME_FORMAT = "dd_MM_yyyy";
    private static final String FILE_NAME_SEPARATOR = "_";
    private static final String FILE_SEPARATOR = ",";
    private static final String SEPARATE_EVENT = "Separate Event";
    private static final String TIMED_EVENT = "Timed Event";

    private final Set<WeakReference<FileManagerListener>> listeners;

    private final Log log;
    private final Context context;

    private boolean writeEnabled;

    public FileManager(Context context, Log log) {
        this.log = log;
        this.context = context;

        this.listeners = new HashSet<>();
    }

    public void setWriteEnabled(boolean writeEnabled) {
        this.writeEnabled = writeEnabled;
    }

    public boolean isWriteEnabled() {
        return writeEnabled;
    }

    public void save() {
        if (writeEnabled) {
            final File directory = getDirectory();
            final String filename = generateFilename();

            final List<Pair<Boolean, Integer>> states = saveFile(DIRECTORY_PATH, filename);

            final File file = new File(directory.getAbsolutePath(), filename + FileUtils.DATA_FILE_EXTENSION);

            try {

                file.createNewFile();

                final Writer writer = new BufferedWriter(new FileWriter(file));
                writer.write(serialiseLog());
                writer.close();

                log.setSaved(true);
                states.add(new Pair<>(true, R.string.myst_label_csv));

            } catch (IOException e) {
                ExceptionLogger.log(e);
                states.add(new Pair<>(false, R.string.myst_label_csv));
            }

            final StringBuilder builder = new StringBuilder();
            boolean allSaved = true;
            for (Pair<Boolean, Integer> state : states) {
                final Resources res = context.getResources();

                allSaved = allSaved && state.first;

                if (builder.length() > 0) {
                    builder.append("\n");
                }

                builder.append(res.getString(state.first
                        ? R.string.myst_format_save_success
                        : R.string.myst_format_save_failure,
                            res.getString(state.second)));
            }

            if (allSaved) {
                Toast.makeText(context, R.string.myst_toast_file_saved, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, builder.toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, R.string.myst_toast_file_save_permission_fail, Toast.LENGTH_LONG).show();
        }
    }

    public List<File> getSessions() {
        final File directory = getDirectory();

        final File[] files = directory.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        final List<File> fileList = new ArrayList<>();

        for (File file : files) {
            if (file.getName().endsWith(FileUtils.DATA_FILE_EXTENSION)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    public void loadSession(File file) {
        readSession(file);
    }

    public boolean canDataBeWritten() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean canDataBeRead() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void addListener(FileManagerListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public List<Pair<Boolean, Integer>> saveFile(String directoryPath, String name) {
        final List<Pair<Boolean, Integer>> states = new LinkedList<>();

        for (WeakReference<FileManagerListener> listener : listeners) {
            if (listener.get() != null) {
                states.addAll(listener.get().saveFile(directoryPath, name));
            }
        }

        return states;
    }

    private void readSession(File file) {
        final List<String> lines = new LinkedList();

        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            reader.close();

            Toast.makeText(context, R.string.myst_toast_file_loaded, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, R.string.myst_toast_file_load_fail, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        updateLog(lines);
    }

    private String generateFilename() {
        final StringBuilder builder = new StringBuilder();
        final SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FILENAME_FORMAT);

        builder.append(format.format(new Date()));

        int currentCount = 0;

        if (getDirectory().list() != null) {
            for (String name : getDirectory().list()) {
                if (name.contains(builder.toString())) {
                    final int count = getNameCount(builder.toString(), name);

                    if (count >= currentCount) {
                        currentCount = count + 1;
                    }
                }
            }
        }

        builder.append(FILE_NAME_SEPARATOR).append(currentCount);

        return builder.toString();
    }

    private File getDirectory() {
        final File file = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), DIRECTORY_NAME);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    private int getNameCount(String dateString, String filename) {
        final String substring = filename.substring(
                dateString.length() + FILE_NAME_SEPARATOR.length(),
                filename.length() - FileUtils.DATA_FILE_EXTENSION.length());
        return Integer.parseInt(substring);
    }

    private String serialiseLog() {
        final StringBuilder builder = new StringBuilder();

        builder.append(buildHeaderRow());

        for (LogEntry entry : log.getEntries()) {
            builder
                    .append("\n")
                    .append(serialiseEntry(entry));
        }

        return builder.toString();
    }

    private String buildHeaderRow() {
        final StringBuilder builder = new StringBuilder();

        builder
                .append(HEADER_TIME)
                .append(FILE_SEPARATOR)
                .append(HEADER_TRANSMITTANCE)
                .append(FILE_SEPARATOR)
                .append(HEADER_ABSORBANCE)
                .append(FILE_SEPARATOR)
                .append(HEADER_COLOUR)
                .append(FILE_SEPARATOR)
                .append(HEADER_NOTE)
                .append(FILE_SEPARATOR)
                .append(HEADER_EVENT_TYPE);

        return builder.toString();
    }

    private String serialiseEntry(LogEntry entry) {
        final StringBuilder builder = new StringBuilder();

        builder
                .append(DisplayUtils.getDisplayableTime(context, entry.getSeconds()))
                .append(FILE_SEPARATOR)
                .append(DisplayUtils.getPercentage(context, entry.getTransmittance()))
                .append(FILE_SEPARATOR)
                .append(DisplayUtils.getRounded(context, entry.getAbsorbance()))
                .append(FILE_SEPARATOR)
                .append(entry.getColour().toString())
                .append(FILE_SEPARATOR)
                .append(entry.getNote().replace(FILE_SEPARATOR, ","))
                .append(FILE_SEPARATOR)
                .append(entry.isSingleCapture() ? SEPARATE_EVENT : TIMED_EVENT);

        return builder.toString();
    }

    private void updateLog(List<String> lines) {
        final List<LogEntry> entries = new LinkedList<>();

        for (String string : lines) {
            if (!HEADER_TIME.equals(
                    string.substring(0, Math.min(string.length(), HEADER_TIME.length())))) {
                entries.add(parseEntry(string));
            }
        }

        log.clear();
        log.add(entries);
        log.setSaved(true);
    }

    private LogEntry parseEntry(String string) {
        final String[] components = string.split(FILE_SEPARATOR);

        final long seconds = Utils.parseTime(components[0]);
        final double transmittance
                = Double.parseDouble(components[1].substring(0, components[1].length() - 1));
        final double absorbance = Double.parseDouble(components[2]);
        final Colour colour = Colour.forName(components[3]);
        final String note = components[4];
        final boolean isSingleCapture = SEPARATE_EVENT.equals(components[5]);

        return new LogEntry(seconds, transmittance, absorbance, note, colour, isSingleCapture);
    }
}
