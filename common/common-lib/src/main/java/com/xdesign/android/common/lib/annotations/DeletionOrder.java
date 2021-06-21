package com.xdesign.android.common.lib.annotations;

import com.activeandroid.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

/**
 * Annotation to be added to models with foreign key relationships, for defining an
 * order in which the models should be deleted in order to avoid breaking the constraints.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DeletionOrder {

    int value() default 0;

    /**
     * A {@link Comparator} which can be used for ordering a collection of {@link Model} classes.
     */
    final class DeletionOrderComparator implements Comparator<Class<? extends Model>> {

        @Override
        public int compare(Class<? extends Model> lhs, Class<? extends Model> rhs) {
            final int lhsOrder;
            if (lhs.isAnnotationPresent(DeletionOrder.class)) {
                lhsOrder = lhs.getAnnotation(DeletionOrder.class).value();
            } else {
                lhsOrder = 0;
            }

            final int rhsOrder;
            if (rhs.isAnnotationPresent(DeletionOrder.class)) {
                rhsOrder = rhs.getAnnotation(DeletionOrder.class).value();
            } else {
                rhsOrder = 0;
            }

            // models without an order should be at the end
            if (lhsOrder == 0 && rhsOrder == 0) {
                return 0;
            } else if (lhsOrder == 0) {
                return 1;
            } else if (rhsOrder == 0) {
                return -1;
            }

            final int diff = lhsOrder - rhsOrder;
            if (diff < 0) {
                return -1;
            } else if (diff > 1) {
                return 1;
            } else {
                return diff;
            }
        }
    }
}
