package com.xdesign.android.common.lib.adapters.support;

import android.view.View;

import androidx.annotation.LayoutRes;

public interface PageItem {

    @LayoutRes
    int getLayout();

    void bind(View view);
}
