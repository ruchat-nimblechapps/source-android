package com.xdesign.android.common.harness.fragments;

import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.webkit.WebView;

import com.xdesign.android.common.lib.fragments.OssLicencesFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public final class OssLicenseFragmentTest {

    private OssLicencesFragment uut;

    @Before
    public void before() {
        uut = OssLicencesFragment.create(OssLicencesFragment.class, "test_licenses");
    }

    @After
    public void after() {
        uut = null;
    }

    @Test
    public void licensesLoaded() {
        final WebView webView = mock(WebView.class);
        final View view = mock(View.class);
        when(view.findViewById(com.xdesign.android.common.lib.R.id.oss_licenses_webview))
                .thenReturn(webView);

        uut.onViewCreated(view, null);

        verify(webView).loadUrl(eq("file:///android_asset/test_licenses"));
    }
}
