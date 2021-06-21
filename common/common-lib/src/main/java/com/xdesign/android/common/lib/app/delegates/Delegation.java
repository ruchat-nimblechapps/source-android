package com.xdesign.android.common.lib.app.delegates;

import android.app.Activity;
import android.os.Bundle;

/**
 * Should be implemented by classes wishing to support {@link Delegate}s.
 * <p>
 * {@link Delegate}s should be added during {@link android.app.Activity#onCreate(Bundle)}/
 * {@link android.support.v4.app.Fragment#onAttach(Activity)} through
 * {@link #registerDelegate(Delegate)}.
 * <p>
 * There should generally be no need to use {@link #unregisterDelegate(Delegate)}.
 *
 * @see Delegate
 */
public interface Delegation {

    void registerDelegate(Delegate delegate);

    void unregisterDelegate(Delegate delegate);
}
