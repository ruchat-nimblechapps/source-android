package com.xdesign.android.mystrica.util;

import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.models.LogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author keithkirk
 */
public class GraphUtils {

    public static final int TOTAL_LABELS = 5;
    public static final int DECIMAL_PLACES = 3;
    public static final int INVALID_INDEX = -1;
    public static final int RESULT_DATA_SET_INDEX = 0;

    private static Comparator<LogEntry> xComparator = new Comparator<LogEntry>() {
        @Override
        public int compare(LogEntry lhs, LogEntry rhs) {
            double left = Double.parseDouble(lhs.getNote());
            double right = Double.parseDouble(rhs.getNote());

            if (left < right) {
                return -1;
            }
            if (left > right) {
                return 1;
            }

            return 0;
        }
    };

    public static List<LogEntry> entriesFromLog(Log log) {
        final List<LogEntry> entries = new ArrayList<>(log.getEntries());

        if (log.isValidForNewX()) {
            Collections.sort(entries, xComparator);
        }

        return entries;
    }

    public static float getYVal(LogEntry entry, Log log) {
        switch (log.getCurrentReadingType()) {
            case T:
                return (float) entry.getTransmittance();
            case A:
                return (float) entry.getAbsorbance();
            default:
                return 0.0f;
        }
    }

    public static double getXVal(LogEntry entry, Log log) {
        if (log.isValidForNewX()) {
            return Double.parseDouble(entry.getNote());
        } else {
            return (double) entry.getSeconds();
        }
    }

    public static double getOffsettedXVal(LogEntry entry, Log log) {
        return (getXVal(entry, log) * getScalingFactor(log.getMaxDecimalPlaces())) + getXOffset(log);
    }

    public static int getXOffset(Log log) {
        final List<LogEntry> entries = entriesFromLog(log);

        if (log.isValidForNewX() && entries.size() > 0) {
            return -1 * (int) (GraphUtils.getXVal(entries.get(0), log)
                    * getScalingFactor(log.getMaxDecimalPlaces()));
        } else {
            return 0;
        }
    }

    public static int getIndexFromCustomXIndex(Log log, int index) {
        if (entriesFromLog(log).size() > index) {
            final LogEntry entry = entriesFromLog(log).get(index);

            final List<LogEntry> entries = log.getEntries();
            for (int current = 0; current < entries.size(); current++) {
                final LogEntry currentEntry = entries.get(current);
                if (currentEntry.getSeconds() == entry.getSeconds()) {
                    return current;
                }
            }
        }

        return INVALID_INDEX;
    }

    public static int getCustomXIndexFromIndex(Log log, int index) {
        if (entriesFromLog(log).size() > index) {
            final LogEntry entry = log.getEntries().get(index);

            final List<LogEntry> entries = entriesFromLog(log);
            for (int current = 0; current < entries.size(); current++) {
                final LogEntry currentEntry = entries.get(current);
                if (currentEntry.getSeconds() == entry.getSeconds()) {
                    return current;
                }
            }
        }

        return INVALID_INDEX;
    }

    public static boolean isWhole(double value) {
        return value % 1 == 0;
    }

    public static int getScalingFactor(int decimals) {
        return (int) Math.pow(10, Math.min(DECIMAL_PLACES, decimals));
    }

    private GraphUtils() {
    }
}
