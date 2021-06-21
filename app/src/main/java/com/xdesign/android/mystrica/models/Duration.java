package com.xdesign.android.mystrica.models;


import androidx.annotation.Nullable;

/**
 * @author keithkirk
 */
public class Duration {

    public static final int MAX_HOURS   = 24;
    public static final int MAX_MINUTES = 59;
    public static final int MAX_SECONDS = 59;

    public static final int MIN_HOURS   = 0;
    public static final int MIN_MINUTES = 0;
    public static final int MIN_SECONDS = 0;

    private final int hours;
    private final int minutes;
    private final int seconds;

    @Nullable
    public static Duration decrement(Duration duration, long seconds) {
        final long durationSeconds
                = (((duration.getHours() * 60) + duration.getMinutes()) * 60) + duration.seconds;

        final long remainingSeconds = durationSeconds - seconds;

        if (remainingSeconds >= 0) {
            return new Duration(
                    (int) remainingSeconds / (60 * 60),
                    (int) (remainingSeconds % 60) / 60,
                    (int) remainingSeconds % (60 * 60)
            );
        }

        return null;
    }

    public Duration(int hours, int minutes, int seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
}
