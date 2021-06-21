package com.xdesign.android.common.harness.adapters.support;

import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.BaseAdapter;

import com.xdesign.android.common.lib.adapters.support.EmptyViewObservers;
import com.xdesign.android.common.lib.adapters.support.EmptyViewSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class EmptyViewObserversTest {

    private View empty;

    @Before
    public void before() {
        empty = mock(View.class);
    }

    @After
    public void after() {
        empty = null;
    }

    @Test
    public void emptyViewBaseAdapterObserver() {
        EmptyViewObservers.forBaseAdapter(empty, mock(BaseEmptyAdapter.class)).onChanged();

        verify(empty, times(2)).setVisibility(View.GONE);
    }

    @Test
    public void emptyViewBaseAdapterObserverEmpty() {
        final BaseEmptyAdapter adapter = mock(BaseEmptyAdapter.class);
        when(adapter.showEmptyView()).thenReturn(true);

        EmptyViewObservers.forBaseAdapter(empty, adapter).onChanged();

        verify(empty, times(2)).setVisibility(View.VISIBLE);
    }

    @Test
    public void emptyViewRecyclerAdapterObserver() {
        final EmptyViewObservers.EmptyViewRecyclerAdapterObserver uut =
                EmptyViewObservers.forRecyclerAdapter(empty, mock(RecyclerEmptyAdapter.class));

        verify(empty).setVisibility(View.GONE);

        uut.onChanged();
        verify(empty, times(2)).setVisibility(View.GONE);

        uut.onItemRangeInserted(0, 0);
        verify(empty, times(3)).setVisibility(View.GONE);

        uut.onItemRangeRemoved(0, 0);
        verify(empty, times(4)).setVisibility(View.GONE);
    }

    @Test
    public void emptyViewRecyclerAdapterObserverEmpty() {
        final RecyclerEmptyAdapter adapter = mock(RecyclerEmptyAdapter.class);
        when(adapter.showEmptyView()).thenReturn(true);
        final EmptyViewObservers.EmptyViewRecyclerAdapterObserver uut =
                EmptyViewObservers.forRecyclerAdapter(empty, adapter);

        verify(empty).setVisibility(View.VISIBLE);

        uut.onChanged();
        verify(empty, times(2)).setVisibility(View.VISIBLE);

        uut.onItemRangeInserted(0, 0);
        verify(empty, times(3)).setVisibility(View.VISIBLE);

        uut.onItemRangeRemoved(0, 0);
        verify(empty, times(4)).setVisibility(View.VISIBLE);
    }

    @Test
    public void privateCtor() {
        assertEquals(1, EmptyViewObservers.class.getDeclaredConstructors().length);
        assertTrue(Modifier.isPrivate(
                EmptyViewObservers.class.getDeclaredConstructors()[0].getModifiers()));
    }

    protected static abstract class BaseEmptyAdapter
            extends BaseAdapter
            implements EmptyViewSupport {

        // NOP
    }

    protected static abstract class RecyclerEmptyAdapter
            extends RecyclerView.Adapter
            implements EmptyViewSupport {

        // NOP
    }
}
