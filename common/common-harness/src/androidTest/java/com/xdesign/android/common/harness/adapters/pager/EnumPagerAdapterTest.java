package com.xdesign.android.common.harness.adapters.pager;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xdesign.android.common.lib.adapters.pager.EnumPagerAdapter;
import com.xdesign.android.common.lib.adapters.support.PageItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class EnumPagerAdapterTest {

    private Context context;
    private LayoutInflater inflater;
    private ViewGroup container;

    @Before
    public void before() {
        context = mock(Context.class);
        inflater = mock(LayoutInflater.class);
        container = mock(ViewGroup.class);

        when(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(inflater);
    }

    @After
    public void after() {
        context = null;
        inflater = null;
        container = null;
    }

    @Test
    public void instantiateItem() {
        final View mockedView = mock(View.class);
        when(inflater.inflate(Page.TWO.getLayout(), container, false)).thenReturn(mockedView);

        final View result = (View) new EnumPagerAdapter<>(context, Page.class)
                .instantiateItem(container, 1);

        assertSame(mockedView, result);
        verify(mockedView).setAlpha(anyFloat());
        verify(mockedView).setTag(same(Page.TWO));
        verify(container).addView(mockedView);
    }

    @Test
    public void destroyItem() {
        final View view = mock(View.class);
        new EnumPagerAdapter<>(context, Page.class).destroyItem(container, 1, view);

        verify(container).removeView(same(view));
    }

    @Test
    public void getItemPosition() {
        final View view = mock(View.class);
        when(view.getTag()).thenReturn(Page.TWO);
        final int result = new EnumPagerAdapter<>(context, Page.class).getItemPosition(view);

        assertSame(Page.TWO.ordinal(), result);
    }

    @Test
    public void isViewFromObject() {
        final View view = mock(View.class);
        final EnumPagerAdapter<Page> uut = new EnumPagerAdapter<>(context, Page.class);

        assertTrue(uut.isViewFromObject(view, view));
        assertFalse(uut.isViewFromObject(view, new Object()));
    }

    @Test
    public void getCount() {
        assertEquals(Page.values().length, new EnumPagerAdapter<>(context, Page.class).getCount());
    }

    @Test
    public void getItem() {
        assertSame(Page.TWO, new EnumPagerAdapter<>(context, Page.class).getItem(1));
    }

    private enum Page implements PageItem {

        ONE(1),
        TWO(2);

        private int layout;

        Page(int layout) {
            this.layout = layout;
        }

        @Override
        public int getLayout() {
            return layout;
        }

        @Override
        public void bind(View view) {
            view.setAlpha(0f);
        }
    }
}
