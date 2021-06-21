package com.xdesign.android.common.lib.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.xdesign.android.common.lib.R;

/**
 * {@link Button} which allows setting a loading text using the
 * {@code xd_common_loadingText} attribute, or using {@link #setLoadingText(int)}/
 * {@link #setLoadingText(CharSequence). The loading state can be changed with
 * {@link #setLoadingState(boolean)}, which will also disable the clickable
 * state of the button.
 * <p>
 * The button will also take care of hiding itself when a soft keyboard opens
 * on the screen, providing that the {@code xd_common_hideOnKeyboard} attribute
 * has been set.
 */
@SuppressLint("AppCompatCustomView")
public class LoadingStateButton extends Button {

    private static final boolean HIDE_ON_KEYBOARD = false;
    private static final float HEIGHT_DIFF_THRESHOLD = 0.25f;

    private boolean loadingState;
    private CharSequence otherText;
    @Nullable
    private HideWhenKeyboardShownListener listener;

    public LoadingStateButton(Context context) {
        this(context, null);
    }

    public LoadingStateButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingStateButton(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        final TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LoadingStateButton,
                0,
                0);

        otherText = a.getText(R.styleable.LoadingStateButton_xd_common_loadingText);

        final boolean hideOnKeyboard = a.getBoolean(
                R.styleable.LoadingStateButton_xd_common_hideOnKeyboard,
                HIDE_ON_KEYBOARD);

        a.recycle();

        if (hideOnKeyboard) {
            listener = new HideWhenKeyboardShownListener();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (listener != null) {
            listener.register();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if (listener != null) {
            listener.notifyNewVisibility(visibility);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (listener != null) {
            listener.unregister();
        }
    }

    private void setVisibilityInner(int visibility) {
        super.setVisibility(visibility);
    }

    public void setLoadingState(boolean loadingState) {
        if (this.loadingState != loadingState) {
            this.loadingState = loadingState;

            // swap
            final CharSequence temp = getText();
            setText(otherText);
            otherText = temp;

            setClickable(!this.loadingState);
        }
    }

    public void setLoadingText(CharSequence loadingText) {
        if (loadingState) {
            setText(loadingText);
        } else {
            otherText = loadingText;
        }
    }

    public void setLoadingText(@StringRes int id) {
        setLoadingText(getResources().getText(id));
    }

    private class HideWhenKeyboardShownListener implements
            ViewTreeObserver.OnGlobalLayoutListener {

        private final View activityRoot;
        private final int screenHeight;

        private int masterVisibility;

        public HideWhenKeyboardShownListener() {
            if (getContext() instanceof Activity) {
                final Activity context = (Activity) getContext();

                activityRoot = context.getWindow().getDecorView().findViewById(
                        android.R.id.content);

                final Point size = new Point();
                context.getWindowManager().getDefaultDisplay().getSize(size);
                screenHeight = size.y;
            } else {
                activityRoot = null;
                screenHeight = 0;
            }

            masterVisibility = getVisibility();
        }

        @Override
        public void onGlobalLayout() {
            final View rootView = activityRoot.getRootView();
            final Rect rootRect = new Rect();
            rootView.getWindowVisibleDisplayFrame(rootRect);

            final float heightDiff = rootView.getHeight() - rootRect.bottom - rootRect.top;
            if (heightDiff / screenHeight > HEIGHT_DIFF_THRESHOLD) {
                setVisibilityInner(View.GONE);
            } else if (masterVisibility != View.GONE) {
                // only make visible if it hasn't been explicitly hidden
                setVisibilityInner(View.VISIBLE);
            }
        }

        public void register() {
            if (activityRoot != null) {
                activityRoot.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        }

        @SuppressWarnings("deprecation")
        public void unregister() {
            if (activityRoot != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    activityRoot.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                } else {
                    activityRoot.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                }
            }
        }

        public void notifyNewVisibility(int visibility) {
            masterVisibility = visibility;
        }
    }
}
