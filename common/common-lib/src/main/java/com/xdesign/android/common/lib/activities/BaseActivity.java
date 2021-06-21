package com.xdesign.android.common.lib.activities;

import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * {@link AppCompatActivity} which injects ButterKnife after either of the
 * {@code setContentView} methods or {@link #addContentView(View, ViewGroup.LayoutParams)}
 * method is called.
 *
 * @author Alex Macrae
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
//        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
//        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
//        ButterKnife.bind(this);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
//        ButterKnife.bind(this);
    }
}
