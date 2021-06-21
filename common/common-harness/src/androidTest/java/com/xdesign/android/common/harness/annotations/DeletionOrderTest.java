package com.xdesign.android.common.harness.annotations;

import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.Model;
import com.xdesign.android.common.lib.annotations.DeletionOrder;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class DeletionOrderTest {
    
    @Test
    public void comparator() {
        final List<Class<? extends Model>> classes = Arrays.asList(
                SecondOrderModel.class,
                DefaultOrderModel.class,
                FirstOrderModel.class,
                RegularModel.class,
                FourthOrderModel.class);

        Collections.sort(classes, new DeletionOrder.DeletionOrderComparator());
        
        assertSame(FirstOrderModel.class, classes.get(0));
        assertSame(SecondOrderModel.class, classes.get(1));
        assertSame(FourthOrderModel.class, classes.get(2));
        assertTrue(
                classes.get(3) == DefaultOrderModel.class
                || classes.get(3) == RegularModel.class);
        assertTrue(
                classes.get(4) == DefaultOrderModel.class
                || classes.get(4) == RegularModel.class);
    }

    @DeletionOrder(1)
    private static final class FirstOrderModel extends Model {}

    @DeletionOrder(2)
    private static final class SecondOrderModel extends Model {}

    @DeletionOrder(4)
    private static final class FourthOrderModel extends Model {}
    
    @DeletionOrder()
    private static final class DefaultOrderModel extends Model {}
    
    private static final class RegularModel extends Model {}
}
