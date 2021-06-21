package com.xdesign.android.common.lib.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.xdesign.android.common.lib.R;

/**
 * {@link BaseFragment} for showing OSS licenses from an assets file, as supplied to
 * {@link #create(Class, String)}.
 * <p>
 * The licenses should be declared in the file in the following form:
 * <pre>
 * {@code
 * <h3>Library title</h3>
 * <pre>
 * Library license.
 * </pre>
 * }
 * </pre>
 *
 * @see com.xdesign.android.common.lib.activities.BaseFragmentHolderActivity
 */
public class OssLicencesFragment extends BaseFragment {

    private static final String PREFIX = "file:///android_asset/";
    private static final String FILENAME = "filename";

    protected WebView viewWeb;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_oss_licenses, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewWeb = (WebView) view.findViewById(R.id.oss_licenses_webview);
        viewWeb.loadUrl(PREFIX + getArguments().getString(FILENAME));
    }

    /**
     * @param   cls         the class of the {@link OssLicencesFragment} to instantiate
     * @param   fileName    the name of the file located in the assets folder
     */
    public static <T extends OssLicencesFragment> OssLicencesFragment create(
            Class<T> cls,
            String fileName) {

        final Bundle args = new Bundle(1);
        args.putString(FILENAME, fileName);

        final OssLicencesFragment fragment;
        try {
            fragment = cls.newInstance();
        } catch (java.lang.InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        fragment.setArguments(args);

        return fragment;
    }
}
