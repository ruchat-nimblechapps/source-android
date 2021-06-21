package com.xdesign.android.mystrica;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public final class Mystrica extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


    }

    public static void dismissKeyboard(Context context, View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    view.getApplicationWindowToken(), 0);
        }
    }
}
