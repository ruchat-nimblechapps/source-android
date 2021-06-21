package com.xdesign.android.common.lib.app.delegates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Allows addition of extra behaviours to activities or fragments.
 *
 * @see com.xdesign.android.common.lib.activities.BaseActivityWithDelegation
 * @see com.xdesign.android.common.lib.fragments.BaseFragmentWithDelegation
 */
public abstract class Delegate {

    protected static final int RESULT_OK = Activity.RESULT_OK;

    public void onDestroy() {
        // NOP
    }

    public void onSaveState(Bundle outState) {
        // NOP
    }

    public void onRestoreState(Bundle inState) {
        // NOP
    }

    /**
     * @return {@code True} if handled, else {@code false}.
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    protected final String getTag() {
        return getClass().getSimpleName();
    }
}
