package com.xdesign.android.mystrica.enums;

import android.content.Context;

import com.xdesign.android.mystrica.R;

/**
 * @author keithkirk
 */
public enum SamplingRate {
    ONE_SECOND(     1,          R.string.myst_label_sampling_rate_one_second),
    FIVE_SECOND(    5,          R.string.myst_label_sampling_rate_five_seconds),
    THIRTY_SECOND(  30,         R.string.myst_label_sampling_rate_thirty_seconds),
    TWO_MINUTES(    2 * 60,     R.string.myst_label_sampling_rate_two_minutes),
    THIRTY_MINUTES( 30 * 60,    R.string.myst_label_sampling_rate_thirty_minutes);

    private final long delay;
    private final int textResource;

    public static String[] getNames(Context context) {
        final String[] names = new String[values().length];

        for (int i = 0 ; i < values().length ; i++) {
            names[i] = context.getResources().getString(values()[i].textResource);
        }

        return names;
    }

    SamplingRate(long delay, int textResource) {
        this.delay = delay * 1000;
        this.textResource = textResource;
    }

    public long getDelay() {
        return delay;
    }
}
