package com.xdesign.android.common.lib.utils;

import java.util.Calendar;
import java.util.Date;

public final class DateAndTimeUtil {

    public static final int MS_SECOND = 1000;
    public static final int MS_MINUTE = 60 * MS_SECOND;
    public static final int MS_HOUR = 60 * MS_MINUTE;
    public static final int MS_DAY = 24 * MS_HOUR;
    public static final int MS_WEEK = 7 * MS_DAY;

    public static boolean isToday(Calendar date) {
        return isToday(date.getTime());
    }

    public static boolean isToday(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final Calendar now = Calendar.getInstance();

        return calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && calendar.get(Calendar.DATE) == now.get(Calendar.DATE);
    }

    /**
     * Wraps the time of {@code time} to the next {@code interval} specified
     * in minutes. If the next interval falls outside of the {@code cutoff}
     * value in minutes, then the following interval is used.
     * <p>
     * For example with the values of 5 and 3, 12:03 would be shifted to 12:10,
     * however 12:02 would be shifted to 12:05.
     * <p>
     */
    public static Date wrapTimeToNext(Date time, int interval, int cutoff) {
        if (interval < 1) {
            throw new IllegalArgumentException("interval cannot be zero or negative");
        }
        if (cutoff < 0) {
            throw new IllegalArgumentException("cutoff cannot be negative");
        }
        if (interval < cutoff) {
            throw new IllegalArgumentException("interval cannot be less than cutoff");
        }

        final Calendar result = Calendar.getInstance();
        result.setTime(time);

        final int toAdd = interval - result.get(Calendar.MINUTE) % interval;
        result.add(Calendar.MINUTE, toAdd);

        if (toAdd <= cutoff) {
            result.add(Calendar.MINUTE, interval);
        }

        return result.getTime();
    }

    private DateAndTimeUtil() {}
}
