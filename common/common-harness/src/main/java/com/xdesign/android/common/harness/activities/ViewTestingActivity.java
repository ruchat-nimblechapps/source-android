package com.xdesign.android.common.harness.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

public final class ViewTestingActivity extends AppCompatActivity {

    private static final String LAYOUT = "layout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getIntent().getIntExtra(LAYOUT, -1));
    }

    public static Intent create(Context context, @LayoutRes int layout) {
        return new Intent(context, ViewTestingActivity.class).putExtra(LAYOUT, layout);
    }
}
