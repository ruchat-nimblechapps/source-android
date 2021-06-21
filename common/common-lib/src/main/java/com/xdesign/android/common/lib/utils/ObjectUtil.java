package com.xdesign.android.common.lib.utils;

/**
 * Various utility functions for {@link Object}s.
 */
public final class ObjectUtil {

    /**
     * A null-safe version of {@link Object#equals(Object)}.
     *
     * @param   a   first {@link Object} to check
     * @param   b   second {@link Object} to check
     * @return      {@code true} if both are equal, else {@code false}, as per
     *              {@link Object#equals(Object)}
     */
    public static boolean safeEquals(Object a, Object b) {
        return a == b || !((a == null) || (b == null)) && a.equals(b);
    }

    private ObjectUtil() {}
}
