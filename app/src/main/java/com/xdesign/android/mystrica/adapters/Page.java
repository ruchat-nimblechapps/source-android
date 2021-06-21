package com.xdesign.android.mystrica.adapters;


import androidx.fragment.app.Fragment;

/**
 * @author keithkirk
 */
public class Page {

    private final Fragment fragment;
    private final CharSequence title;

    public Page(Fragment fragment, CharSequence title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public CharSequence getTitle() {
        return title;
    }
}
