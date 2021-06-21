package com.xdesign.android.common.harness.fragments;

import android.view.View;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.lib.fragments.BaseFragment;

import butterknife.Bind;

public final class TestBaseFragment extends BaseFragment {

    @Bind(R.id.base_test)
    protected View viewTest;
}
