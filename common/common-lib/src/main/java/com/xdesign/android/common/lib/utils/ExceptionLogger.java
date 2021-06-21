package com.xdesign.android.common.lib.utils;

import android.text.TextUtils;
import android.util.Log;


import java.util.Locale;

/**
 * Class which should be used for logging non-fatal exception to Crashlytics.
 * The class aims to work around the issue of Crashlytics not printing to the
 * local log on a {@link } call, but can
 * also be used to supply extra context for the log.
 */
public final class ExceptionLogger {

    /**
     * Logs the exception without printing it locally.
     */
    public static void log(Throwable throwable) {
        log(throwable, null, null);
    }

    /**
     * Logs the exception printing it locally with tag {@code tag}.
     */
    public static void log(Throwable throwable, String tag) {
        log(throwable, tag, null);
    }

    /**
     * Same as {@link #log(Throwable, String, String)} but for
     * format-style messages.
     */
    public static void log(
            Throwable throwable,
            String tag,
            String format,
            Object... args) {

        log(throwable, tag, String.format(
                Locale.ENGLISH, format, args));
    }

    /**
     * Logs the exception printing it locally with tag {@code tag} and
     * message {@code msg}.
     */
    public static void log(
            Throwable throwable,
            String tag,
            String msg) {

        if (!TextUtils.isEmpty(tag)) {
            if (!TextUtils.isEmpty(msg)) {
                /*
                 * If a message has been provided then consider this of enough
                 * importance to supply the extra context to Crashlytics. This
                 * will unfortunately result in a duplicate log to logcat, but
                 * we would also like to see the exception.
                 */

                Log.w(tag, msg, throwable);
            } else {
                Log.w(tag, throwable);
            }
        }

    }

    private ExceptionLogger() {}
}
