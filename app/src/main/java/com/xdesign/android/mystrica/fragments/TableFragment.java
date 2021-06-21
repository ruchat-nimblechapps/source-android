package com.xdesign.android.mystrica.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.xdesign.android.common.lib.fragments.BaseFragment;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.adapters.LogAdapter;
import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.util.BestFitManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * @author keithkirk
 */
public class TableFragment extends BaseFragment {

    @BindView(R.id.myst_list_view_results)
    protected ListView resultsListView;

    private LogAdapter adapter;

    private Log log;

    private BestFitManager manager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        adapter = new LogAdapter(context);

        if (log != null) {
            log.addListener(adapter);
        }

        if (manager != null) {
            manager.setLogAdapter(adapter);


        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myst_fragment_table, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        resultsListView.setAdapter(adapter);
    }

    public void setBestFitManager(BestFitManager manager) {
        this.manager = manager;

        if (adapter != null) {
            manager.setLogAdapter(adapter);
        }
    }

    public void setLog(Log log) {
        this.log = log;

        if (adapter != null) {
            log.addListener(adapter);
        }
    }

    @OnItemClick(R.id.myst_list_view_results)
    protected void onItemClicked(int position) {
        if (manager != null) {
            manager.entryClicked(position);
        }
    }
}
