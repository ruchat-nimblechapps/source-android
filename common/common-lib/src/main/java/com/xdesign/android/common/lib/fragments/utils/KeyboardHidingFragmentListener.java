package com.xdesign.android.common.lib.fragments.utils;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * {@link FragmentManager.OnBackStackChangedListener} which hides the keyboard when
 * <p>
 * This class will not handle cases such as tab switching in a tabbed activity.
 */
public final class KeyboardHidingFragmentListener
        implements FragmentManager.OnBackStackChangedListener {

    private final Activity activity;
    private final InputMethodManager manager;

    public KeyboardHidingFragmentListener(Activity activity) {
        this.activity = activity;
        this.manager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onBackStackChanged() {
        manager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }
}
