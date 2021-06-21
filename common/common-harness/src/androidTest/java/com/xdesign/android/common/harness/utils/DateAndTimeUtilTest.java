package com.xdesign.android.common.harness.utils;

import android.support.test.runner.AndroidJUnit4;

import com.xdesign.android.common.lib.utils.DateAndTimeUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class DateAndTimeUtilTest {
    
    @Test
    public void isToday() {
        final Calendar calendar = Calendar.getInstance();
        assertTrue(DateAndTimeUtil.isToday(calendar));
        
        calendar.add(Calendar.YEAR, 1);
        assertFalse(DateAndTimeUtil.isToday(calendar));

        calendar.add(Calendar.YEAR, -2);
        assertFalse(DateAndTimeUtil.isToday(calendar));

        calendar.add(Calendar.MONTH, 1);
        assertFalse(DateAndTimeUtil.isToday(calendar));

        calendar.add(Calendar.MONTH, -2);
        assertFalse(DateAndTimeUtil.isToday(calendar));

        calendar.add(Calendar.DATE, 1);
        assertFalse(DateAndTimeUtil.isToday(calendar));

        calendar.add(Calendar.DATE, -2);
        assertFalse(DateAndTimeUtil.isToday(calendar));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrapTimeToNextIntervalLessThanZero() {
        assertEquals(date(0, 0), DateAndTimeUtil.wrapTimeToNext(date(0, 0), -1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrapTimeToNextIntervalZero() {
        assertEquals(date(0, 0), DateAndTimeUtil.wrapTimeToNext(date(0, 0), 0, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrapTimeToNextCutoffNegative() {
        assertEquals(date(0, 0), DateAndTimeUtil.wrapTimeToNext(date(0, 0), 1, -1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrapTimeToNextIntervalLessThanCutoff() {
        assertEquals(date(0, 0), DateAndTimeUtil.wrapTimeToNext(date(0, 0), 1, 2));
    }

    @Test
    public void wrapTimeToNext() {
        assertEquals(date(0, 2), DateAndTimeUtil.wrapTimeToNext(date(0, 0), 2, 0));
        assertEquals(date(0, 2), DateAndTimeUtil.wrapTimeToNext(date(0, 0), 2, 1));
        assertEquals(date(0, 4), DateAndTimeUtil.wrapTimeToNext(date(0, 0), 2, 2));
        assertEquals(date(0, 4), DateAndTimeUtil.wrapTimeToNext(date(0, 1), 2, 1));
    }

    @Test
    public void privateCtor() {
        assertEquals(1, DateAndTimeUtil.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isPrivate(
                DateAndTimeUtil.class.getDeclaredConstructors()[0].getModifiers()));
    }

    private static Date date(int hour, int min) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
