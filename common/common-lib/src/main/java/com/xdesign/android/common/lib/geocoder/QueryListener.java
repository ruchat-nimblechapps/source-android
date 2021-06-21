package com.xdesign.android.common.lib.geocoder;

public interface QueryListener {

    void onQueryFinished(String text);

    void onQueryError(String defaultText);
}
