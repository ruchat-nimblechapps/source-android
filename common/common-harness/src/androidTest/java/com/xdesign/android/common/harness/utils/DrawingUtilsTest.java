package com.xdesign.android.common.harness.utils;

import android.content.res.Resources;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;

import com.xdesign.android.common.lib.utils.DrawingUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class DrawingUtilsTest {

    private Resources res;
    private DisplayMetrics dm;

    @Before
    public void before() {
        res = mock(Resources.class);
        when(res.getDisplayMetrics()).thenReturn(dm = mock(DisplayMetrics.class));
    }

    @After
    public void after() {
        res = null;
        dm = null;
    }

    @Test
    public void convertDpsToPixels() {
        run(120, new Runnable() {
            @Override
            public void run() {
                assertEquals(0.75f, DrawingUtils.convertDpsToPixels(res, 1f), 0f);
            }
        });
        run(160, new Runnable() {
            @Override
            public void run() {
                assertEquals(1f, DrawingUtils.convertDpsToPixels(res, 1f), 0f);
            }
        });
        run(240, new Runnable() {
            @Override
            public void run() {
                assertEquals(1.5f, DrawingUtils.convertDpsToPixels(res, 1f), 0f);
            }
        });
    }

    @Test
    public void convertPixelsToDps() {
        run(120, new Runnable() {
            @Override
            public void run() {
                assertEquals(1f, DrawingUtils.convertPixelsToDps(res, 0.75f), 0f);
            }
        });
        run(160, new Runnable() {
            @Override
            public void run() {
                assertEquals(1f, DrawingUtils.convertPixelsToDps(res, 1f), 0f);
            }
        });
        run(240, new Runnable() {
            @Override
            public void run() {
                assertEquals(1f, DrawingUtils.convertPixelsToDps(res, 1.5f), 0f);
            }
        });
    }

    private void run(int densityDpi, Runnable action) {
        dm.densityDpi = densityDpi;

        action.run();
    }
}
