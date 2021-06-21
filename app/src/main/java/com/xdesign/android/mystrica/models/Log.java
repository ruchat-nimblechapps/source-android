package com.xdesign.android.mystrica.models;

import android.os.Handler;
import android.text.TextUtils;

import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.interfaces.LogListener;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author keithkirk
 */
public class Log {

    private final Set<WeakReference<LogListener>> listeners;
    private final List<LogEntry> entries;
    private final Set<Long> entryTimes;
    private final Handler uiHandler;

    private boolean saved;
    private boolean validForNewX;
    private ReadingType currentReadingType;
    private int maxDecimalPlaces;

    public Log() {
        uiHandler = new Handler();
        listeners = new HashSet<>();
        entries = new LinkedList<>();
        entryTimes = new HashSet<>();

        saved = false;
        validForNewX = true;
        this.currentReadingType = ReadingType.T;
        maxDecimalPlaces = 0;
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public boolean isSaved() {
        return saved || entries.isEmpty();
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isValidForNewX() {
        return validForNewX;
    }

    public ReadingType getCurrentReadingType() {
        return currentReadingType;
    }

    public void setCurrentReadingType(ReadingType currentReadingType) {
        this.currentReadingType = currentReadingType;

        onReadingTypeChanged(currentReadingType);
    }

    public int getMaxDecimalPlaces() {
        return maxDecimalPlaces;
    }

    public void add(LogEntry entry) {
        if (allowEntryAddition(entry)) {
            if (validForNewX) {
                if (!entryValidForNewX(entry)) {
                    validForNewX = false;
                    maxDecimalPlaces = 0;
                } else {
                    reviewMaxDecimalPlaces(entry);
                }
            }

            entries.add(entry);
            saved = false;
            onNewEntry(entry);
        }
    }

    public void add(List<LogEntry> entries) {
        final List<LogEntry> validEntries = new LinkedList<>();

        for (LogEntry entry : entries) {
            if (allowEntryAddition(entry)) {
                if (validForNewX) {
                    if (!entryValidForNewX(entry)) {
                        validForNewX = false;
                        maxDecimalPlaces = 0;
                    } else {
                        reviewMaxDecimalPlaces(entry);
                    }
                }
                this.entries.add(entry);
                validEntries.add(entry);
            }
        }

        if (validEntries.size() > 0) {
            saved = false;
            onNewEntries(validEntries);
        }
    }

    public void clear() {
        entryTimes.clear();
        entries.clear();
        validForNewX = true;
        saved = false;
        maxDecimalPlaces = 0;
        onLogsCleared();
    }

    public void addListener(LogListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    private boolean allowEntryAddition(LogEntry entry) {
        if (entryTimes.contains(entry.getSeconds())) {
            return false;
        } else {
            entryTimes.add(entry.getSeconds());
            return true;
        }
    }

    public void onNewEntry(final LogEntry entry) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<LogListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onNewEntry(entry);
                    }
                }
            }
        });
    }

    public void onLogsCleared() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<LogListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onLogsCleared();
                    }
                }
            }
        });
    }

    public void onNewEntries(final List<LogEntry> entries) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<LogListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onNewEntries(entries);
                    }
                }
            }
        });
    }

    public void onReadingTypeChanged(final ReadingType type) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<LogListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onReadingTypeChanged(type);
                    }
                }
            }
        });
    }

    private void reviewMaxDecimalPlaces(LogEntry entry) {
        final double value = Double.parseDouble(entry.getNote());
        final String string = String.valueOf(value);
        maxDecimalPlaces = Math.max(
                maxDecimalPlaces,
                string.endsWith(".0")
                        ? 0
                        : string.length() - (string.indexOf(".") + 1));
    }


    private static boolean entryValidForNewX(LogEntry entry) {
        if (!entry.isSingleCapture()) {
            return false;
        }
        if (TextUtils.isEmpty(entry.getNote())) {
            return false;
        }
        try {
            Double.parseDouble(entry.getNote());
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
