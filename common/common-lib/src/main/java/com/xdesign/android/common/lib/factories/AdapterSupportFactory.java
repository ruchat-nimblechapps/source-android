package com.xdesign.android.common.lib.factories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdesign.android.common.lib.viewholders.AbstractViewHolder;

import butterknife.ButterKnife;

/**
 * A factory which needs to be extended in order to provide {@link View} and
 * {@link .AbstractViewHolder} creation support for the
 * {@link com.xdesign.android.common.lib.adapters.GenericArrayAdapter}.
 */
public abstract class AdapterSupportFactory<T> {

    /**
     * Creates a {@link View}, populates the {@link AbstractViewHolder},
     * and adds it to the newly created view as the tag.
     *
     * @param   inflater    the inflater for the view
     * @param   parent      the parent of the view
     * @return              the newly created {@link View}
     */
    public View createView(LayoutInflater inflater, ViewGroup parent) {
        final AbstractViewHolder<T> viewHolder = createViewHolder();
        final View view = inflater.inflate(viewHolder.getLayout(), parent, false);

        ButterKnife.bind(viewHolder, view);
        view.setTag(viewHolder);

        return view;
    }

    /**
     * Sets up the {@code view} with the provided {@code object}.
     *
     * @param view          the {@link View} which needs to be setup
     * @param object        the object to setup with
     */
    public void setupView(View view, T object) {
        getViewHolder(view).setup(object);
    }

    @SuppressWarnings("unchecked")
    protected AbstractViewHolder<T> getViewHolder(View view) {
        return (AbstractViewHolder<T>) view.getTag();
    }

    protected abstract AbstractViewHolder<T> createViewHolder();
}
