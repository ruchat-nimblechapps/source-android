package com.xdesign.android.common.lib.views.grids;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * {@link GridView} which expands its height to fit the height of its contents, so scrolling
 * will never be triggered.
 * <p>
 * Useful when we need to show a grid inside other elements which already provide scrolling
 * functionality.
 *
 * @deprecated Use a RecyclerView instead.
 */
@Deprecated
public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NoScrollGridView(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*
         * http://stackoverflow.com/a/12931731
         */
        final int heightSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        
        super.onMeasure(widthMeasureSpec, heightSpec);
        
        getLayoutParams().height = getMeasuredHeight();
    }
}
