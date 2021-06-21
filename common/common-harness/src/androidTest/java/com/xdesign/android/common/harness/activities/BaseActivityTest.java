package com.xdesign.android.common.harness.activities;

import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import android.view.View;
import android.view.ViewGroup;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.testing.rules.FixedActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public final class BaseActivityTest {

    @Rule
    public final FixedActivityTestRule<TestBaseActivity> rule =
            new FixedActivityTestRule<>(TestBaseActivity.class);

    @UiThreadTest
    @Test
    public void butterKnifeInjectedFirst() {
        getActivity().setContentView(R.layout.activity_base_test);

        assertNotNull(getActivity().viewTest);
    }

    @UiThreadTest
    @Test
    public void butterKnifeInjectedSecond() {
        getActivity().setContentView(inflate());

        assertNotNull(getActivity().viewTest);
    }

    @UiThreadTest
    @Test
    public void butterKnifeInjectedThird() {
        getActivity().setContentView(inflate(), new ViewGroup.LayoutParams(0, 0));

        assertNotNull(getActivity().viewTest);
    }

    @UiThreadTest
    @Test
    public void butterKnifeInjectedOnAdd() {
        getActivity().addContentView(inflate(), new ViewGroup.LayoutParams(0, 0));

        assertNotNull(getActivity().viewTest);
    }

    private View inflate() {
        return getActivity().getLayoutInflater().inflate(R.layout.activity_base_test, null);
    }

    private TestBaseActivity getActivity() {
        return rule.getActivity();
    }
}
