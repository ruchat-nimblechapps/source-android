package com.xdesign.android.common.lib.adapters.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.xdesign.android.common.lib.adapters.support.PageItem;

/**
 * {@link PagerAdapter} to be used with an {@link Enum}s provided by {@link T},
 * which also need to implement {@link PageItem} to support view creation and
 * binding.
 * <p>
 * Useful for cases such as paged tutorials.
 */
public class EnumPagerAdapter<T extends Enum & PageItem> extends PagerAdapter {

    private final LayoutInflater inflater;
    private final T[] items;

    public EnumPagerAdapter(Context context, Class<T> cls) {
        inflater = LayoutInflater.from(context);
        items = cls.getEnumConstants();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = inflater.inflate(items[position].getLayout(), container, false);

        items[position].bind(view);

        view.setTag(items[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return ((T) ((View) object).getTag()).ordinal();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public final int getCount() {
        return items.length;
    }

    public final T getItem(int position) {
        return items[position];
    }
}
