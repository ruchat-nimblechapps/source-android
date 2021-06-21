package com.xdesign.android.common.lib.utils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.xdesign.android.common.lib.annotations.DeletionOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Utility class with methods for generic operations on model classes.
 */
public final class ModelUtil {

    private static final DeletionOrder.DeletionOrderComparator COMPARATOR =
            new DeletionOrder.DeletionOrderComparator();

    /**
     * Deletes all {@link Model}s without breaking any foreign key relationships,
     * providing classes have been correctly annotated with {@link DeletionOrder}.
     *
     * @param excludeNonAnnotated {@link Model} classes not annotated with
     *                            {@link DeletionOrder} will be excluded if {@code true}, else all
     *                            will be deleted
     */
    public static void deleteAllSafely(boolean excludeNonAnnotated) {
        final List<Class<? extends Model>> ordered = new ArrayList<>();

        for (final TableInfo info : Cache.getTableInfos()) {
            if (excludeNonAnnotated
                    && !info.getType().isAnnotationPresent(DeletionOrder.class)) {

                continue;
            }

            ordered.add(info.getType());
        }
        Collections.sort(ordered, COMPARATOR);

        for (final Class<? extends Model> aClass : ordered) {
            new Delete().from(aClass).execute();

            ActiveAndroid.execSQL(String.format(
                    Locale.ENGLISH,
                    "DELETE FROM SQLITE_SEQUENCE WHERE NAME = '%1$s'",
                    aClass.getAnnotation(Table.class).name()));
        }
    }

    private ModelUtil() {}
}
