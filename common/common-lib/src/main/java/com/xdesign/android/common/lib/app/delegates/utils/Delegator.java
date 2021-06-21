package com.xdesign.android.common.lib.app.delegates.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.xdesign.android.common.lib.app.delegates.Delegate;
import com.xdesign.android.common.lib.app.delegates.Delegation;

import java.util.HashSet;
import java.util.Set;

/**
 * Helper for easier {@link Delegation} support using composition.
 *
 * TODO improve manual forwarding from activity/fragment lifecycle methods
 */
public class Delegator implements Delegation {

    private static final String TAG = Delegator.class.getSimpleName();

    private final Set<Delegate> delegates = new HashSet<>();

    public void onDestroy() {
        for (final Delegate delegate : delegates) {
            delegate.onDestroy();
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        for (final Delegate delegate : delegates) {
            delegate.onSaveState(outState);
        }
    }

    public void onRestoreInstanceState(@NonNull Bundle inState) {
        for (final Delegate delegate : delegates) {
            delegate.onRestoreState(inState);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (final Delegate delegate : delegates) {
            delegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void registerDelegate(Delegate delegate) {
        if (!delegates.add(delegate)) {
            Log.w(TAG, delegate + " has already been registered");
        }
    }

    @Override
    public void unregisterDelegate(Delegate delegate) {
        if (!delegates.remove(delegate)) {
            Log.w(TAG, delegate + " has not been registered previously");
        }
    }
}
