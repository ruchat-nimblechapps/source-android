package com.xdesign.android.common.harness.app.delegates;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.xdesign.android.common.lib.app.delegates.TakeOrPickPictureDelegate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class TakeOrPickPictureDelegateTest {

    private static final String TAG = TakeOrPickPictureDelegateTest.class.getSimpleName();

    private Activity activity;
    private TakeOrPickPictureDelegate.WithSelected action;

    private File directory;

    @Before
    public void before() {
        activity = mock(Activity.class);
        action = mock(TakeOrPickPictureDelegate.WithSelected.class);

        directory = InstrumentationRegistry.getTargetContext().getExternalCacheDir();

        when(activity.getExternalCacheDir()).thenReturn(directory);
        when(activity.getPackageManager()).thenReturn(
                InstrumentationRegistry.getTargetContext().getPackageManager());
        when(activity.getResources()).thenReturn(
                InstrumentationRegistry.getTargetContext().getResources());
    }

    @After
    public void after() {
        activity = null;
        action = null;

        delete(directory);
        directory = null;
    }

    @Test
    public void invoke() {
        new Creator(1).create().invoke();

        verify(activity).startActivityForResult(any(Intent.class), eq(1));
    }

    @Test
    public void withSelectedFromCamera() {
        final TakeOrPickPictureDelegate uut = new Creator(1).create();
        final Intent data = new Intent().setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        uut.invoke();
        uut.onActivityResult(1, Activity.RESULT_OK, data);

        verify(action).perform(notNull(Uri.class));
        verify(action, never()).failed();
    }

    @Test
    public void withSelectedNotFromCamera() {
        final TakeOrPickPictureDelegate uut = new Creator(1).create();
        final Intent data = new Intent().setData(Uri.EMPTY);

        uut.invoke();
        uut.onActivityResult(1, Activity.RESULT_OK, data);

        verify(action).perform(same(Uri.EMPTY));
        verify(action, never()).failed();
    }

    @Test
    public void cleansUp() {
        final TakeOrPickPictureDelegate uut = new Creator(1).create();

        uut.invoke();
        uut.invoke();
        uut.onDestroy();

        assertEquals(0, directory.list().length);
    }

    @Test
    public void doesNotCleanUp() {
        final TakeOrPickPictureDelegate uut = new Creator(1) {
            @Override
            protected void apply(TakeOrPickPictureDelegate.Builder builder) {
                builder.cleanUp(false);
            }
        }.create();

        uut.invoke();
        uut.invoke();
        uut.onDestroy();

        assertEquals(2, directory.list().length);
    }

    @Test
    public void savesAndRestoresState() {
        TakeOrPickPictureDelegate uut = new Creator(1).create();
        final Bundle state = new Bundle();

        uut.invoke();
        uut.onSaveState(state);

        uut = new Creator(1).create();

        uut.onRestoreState(state);
        uut.onActivityResult(1, Activity.RESULT_OK, new Intent().setData(Uri.EMPTY));
        uut.onDestroy();

        verify(action).perform(same(Uri.EMPTY));
        assertEquals(0, directory.list().length);
    }

    @Test
    public void locationAttributes() {
        final File dir = new File(directory, File.separator + "dir");
        if (!dir.mkdirs()) {
            Log.w(TAG, "Failed to create " + dir);
        }
        final String prefix = "prefix";
        final String ext = "ext";
        final TakeOrPickPictureDelegate uut = new Creator(1) {
            @Override
            protected void apply(TakeOrPickPictureDelegate.Builder builder) {
                builder.directory(dir);
                builder.prefix(prefix);
                builder.extension(ext);
            }
        }.create();

        uut.invoke();
        uut.invoke();

        assertEquals(2, dir.listFiles().length);
        for (final File file : dir.listFiles()) {
            assertEquals(dir, file.getParentFile());
            assertTrue(file.getName().startsWith(prefix));
            assertTrue(file.getName().endsWith(ext));
        }
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            for (final File item : file.listFiles()) {
                delete(item);
            }
        }

        if ((file != directory) && !file.delete()) {
            Log.w(TAG, "Failed to delete " + file);
        }
    }

    private class Creator {

        private final int requestCode;

        Creator(int requestCode) {
            this.requestCode = requestCode;
        }

        final TakeOrPickPictureDelegate create() {
            final TakeOrPickPictureDelegate.Builder builder =
                    new TakeOrPickPictureDelegate.Builder(activity, requestCode, action);

            apply(builder);

            return builder.build();
        }

        protected void apply(TakeOrPickPictureDelegate.Builder builder) {
            // NOP
        }
    }
}
