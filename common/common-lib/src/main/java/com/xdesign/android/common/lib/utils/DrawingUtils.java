package com.xdesign.android.common.lib.utils;

import android.content.res.Resources;

/**
 * @author Alex Macrae
 */
public final class DrawingUtils {

    private static final float BASELINE_DENSITY = 160f;

    /**
     * Converts device-independent pixels (dip/dp/dps) to pixels (px), depending on device density.
     *
     * @param   res resources
     * @param   dps value in density independent pixels
     * @return      value in pixels
     */
    public static float convertDpsToPixels(Resources res, float dps) {
        return dps * (res.getDisplayMetrics().densityDpi / BASELINE_DENSITY);
    }

    /**
     * Converts pixels (px) to device-independent pixels (dip/dp/dps), depending on device density.
     *
     * @param   res resources
     * @param   px  value in pixels
     * @return      value in device-independent pixels
     */
    public static float convertPixelsToDps(Resources res, float px) {
        return px / (res.getDisplayMetrics().densityDpi / BASELINE_DENSITY);
    }

    private DrawingUtils() {}
}
