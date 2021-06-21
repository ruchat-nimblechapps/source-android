package com.xdesign.android.common.lib.activities;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;

import com.xdesign.android.common.lib.app.delegates.Delegate;
import com.xdesign.android.common.lib.app.delegates.Delegation;
import com.xdesign.android.common.lib.app.delegates.utils.Delegator;

/**
 * {@link BaseActivity} with support for {@link Delegate}s.
 *
 * @see Delegate
 * @see Delegation
 */
public class BaseActivityWithDelegation extends BaseActivity implements Delegation {

    private final Delegator delegator = new Delegator();

    @Override
    protected void onDestroy() {
        delegator.onDestroy();

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        delegator.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle inState) {
        delegator.onRestoreInstanceState(inState);

        super.onRestoreInstanceState(inState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        delegator.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void registerDelegate(Delegate delegate) {
        delegator.registerDelegate(delegate);
    }

    @Override
    public void unregisterDelegate(Delegate delegate) {
        delegator.unregisterDelegate(delegate);
    }
}
