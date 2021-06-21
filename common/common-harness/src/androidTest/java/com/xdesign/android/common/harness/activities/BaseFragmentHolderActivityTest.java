package com.xdesign.android.common.harness.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import com.xdesign.android.common.testing.rules.FixedActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public final class BaseFragmentHolderActivityTest {

    @Rule
    public final FixedActivityTestRule<TestBaseFragmentHolderActivity> rule =
            new FixedActivityTestRule<>(TestBaseFragmentHolderActivity.class);

    @Test
    public void loadedOnCreation() {
        final List<Fragment> fragments =
                rule.getActivity().getSupportFragmentManager().getFragments();

        assertEquals(1, fragments.size());
        assertSame(Fragment.class, fragments.get(0).getClass());
    }

    @Test
    public void reloaded() {
        final Fragment old = getFragment();

        rule.getActivity().reloadFragment();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        assertNotNull(getFragment());
        assertNotSame(old, getFragment());
    }

    private Fragment getFragment() {
        return rule.getActivity().getSupportFragmentManager().getFragments().get(0);
    }
}
