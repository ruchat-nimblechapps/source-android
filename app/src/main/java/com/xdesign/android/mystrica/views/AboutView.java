package com.xdesign.android.mystrica.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xdesign.android.common.lib.utils.ExceptionLogger;
import com.xdesign.android.mystrica.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author keithkirk
 */
public class AboutView extends ScrollView {

    private static final String BUILD_SEPARATOR = "-";

    @BindView(R.id.myst_text_view_build)
    protected TextView buildTextView;

    @BindView(R.id.myst_text_view_licences)
    protected TextView licenseTextView;

    public AboutView(Context context) {
        this(context, null);
    }

    public AboutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AboutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AboutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.myst_view_about, this);

        ButterKnife.bind(this);

        try {
            final PackageInfo pInfo = getContext()
                    .getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), 0);

            final String version;

            if (pInfo.versionName.contains(BUILD_SEPARATOR)) {
                version = pInfo.versionName.substring(0,
                        pInfo.versionName.indexOf(BUILD_SEPARATOR));
            } else {
                version = pInfo.versionName;
            }

            buildTextView.setText(getResources()
                    .getString(R.string.myst_format_build, version));
            buildTextView.setVisibility(VISIBLE);
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionLogger.log(e);
        }

        licenseTextView.setText(Html.fromHtml(buildOssLicensesText()));
    }

    private String buildOssLicensesText() {
        final StringBuilder builder = new StringBuilder();
        final String joiner = getResources().getString(R.string.wts_oss_joiner);

        for (final String item : getResources().getStringArray(R.array.wts_oss_licenses)) {
            builder.append(item);
            builder.append(joiner);
        }
        builder.delete(builder.length() - joiner.length(), builder.length());

        return builder.toString();
    }
}
