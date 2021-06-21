package com.xdesign.android.common.harness.activities;

import android.support.v4.app.Fragment;

import com.xdesign.android.common.lib.activities.BaseFragmentHolderActivity;

public final class TestBaseFragmentHolderActivity extends BaseFragmentHolderActivity {

    @Override
    protected Fragment getFragment() {
        return new Fragment();
    }

    @Override
    public void reloadFragment() {
        super.reloadFragment();
    }
}
