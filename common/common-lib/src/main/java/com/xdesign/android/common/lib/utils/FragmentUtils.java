package com.xdesign.android.common.lib.utils;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;

public final class FragmentUtils {

    /**
     * Pops the backstack in a depth-first fashion.
     * <p>
     * May be used to workaround the bug where nested fragments with backstack
     * entries inside an activity do not respect the back button press, and instead
     * of popping the deepest backstack entry the activity finishes.
     * <p>
     * Example usage:
     * <pre>{@code
     * &#64;Override
     * public void onBackPressed() &#123;
     *     if (!unRollBackstackByOne(getSupportFragmentManager())) &#123;
     *         super.onBackPressed();
     *     &#125;
     * &#125;
     * }</pre>
     *
     * @param   mngr    the top-level {@link FragmentManager} from which to start
     *                  popping entries
     * @return          {@code true} if a backstack entry was popped,
     *                  else {@code false}
     *
     * @see <a href="http://code.google.com/p/android/issues/detail?id=40323">Bug report</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean unRollBackstackByOne(FragmentManager mngr) {
        final List<Fragment> fragments = mngr.getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return false;
        }

        for (final Fragment fragment : fragments) {
            if (fragment == null) {
                continue;
            }

            final FragmentManager childMngr =
                    fragment.getChildFragmentManager();

            if (!unRollBackstackByOne(childMngr)) {
                if (childMngr.getBackStackEntryCount() > 0) {
                    childMngr.popBackStack();
                    return true;
                }
            }
        }

        return false;
    }

    private FragmentUtils() {}
}
