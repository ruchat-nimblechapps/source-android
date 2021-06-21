package com.xdesign.android.common.lib.views.pagers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * {@link ViewPager} which binds its height to the height of its tallest child.
 * <p>
 * Useful when we need to show images with varying heights. Consider using together with
 * {@link com.xdesign.android.common.lib.views.images.BoundsAdjustableImageView}.
 */
public class TallestChildBoundingViewPager extends ViewPager {

    public TallestChildBoundingViewPager(Context context) {
        super(context);
    }

    public TallestChildBoundingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);

            child.measure(
                    widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            
            final int childHeight = child.getMeasuredHeight();
            if (childHeight > maxHeight) {
                maxHeight = childHeight;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        
        /*
         * This is inefficient but it stops our height from jumping around when
         * our children get cycled out.
         */
        setOffscreenPageLimit(adapter.getCount());
    }
}
