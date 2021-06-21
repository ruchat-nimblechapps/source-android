package com.xdesign.android.common.lib.fragments;

import android.content.Intent;
import android.os.Bundle;

import com.xdesign.android.common.lib.app.delegates.Delegate;
import com.xdesign.android.common.lib.app.delegates.Delegation;
import com.xdesign.android.common.lib.app.delegates.utils.Delegator;

/**
 * {@link BaseFragment} with support for {@link Delegate}s.
 *
 * @see Delegate
 * @see Delegation
 */
public class BaseFragmentWithDelegation extends BaseFragment implements Delegation {

    private final Delegator delegator = new Delegator();

    @Override
    public void onActivityCreated(Bundle inState) {
        super.onActivityCreated(inState);

        delegator.onRestoreInstanceState(inState);
    }

    @Override
    public void onDestroy() {
        delegator.onDestroy();

        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        delegator.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        delegator.onSaveInstanceState(outState);
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
