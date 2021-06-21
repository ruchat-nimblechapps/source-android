package com.xdesign.android.common.lib.adapters.recycler;


import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} for storing a items of a single type {@link T}, supplied
 * either as a {@link List} or as an array (ie for enums). For the former case regrowing
 * is not supported.
 */
public abstract class ItemisedAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private final List<T> items;

    public ItemisedAdapter(List<T> items) {
        this.items = items;
    }

    public ItemisedAdapter(T[] items) {
        this.items = Arrays.asList(items);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    protected abstract void onBindViewHolder(VH holder, T item);
}
