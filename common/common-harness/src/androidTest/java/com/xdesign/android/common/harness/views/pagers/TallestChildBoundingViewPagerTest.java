package com.xdesign.android.common.harness.views.pagers;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.harness.activities.ViewTestingActivity;
import com.xdesign.android.common.lib.views.pagers.TallestChildBoundingViewPager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public final class TallestChildBoundingViewPagerTest {

    @Rule
    public final ActivityTestRule<ViewTestingActivity> rule =
            new ActivityTestRule<ViewTestingActivity>(ViewTestingActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    return ViewTestingActivity.create(
                            InstrumentationRegistry.getTargetContext(),
                            R.layout.tallest_child_bounding_view_pager);
                }
            };

    private TallestChildBoundingViewPager uut;

    @Before
    public void before() throws Throwable {
        uut = (TallestChildBoundingViewPager) rule.getActivity().findViewById(
                R.id.tallest_child_bounding_view_pager);

        rule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uut.setAdapter(new Adapter());
            }
        });
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @After
    public void after() {
        uut = null;
    }

    @Test
    public void adjustsHeight() {
        runAfterInflated(new Runnable() {
            @Override
            public void run() {
                assertEquals(200, uut.getMeasuredHeight());
                assertEquals(200, uut.getHeight());
            }
        });
    }

    @Test
    public void heightDoesNotJump() throws Throwable {
        runAfterInflated(new Runnable() {
            @Override
            public void run() {
                for (final Adapter.Page page : Adapter.Page.values()) {
                    uut.setCurrentItem(page.ordinal());

                    assertEquals(200, uut.getHeight());
                }
            }
        });
    }

    private void runAfterInflated(final Runnable action) {
        uut.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                uut.getViewTreeObserver().removeOnPreDrawListener(this);

                action.run();

                return false;
            }
        });
    }

    private static final class Adapter extends PagerAdapter {

        private final LayoutInflater inflater = LayoutInflater.from(
                InstrumentationRegistry.getTargetContext());

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View view = inflater.inflate(
                    Page.values()[position].layout, container, false);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public int getCount() {
            return Page.values().length;
        }

        @Override
        public int getItemPosition(Object object) {
            return ((Page) ((View) object).getTag()).ordinal();
        }

        private enum Page {

            ONE(R.layout.tallest_child_bounding_view_pager_page1),
            TWO(R.layout.tallest_child_bounding_view_pager_page2),
            THREE(R.layout.tallest_child_bounding_view_pager_page3);

            @LayoutRes
            final int layout;

            Page(@LayoutRes int layout) {
                this.layout = layout;
            }
        }
    }
}
