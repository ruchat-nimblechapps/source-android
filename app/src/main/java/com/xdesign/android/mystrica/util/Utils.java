package com.xdesign.android.mystrica.util;

/**
 * @author keithkirk
 */
public class Utils {

    public static long parseTime(String string) {
        final String[] components = string.split(" ");
        long seconds = 0;

        for (String current : components) {
            final char type = current.charAt(current.length() - 1);
            final String intSegment = current.substring(0, current.length() - 1);
            final int value = Integer.parseInt(intSegment);

            switch (type) {
                case 'h':
                    seconds += value * 60 * 60;
                    break;
                case 'm':
                    seconds += value * 60;
                    break;
                case 's':
                    seconds += value;
                    break;
            }
        }

        return seconds;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private Utils() {
    }
}
