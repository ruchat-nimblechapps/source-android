package com.xdesign.android.common.harness.fragments;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;

import com.xdesign.android.common.harness.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public final class BaseFragmentTest {

    private TestBaseFragment fragment;

    @Before
    public void before() {
        fragment = new TestBaseFragment();
    }

    @After
    public void after() {
        fragment = null;
    }

    @Test
    public void butterKnifeInjected() {
        fragment.onViewCreated(getInflater().inflate(R.layout.fragment_base_test, null), null);

        assertNotNull(fragment.viewTest);
    }

    private LayoutInflater getInflater() {
        return LayoutInflater.from(InstrumentationRegistry.getTargetContext());
    }
}
