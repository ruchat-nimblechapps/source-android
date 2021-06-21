package com.xdesign.android.mystrica.util;

import android.content.Context;

/**
 * @author keithkirk
 */
public class Conversions {

    public static double transmittanceToAbsorbance(double transmittance) {
        final double x = (transmittance - 0.02) / 100.0;

        if (x <= 0.0) {
            return 0.0;
        } else {
            return (-Math.log10(x)) * 1.0 + 0.0002;
        }
    }

    private Conversions() {
    }
}
