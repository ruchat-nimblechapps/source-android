package com.xdesign.android.common.harness.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Log;

import com.xdesign.android.common.lib.utils.InstallationId;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class InstallationIdTest {

    private static final String TAG = InstallationIdTest.class.getSimpleName();

    @After
    public void after() {
        if (!new File(getContext().getFilesDir(), "installation").delete()) {
            Log.w(TAG, "Failed to clean up installation id file");
        }
    }

    @Test
    public void get() throws IOException {
        final String id = InstallationId.get(getContext().getFilesDir());
        
        assertTrue(!TextUtils.isEmpty(id));
        
        final String sameId = InstallationId.get(getContext().getFilesDir());
        
        assertEquals(id, sameId);
    }

    @Test
    public void privateCtor() {
        assertEquals(1, InstallationId.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isPrivate(
                InstallationId.class.getDeclaredConstructors()[0].getModifiers()));
    }

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }
}
