package com.xdesign.android.common.harness.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.xdesign.android.common.lib.app.delegates.Delegate;
import com.xdesign.android.common.lib.app.delegates.utils.Delegator;
import com.xdesign.android.common.lib.fragments.BaseFragmentWithDelegation;
import com.xdesign.android.common.testing.utils.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public final class BaseFragmentWithDelegationTest {

    private BaseFragmentWithDelegation uut;

    private Delegator delegator;

    @Before
    public void before() {
        uut = new BaseFragmentWithDelegation();
        delegator = mock(Delegator.class);

        TestUtil.setField("delegator", uut, delegator);
    }

    @After
    public void after() {
        uut = null;
        delegator = null;
    }

    @Test
    public void onActivityCreated() {
        final Bundle state = Bundle.EMPTY;

        uut.onActivityCreated(state);

        verify(delegator).onRestoreInstanceState(same(state));
    }

    @Test
    public void onDestroy() {
        try {
            uut.onDestroy();
        } catch (NullPointerException e) {
            /*
             * This is expected since the fragment is not attached to an actual
             * activity, but testing it on its own makes it much easier. If the
             * delegation failed then the verifications will fail.
             */
            Log.w(getClass().getSimpleName(), e);
        }

        verify(delegator).onDestroy();
    }

    @Test
    public void onActivityResult() {
        final int requestCode = 1;
        final int resultCode = 1;
        final Intent data = new Intent();

        uut.onActivityResult(requestCode, resultCode, data);

        verify(delegator).onActivityResult(same(requestCode), same(resultCode), same(data));
    }

    @Test
    public void onSaveInstanceState() {
        final Bundle state = Bundle.EMPTY;

        uut.onSaveInstanceState(state);

        verify(delegator).onSaveInstanceState(same(state));
    }

    @Test
    public void registerDelegate() {
        final Delegate delegate = mock(Delegate.class);

        uut.registerDelegate(delegate);

        verify(delegator).registerDelegate(same(delegate));
    }

    @Test
    public void unregisterDelegate() {
        final Delegate delegate = mock(Delegate.class);

        uut.unregisterDelegate(delegate);

        verify(delegator).unregisterDelegate(same(delegate));
    }
}
