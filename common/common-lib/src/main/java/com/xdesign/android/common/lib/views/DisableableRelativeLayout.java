package com.xdesign.android.common.lib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

/**
 * {@link RelativeLayout} which will consume touch events when set to disabled
 * and also change its alpha state.
 *
 * TODO alpha should come from an attribute
 */
public class DisableableRelativeLayout extends RelativeLayout {

    private static final float ALPHA_DISABLED = 0.3f;
    private static final float ALPHA_ENABLED = 1f;

    public DisableableRelativeLayout(Context context) {
        super(context);
    }

    public DisableableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableableRelativeLayout(
            Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DisableableRelativeLayout(
            Context context,
            AttributeSet attrs,
            int defStyleAttr,
            int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        return !isEnabled() || super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            setAlpha(ALPHA_ENABLED);
        } else {
            setAlpha(ALPHA_DISABLED);
        }
    }
}
