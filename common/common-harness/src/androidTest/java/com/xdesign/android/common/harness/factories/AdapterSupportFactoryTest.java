package com.xdesign.android.common.harness.factories;

import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdesign.android.common.lib.factories.AdapterSupportFactory;
import com.xdesign.android.common.lib.viewholders.AbstractViewHolder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public final class AdapterSupportFactoryTest {

    private AbstractViewHolder<Object> holder;
    private AdapterSupportFactory<Object> factory;

    @Before
    public void before() {
        holder = mock(AbstractViewHolder.class);
        factory = new AdapterSupportFactory<Object>() {
            @Override
            protected AbstractViewHolder<Object> createViewHolder() {
                return holder;
            }
        };
    }

    @After
    public void after() {
        factory = null;
        holder = null;
    }

    @Test
    public void createsView() {
        final int layout = 1;
        final ViewGroup parent = mock(ViewGroup.class);
        final LayoutInflater inflater = mock(LayoutInflater.class);
        final View view = mock(View.class);
        when(holder.getLayout()).thenReturn(layout);
        when(inflater.inflate(same(layout), same(parent), eq(false))).thenReturn(view);

        assertSame(view, factory.createView(inflater, parent));
    }

    @Test
    public void setsViewHolder() {
        final LayoutInflater inflater = mock(LayoutInflater.class);
        final View view = mock(View.class);
        when(inflater.inflate(any(int.class), any(ViewGroup.class), anyBoolean()))
                .thenReturn(view);

        factory.createView(inflater, mock(ViewGroup.class));

        verify(view).setTag(same(holder));
    }

    @Test
    public void setsUpView() {
        final View view = mock(View.class);
        final Object object = new Object();
        when(view.getTag()).thenReturn(holder);

        factory.setupView(view, object);

        verify(holder).setup(same(object));
    }
}
