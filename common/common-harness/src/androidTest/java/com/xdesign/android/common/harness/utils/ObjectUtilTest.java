package com.xdesign.android.common.harness.utils;

import android.support.test.runner.AndroidJUnit4;

import com.xdesign.android.common.lib.utils.ObjectUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class ObjectUtilTest {

    @Test
    public void safeEquals() {
        assertFalse(ObjectUtil.safeEquals(null, new Object()));
        assertFalse(ObjectUtil.safeEquals(new Object(), null));
        assertFalse(ObjectUtil.safeEquals(new Object(), new Object()));

        assertTrue(ObjectUtil.safeEquals(null, null));
        final Object same = new Object();
        assertTrue(ObjectUtil.safeEquals(same, same));
    }

    @Test
    public void privateCtor() {
        assertEquals(1, ObjectUtil.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isPrivate(
                ObjectUtil.class.getDeclaredConstructors()[0].getModifiers()));
    }
}
