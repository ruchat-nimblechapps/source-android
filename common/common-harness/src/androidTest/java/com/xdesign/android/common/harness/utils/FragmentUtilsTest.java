package com.xdesign.android.common.harness.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static com.xdesign.android.common.lib.utils.FragmentUtils.unRollBackstackByOne;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class FragmentUtilsTest {

    private FragmentManager mngr;

    @Before
    public void before() {
        mngr = mock(FragmentManager.class);
    }

    @After
    public void after() {
        mngr = null;
    }

    @Test
    public void noTopLevelFragments() {
        when(mngr.getFragments()).thenReturn(null);
        assertFalse(unRollBackstackByOne(mngr));
    }

    @Test
    public void emptyTopLevelFragments() {
        when(mngr.getFragments()).thenReturn(new ArrayList<Fragment>(0));
        assertFalse(unRollBackstackByOne(mngr));
    }

    @Test
    public void nullTopLevelFragment() {
        when(mngr.getFragments()).thenReturn(Collections.singletonList((Fragment) null));
        assertFalse(unRollBackstackByOne(mngr));
    }

    @Ignore("getChildFragmentManager() needs to be non-final")
    @Test
    public void topLevelFragmentWithoutEntries() {
        final Fragment fragment = mock(Fragment.class);
        final FragmentManager childMngr = mock(FragmentManager.class);
        when(mngr.getFragments()).thenReturn(Collections.singletonList(fragment));
        when(fragment.getChildFragmentManager()).thenReturn(childMngr);
        when(childMngr.getBackStackEntryCount()).thenReturn(0);

        assertFalse(unRollBackstackByOne(mngr));
    }

    @Ignore("getChildFragmentManager() needs to be non-final")
    @Test
    public void topLevelFragmentWithEntries() {
        final Fragment fragment = mock(Fragment.class);
        final FragmentManager childMngr = mock(FragmentManager.class);
        when(mngr.getFragments()).thenReturn(Collections.singletonList(fragment));
        when(fragment.getChildFragmentManager()).thenReturn(childMngr);
        when(childMngr.getBackStackEntryCount()).thenReturn(1);

        assertTrue(unRollBackstackByOne(mngr));
    }
}
