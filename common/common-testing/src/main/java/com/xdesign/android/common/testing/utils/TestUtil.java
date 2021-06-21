package com.xdesign.android.common.testing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Collection of methods useful for testing.
 */
public final class TestUtil {
    
    /**
     * @param   list        the list to check against
     * @param   element     the element to find
     * @return              {@code true} if {@code element} is found in {@code list},
     *                      else {@code false}.
     */
    public static boolean arrayContains(int[] list, int element) {
        for (final int i : list) {
            if (i == element) {
                return true;
            }
        }

        return false;
    }

    /**
     * Sets the field named {@code name} on {@link Object} instance {@code on}
     * with value of {@link Object} {@code value}.
     *
     * Please note this method does not scan the superclasses for the field.
     */
    public static void setField(String name, Object on, Object value) {
        setField(name, on.getClass(), on, value);
    }

    /**
     * Sets the field named {@code name} declared by {@code cls} on {@link Object}
     * instance {@code on} with value of {@link Object} {@code value}.
     */
    public static void setField(String name, Class<?> cls, Object on, Object value) {
        try {
            final Field field = cls.getDeclaredField(name);

            field.setAccessible(true);
            field.set(on, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param   activity    {@link Activity} from which to pull the code
     * @return              result {@code int} set by the {@code activity}
     */
    public static int resultCodeForActivity(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mResultCode");
            field.setAccessible(true);

            return (int) field.get(activity);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param   activity    {@link Activity} from which to pull the {@link Intent}
     * @return              result {@link Intent} set by the {@code activity}
     */
    public static Intent resultIntentForActivity(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mResultData");
            field.setAccessible(true);

            return (Intent) field.get(activity);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * @param   context {@link Context} from which the asset should be loaded
     * @param   path    the {@link String} path of the asset
     * @return          the {@link String} contents of the asset from {@code path}
     */
    public static String loadFromAssets(Context context, String path) {
        final StringBuilder out = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(path)));
            
            String line;
            while ((line = br.readLine()) != null) {
                out.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // nop
                }
            }
        }
        
        return out.toString();
    }

    private TestUtil() {}
}
