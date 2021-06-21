package com.xdesign.android.common.harness.views;

import android.content.Intent;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.test.UiThreadTest;
import android.view.MotionEvent;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.harness.activities.ViewTestingActivity;
import com.xdesign.android.common.lib.views.DisableableLinearLayout;
import com.xdesign.android.common.testing.rules.FixedActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public final class DisableableLinearLayoutTest {

    @Rule
    public final FixedActivityTestRule<ViewTestingActivity> rule =
            new FixedActivityTestRule<ViewTestingActivity>(ViewTestingActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    return ViewTestingActivity.create(
                            InstrumentationRegistry.getTargetContext(),
                            R.layout.disableable_linear_layout);
                }
            };

    private DisableableLinearLayout uut;

    @Before
    public void before() {
        uut = (DisableableLinearLayout) rule.getActivity().findViewById(
                R.id.disableable_linear_layout);
    }

    @After
    public void after() {
        uut = null;
    }

    @UiThreadTest
    @Test
    public void alpha() {
        uut.setEnabled(false);
        assertEquals(0.3f, uut.getAlpha(), 0f);

        uut.setEnabled(true);
        assertEquals(1f, uut.getAlpha(), 0f);
    }

    @UiThreadTest
    @Test
    public void touches() {
        uut.setEnabled(false);
        assertFalse(uut.dispatchTouchEvent(MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                uut.getX(),
                uut.getY(),
                0)));

        uut.setEnabled(true);
        assertFalse(uut.dispatchTouchEvent(MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                uut.getX(),
                uut.getY(),
                0)));
    }
}
