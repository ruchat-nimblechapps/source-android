package com.xdesign.android.mystrica.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author keithkirk
 */
public class MystFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Page> pages;

    public MystFragmentPagerAdapter(FragmentManager fm, List<Page> pages) {
        super(fm);
        this.pages = pages;
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).getTitle();
    }
}
