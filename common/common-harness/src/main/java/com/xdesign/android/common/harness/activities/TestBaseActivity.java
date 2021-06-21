package com.xdesign.android.common.harness.activities;

import android.view.View;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.lib.activities.BaseActivity;

import butterknife.Bind;

public final class TestBaseActivity extends BaseActivity {

    @Bind(R.id.base_test)
    protected View viewTest;
}
