package com.xdesign.android.common.lib.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.xdesign.android.common.lib.factories.AdapterSupportFactory;

import java.util.List;

/**
 * Subclasses need to provide an {@link AdapterSupportFactory} which controls
 * the creation of the views, and can provide further customisation for
 * creation/setup if required.
 */
public class GenericArrayAdapter<T> extends ArrayAdapter<T> {

    private final LayoutInflater inflater;
    private final AdapterSupportFactory<T> factory;

    public GenericArrayAdapter(
            Context context,
            AdapterSupportFactory<T> factory,
            List<T> objects) {

        super(context, 0, objects);

        this.inflater = LayoutInflater.from(getContext());
        this.factory = factory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final T object = getItem(position);

        if (rowView == null) {
            rowView = factory.createView(inflater, parent);
        }

        factory.setupView(rowView, object);

        return rowView;
    }
}
