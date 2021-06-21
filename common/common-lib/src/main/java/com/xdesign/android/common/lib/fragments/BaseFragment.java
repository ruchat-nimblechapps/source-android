package com.xdesign.android.common.lib.fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;



import butterknife.ButterKnife;

/**
 * {@link Fragment} which injects ButterKnife after {@link #onViewCreated(View, Bundle)}
 * will be called.
 *
 * @author Alex Macrae
 */
public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
