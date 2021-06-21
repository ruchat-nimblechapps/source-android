package com.xdesign.android.common.lib.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

public final class LocationUtils {

    /**
     * @return  {@code true} if location services are enabled in the Android settings,
     *          else {@code false}.
     */
    public static boolean isLocationSettingEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return (Settings.Secure.getInt(
                        context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE) != Settings.Secure.LOCATION_MODE_OFF);
            } catch (Settings.SettingNotFoundException e) {
                return false;
            }
        } else {
            return isLocationSettingEnabledPreApi19(context);
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isLocationSettingEnabledPreApi19(Context context) {
        return !TextUtils.isEmpty(Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED));
    }

    private LocationUtils() {}
}
