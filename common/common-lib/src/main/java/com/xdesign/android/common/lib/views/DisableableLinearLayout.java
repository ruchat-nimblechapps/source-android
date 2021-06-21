package com.xdesign.android.common.lib.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

/**
 * {@link LinearLayout} which will consume touch events when set to disabled
 * and also change its alpha state.
 *
 * TODO alpha should come from an attribute
 */
public class DisableableLinearLayout extends LinearLayout {

    private static final float ALPHA_DISABLED = 0.3f;
    private static final float ALPHA_ENABLED = 1f;

    public DisableableLinearLayout(Context context) {
        super(context);
    }

    public DisableableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisableableLinearLayout(
            Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DisableableLinearLayout(
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
