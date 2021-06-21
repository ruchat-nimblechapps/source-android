package com.xdesign.android.common.harness.app.delegates.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import com.xdesign.android.common.lib.app.delegates.Delegate;
import com.xdesign.android.common.lib.app.delegates.utils.Delegator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public final class DelegatorTest {

    private Delegator uut;

    private Delegate delegate1;
    private Delegate delegate2;

    @Before
    public void before() {
        uut = new Delegator();

        uut.registerDelegate(delegate1 = mock(Delegate.class));
        uut.registerDelegate(delegate2 = mock(Delegate.class));
    }

    @After
    public void after() {
        uut = null;

        delegate1 = null;
        delegate2 = null;
    }

    @Test
    public void onDestroy() {
        uut.onDestroy();

        assertDelegates(new Action() {
            @Override
            public void perform(Delegate with) {
                verify(with).onDestroy();
            }
        });
    }

    @Test
    public void onSaveInstanceState() {
        final Bundle state = Bundle.EMPTY;

        uut.onSaveInstanceState(state);

        assertDelegates(new Action() {
            @Override
            public void perform(Delegate with) {
                verify(with).onSaveState(same(state));
            }
        });
    }

    @Test
    public void onRestoreInstanceState() {
        final Bundle state = Bundle.EMPTY;

        uut.onRestoreInstanceState(state);

        assertDelegates(new Action() {
            @Override
            public void perform(Delegate with) {
                verify(with).onRestoreState(state);
            }
        });
    }

    @Test
    public void onActivityResult() {
        final int requestCode = 1;
        final int resultCode = Activity.RESULT_OK;
        final Intent data = new Intent();

        uut.onActivityResult(requestCode, resultCode, data);

        assertDelegates(new Action() {
            @Override
            public void perform(Delegate with) {
                verify(with).onActivityResult(same(requestCode), same(resultCode), same(data));
            }
        });
    }

    @Test
    public void registerDelegate() {
        final Delegate delegate = mock(Delegate.class);

        uut.registerDelegate(delegate);
        uut.onDestroy();

        verify(delegate).onDestroy();
    }

    @Test
    public void registerDelegateTwice() {
        uut.registerDelegate(delegate1);
        uut.onDestroy();

        verify(delegate1, times(1)).onDestroy();
    }

    @Test
    public void unregisterDelegate() {
        uut.unregisterDelegate(delegate1);
        uut.onDestroy();

        verify(delegate1, never()).onDestroy();
        verify(delegate2).onDestroy();
    }

    private void assertDelegates(Action action) {
        action.perform(delegate1);
        action.perform(delegate2);
    }

    private interface Action {

        void perform(Delegate with);
    }
}
