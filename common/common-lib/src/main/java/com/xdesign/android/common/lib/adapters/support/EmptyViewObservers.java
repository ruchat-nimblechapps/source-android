package com.xdesign.android.common.lib.adapters.support;

import android.database.DataSetObserver;
import android.view.View;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Data set observers to be used with regular {@link android.widget.ListView}s and
 * {@link androidx.recyclerview.widget.RecyclerView}s wishing to show an empty "no items"
 * view when there are no items.
 * <p>
 * The two variants need to be registered through
 * {@link BaseAdapter#registerDataSetObserver(DataSetObserver)} and
 *  respectively,
 * and the adapter needs to implement {@link EmptyViewSupport}.
 * <p>
 * Note that the the observers only show and hide the {@code empty} {@link View}
 * and don't do anything with the actual recycler/list view.
 */
public final class EmptyViewObservers {

    private EmptyViewObservers() {}

    public static <T extends BaseAdapter & EmptyViewSupport>
            EmptyViewBaseAdapterObserver<T> forBaseAdapter(View empty, T adapter) {

        return new EmptyViewBaseAdapterObserver<>(empty, adapter);
    }

    public static <T extends RecyclerView.Adapter & EmptyViewSupport>
            EmptyViewRecyclerAdapterObserver<T> forRecyclerAdapter(View empty, T adapter) {

        return new EmptyViewRecyclerAdapterObserver<>(empty, adapter);
    }

    private static void update(View empty, EmptyViewSupport support) {
        empty.setVisibility(support.showEmptyView() ? View.VISIBLE : View.GONE);
    }

    public static final class EmptyViewBaseAdapterObserver
            <T extends BaseAdapter & EmptyViewSupport>
            extends DataSetObserver {

        private final View empty;
        private final T adapter;

        protected EmptyViewBaseAdapterObserver(View empty, T adapter) {
            this.empty = empty;
            this.adapter = adapter;

            update();
        }

        @Override
        public void onChanged() {
            update();
        }

        private void update() {
            EmptyViewObservers.update(empty, adapter);
        }
    }

    public static final class EmptyViewRecyclerAdapterObserver
            <T extends RecyclerView.Adapter & EmptyViewSupport>
            extends RecyclerView.AdapterDataObserver {

        private final View empty;
        private final T adapter;

        public EmptyViewRecyclerAdapterObserver(View empty, T adapter) {
            this.empty = empty;
            this.adapter = adapter;

            update();
        }

        @Override
        public void onChanged() {
            update();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            update();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            update();
        }

        private void update() {
            EmptyViewObservers.update(empty, adapter);
        }
    }
}
