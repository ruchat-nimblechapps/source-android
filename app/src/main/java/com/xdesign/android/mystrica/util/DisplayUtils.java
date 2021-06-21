package com.xdesign.android.mystrica.util;

import android.content.Context;

import com.xdesign.android.mystrica.R;

/**
 * @author keithkirk
 */
public class DisplayUtils {

    private static final long SECONDS_IN_MINUTE = 60;
    private static final long SECONDS_IN_HOUR   = 60 * SECONDS_IN_MINUTE;

    public static String getDisplayableTime(Context context, long seconds) {
        final long hours = seconds / SECONDS_IN_HOUR;
        final long minutes = (seconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE;
        seconds = (seconds % SECONDS_IN_HOUR) % SECONDS_IN_MINUTE;

        if (hours > 0) {
            return context.getString(R.string.myst_format_hours,
                    hours,
                    minutes,
                    seconds);
        }
        if (minutes > 0) {
            return context.getString(R.string.myst_format_minutes,
                    minutes,
                    seconds);
        }

        return context.getString(R.string.myst_format_seconds,
                seconds);
    }

    public static String getPercentage(Context context, double value) {
        return context.getString(R.string.myst_format_percent, value);
    }

    public static String getRounded(Context context, double value) {
        return context.getString(R.string.myst_format_three_dp, value);
    }

    private DisplayUtils() {
    }
}
