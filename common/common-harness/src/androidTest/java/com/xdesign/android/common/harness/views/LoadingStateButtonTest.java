package com.xdesign.android.common.harness.views;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.harness.activities.ViewTestingActivity;
import com.xdesign.android.common.lib.views.LoadingStateButton;
import com.xdesign.android.common.testing.rules.FixedActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * We can't easily test that the button hides when the keyboard comes up
 * as we cannot fully rely on whether a soft or hard keyboard IME is
 * available in the test environment.
 *
 * TODO look closer into testing loading state button hiding
 */
@RunWith(AndroidJUnit4.class)
public class LoadingStateButtonTest {

    @Rule
    public final FixedActivityTestRule<ViewTestingActivity> rule =
            new FixedActivityTestRule<ViewTestingActivity>(ViewTestingActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    return ViewTestingActivity.create(
                            InstrumentationRegistry.getTargetContext(),
                            R.layout.loading_state_button);
                }
            };

    private LoadingStateButton uut;

    @Before
    public void before() {
        uut = (LoadingStateButton) rule.getActivity().findViewById(R.id.loading_state_button);
    }

    @After
    public void after() {
        uut = null;
    }

    @UiThreadTest
    @Test
    public void initialState() {
        assertEquals(getString(R.string.loading_state_button_text), uut.getText());
    }

    @UiThreadTest
    @Test
    public void setLoadingState() {
        uut.setLoadingState(true);
        assertFalse(uut.isClickable());
        assertEquals(getString(R.string.loading_state_button_loading_text), uut.getText());

        uut.setLoadingState(false);
        assertTrue(uut.isClickable());
        assertEquals(getString(R.string.loading_state_button_text), uut.getText());
    }

    @UiThreadTest
    @Test
    public void setLoadingText() {
        uut.setLoadingText("test");
        uut.setLoadingState(true);

        assertEquals("test", uut.getText());
    }

    @UiThreadTest
    @Test
    public void setLoadingTextRes() {
        uut.setLoadingText(R.string.test);
        uut.setLoadingState(true);

        assertEquals(getString(R.string.test), uut.getText());
    }

    @UiThreadTest
    @Test
    public void setLoadingTextAlreadyLoading() {
        uut.setLoadingState(true);
        uut.setLoadingText("test");

        assertEquals("test", uut.getText());

        uut.setLoadingState(false);

        assertEquals(getString(R.string.loading_state_button_text), uut.getText());
    }

    private CharSequence getString(@StringRes int id) {
        return rule.getActivity().getString(id);
    }
}
