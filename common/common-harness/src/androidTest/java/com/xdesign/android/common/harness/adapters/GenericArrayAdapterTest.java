package com.xdesign.android.common.harness.adapters;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xdesign.android.common.lib.adapters.GenericArrayAdapter;
import com.xdesign.android.common.lib.factories.AdapterSupportFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public final class GenericArrayAdapterTest {

    private AdapterSupportFactory<Object> factory;
    private GenericArrayAdapter<Object> adapter;

    private Object item;
    private ViewGroup parent;

    @Before
    public void before() {
        factory = mock(AdapterSupportFactory.class);
        adapter = new GenericArrayAdapter<>(
                InstrumentationRegistry.getTargetContext(),
                factory,
                Collections.singletonList(item = new Object()));

        parent = new LinearLayout(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void after() {
        parent = null;

        adapter = null;
        item = null;
        factory = null;
    }

    @Test
    public void goesToFactoryForSetup() {
        final View view = new View(InstrumentationRegistry.getTargetContext());

        adapter.getView(0, view, parent);

        verify(factory).setupView(eq(view), eq(item));
        verify(factory, never()).createView(any(LayoutInflater.class), any(ViewGroup.class));
    }

    @Test
    public void goesToFactoryForCreationAndSetup() {
        adapter.getView(0, null, parent);

        verify(factory).createView(any(LayoutInflater.class), eq(parent));
        verify(factory).setupView(any(View.class), eq(item));
    }
}
