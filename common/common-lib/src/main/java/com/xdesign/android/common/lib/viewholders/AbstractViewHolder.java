package com.xdesign.android.common.lib.viewholders;


import androidx.annotation.LayoutRes;

/**
 * Base for view holders to be used with the
 * {@link com.xdesign.android.common.lib.adapters.GenericArrayAdapter} and the
 * {@link com.xdesign.android.common.lib.factories.AdapterSupportFactory}.
 * <p>
 * Views held by this view holder should be annotated with
 *  so they
 * can be injected by the
 * {@link com.xdesign.android.common.lib.factories.AdapterSupportFactory}.
 *
 * @param <T>
 */
public abstract class AbstractViewHolder<T> {

    /**
     * @return layout resource id which should be inflated for this view holder
     */
    @LayoutRes
    public abstract int getLayout();

    public abstract void setup(T object);
}
