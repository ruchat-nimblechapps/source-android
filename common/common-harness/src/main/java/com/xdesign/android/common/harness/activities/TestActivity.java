package com.xdesign.android.common.harness.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xdesign.android.common.harness.R;

/**
 * {@link AppCompatActivity} which can be instrumented when testing fragments.
 */
public final class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_test);
    }
}
