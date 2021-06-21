package com.xdesign.android.common.harness.adapters.recycler;

import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xdesign.android.common.lib.adapters.recycler.ItemisedAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class ItemisedAdapterTest {

    @Test
    public void getItemCount() {
        ItemisedAdapter<Object, Holder> adapter;

        final List<Object> list = Arrays.asList(new Object(), new Object());
        adapter = new Adapter(list) {
            @Override
            protected void onBindViewHolder(Holder holder, Object item) {
                // NOP
            }

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };

        assertEquals(list.size(), adapter.getItemCount());

        final Object[] array = new Object[] {new Object()};
        adapter = new Adapter(array) {
            @Override
            protected void onBindViewHolder(Holder holder, Object item) {
                // NOP
            }

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };

        assertEquals(array.length, adapter.getItemCount());
    }

    @Test
    public void getItem() {
        ItemisedAdapter<Object, Holder> adapter;

        final List<Object> list = Arrays.asList(new Object(), new Object());
        adapter = new Adapter(list) {
            @Override
            protected void onBindViewHolder(Holder holder, Object item) {
                // NOP
            }

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };

        assertSame(list.get(0), adapter.getItem(0));
        assertSame(list.get(1), adapter.getItem(1));

        final Object[] array = new Object[] {new Object(), new Object()};
        adapter = new Adapter(array) {
            @Override
            protected void onBindViewHolder(Holder holder, Object item) {
                // NOP
            }

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };

        assertSame(array[0], adapter.getItem(0));
        assertSame(array[1], adapter.getItem(1));
    }

    @Test
    public void onBindViewHolder() {
        final Object first = new Object();
        final Object second = new Object();
        final AtomicBoolean called = new AtomicBoolean();

        new Adapter(Arrays.asList(first, second)) {
            @Override
            protected void onBindViewHolder(Holder holder, Object item) {
                called.set(true);
                assertSame(second, item);
            }

            @Override
            public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        }.onBindViewHolder(null, 1);

        assertTrue(called.get());
    }

    private abstract static class Adapter extends ItemisedAdapter<Object, Holder> {

        public Adapter(List<Object> items) {
            super(items);
        }

        public Adapter(Object[] items) {
            super(items);
        }
    }

    private static final class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
