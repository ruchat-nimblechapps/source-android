package com.xdesign.android.common.testing.rules;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.test.UiThreadTest;

import org.junit.runner.Description;

/**
 * {@link ActivityTestRule} which respects the {@link android.test.UiThreadTest}
 * annotation.
 *
 * TODO remove after https://code.google.com/p/android/issues/detail?id=157356
 */
public class FixedActivityTestRule<T extends Activity> extends ActivityTestRule<T> {

    public FixedActivityTestRule(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    protected boolean shouldRunOnUiThread(Description description) {
        return (super.shouldRunOnUiThread(description)
                || (description.getAnnotation(UiThreadTest.class) != null));
    }
}
