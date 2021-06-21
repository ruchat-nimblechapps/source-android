package com.xdesign.android.common.lib.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.xdesign.android.common.lib.R;

/**
 * {@link BaseActivity} to be used for displaying a single {@link Fragment}.
 * <p>
 * Useful for uses in fragment-based apps which may need to display a fragment
 * in a separate activity.
 */
public abstract class BaseFragmentHolderActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_fragment_holder);

        final Fragment fragment = getFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(   R.id.base_fragment_holder_root,
                        fragment,
                        fragment.getClass().getSimpleName())
                .commit();
    }

    /**
     * Reloads the fragment, by calling {@link #getFragment()}.
     */
    protected void reloadFragment() {
        final Fragment fragment = getFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.base_fragment_holder_root,
                        fragment,
                        fragment.getClass().getSimpleName())
                .commit();
    }

    /**
     * Will be called during {@link #onCreate(Bundle)} to load the fragment
     * into the activity. Will also be called from {@link #reloadFragment()}.
     */
    protected abstract Fragment getFragment();
}
