package com.xdesign.android.common.harness.activities;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;

import com.xdesign.android.common.lib.activities.BaseActivityWithDelegation;
import com.xdesign.android.common.lib.app.delegates.Delegate;
import com.xdesign.android.common.lib.app.delegates.utils.Delegator;
import com.xdesign.android.common.testing.rules.FixedActivityTestRule;
import com.xdesign.android.common.testing.utils.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public final class BaseActivityWithDelegationTest {

    private static final int TIMEOUT = 1000;

    @Rule
    public final FixedActivityTestRule<TestBaseActivityWithDelegation> rule =
            new FixedActivityTestRule<>(TestBaseActivityWithDelegation.class);

    private Delegator delegator;

    @Before
    public void before() {
        delegator = mock(Delegator.class);

        TestUtil.setField(
                "delegator",
                BaseActivityWithDelegation.class,
                rule.getActivity(),
                delegator);
    }

    @After
    public void after() {
        delegator = null;
    }

    @Test
    /**
     * callActivityOnDestroy is commented out due to a bug so we need to
     * this rough workaround to wait for onDestroy to happen.
     *
     * TODO change to properly destroy activity when Android bug is fixed
     */
    public void onDestroy() throws InterruptedException {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                getActivity().finish();
            }
        });
        getInstrumentation().waitForIdleSync();

        Thread.sleep(TIMEOUT);

        verify(delegator).onDestroy();
    }

    @UiThreadTest
    @Test
    public void onSaveInstanceState() {
        final Bundle state = Bundle.EMPTY;

        getInstrumentation().callActivityOnSaveInstanceState(getActivity(), state);

        verify(delegator).onSaveInstanceState(eq(state));
    }

    @UiThreadTest
    @Test
    public void onRestoreInstanceState() {
        final Bundle state = Bundle.EMPTY;

        getInstrumentation().callActivityOnRestoreInstanceState(getActivity(), state);

        verify(delegator).onRestoreInstanceState(eq(state));
    }

    @Test
    /**
     * There is a problem with the extras contained inside a result not being
     * transferred across activities so we use null, but ideally there should
     * be an actual content.
     *
     * TODO change intent instance to a valid one instead of null
     */
    public void onActivityResult() {
        final int requestCode = 1;

        final Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, null);
        final Instrumentation.ActivityMonitor monitor = getInstrumentation()
                .addMonitor(TestActivity.class.getName(), result, false);

        getActivity().startActivityForResult(
                new Intent(getActivity(), TestActivity.class), requestCode);

        final Activity activity = monitor.waitForActivityWithTimeout(TIMEOUT);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.setResult(result.getResultCode(), result.getResultData());
                activity.finish();
            }
        });
        getInstrumentation().waitForIdleSync();

        verify(delegator).onActivityResult(
                eq(requestCode),
                eq(monitor.getResult().getResultCode()),
                eq(monitor.getResult().getResultData()));
    }

    @Test
    public void registerDelegate() {
        final Delegate delegate = mock(Delegate.class);

        getActivity().registerDelegate(delegate);

        verify(delegator).registerDelegate(same(delegate));
    }

    @Test
    public void unregisterDelegate() {
        final Delegate delegate = mock(Delegate.class);

        getActivity().unregisterDelegate(delegate);

        verify(delegator).unregisterDelegate(same(delegate));
    }

    private TestBaseActivityWithDelegation getActivity() {
        return rule.getActivity();
    }

    private static Instrumentation getInstrumentation() {
        return InstrumentationRegistry.getInstrumentation();
    }
}
