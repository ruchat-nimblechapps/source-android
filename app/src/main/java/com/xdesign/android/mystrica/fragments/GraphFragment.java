package com.xdesign.android.mystrica.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.xdesign.android.common.lib.fragments.BaseFragment;
import com.xdesign.android.common.lib.utils.ExceptionLogger;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.adapters.LineGraphAdapter;
import com.xdesign.android.mystrica.interfaces.BestFitManagerListener;
import com.xdesign.android.mystrica.interfaces.FileManagerListener;
import com.xdesign.android.mystrica.models.BestFit;
import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.util.BestFitManager;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

/**
 * @author keithkirk
 */
public class GraphFragment extends BaseFragment
        implements BestFitManagerListener, FileManagerListener {

    @BindView(R.id.myst_text_view_best_fit)
    protected TextView bestFitTextView;

    @BindView(R.id.myst_line_graph)
    protected LineChart graph;

    private Log log;
    private BestFit bestFit;

    private LineGraphAdapter adapter;
    private BestFitManager manager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setup();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.myst_fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setup();
        updateBestFitText();
    }

    public void setLog(Log log) {
        this.log = log;

        setup();
    }

    public void setBestFitManager(BestFitManager manager) {
        this.manager = manager;

        setup();
    }

    private void setup() {
        if (getContext() != null && graph != null && log != null && manager != null) {
            adapter = new LineGraphAdapter(getContext(), graph, log);

            manager.setGraph(graph);
            manager.addListener(this);
            manager.addListener(adapter);
        }
    }

    @Override
    public void onBestFitUpdated(BestFit bestFit) {
        this.bestFit = bestFit;

        updateBestFitText();
    }

    @Override
    public List<Pair<Boolean, Integer>> saveFile(String directoryPath, String name) {
        final List<Pair<Boolean, Integer>> states = new LinkedList<>();
        Pair<Boolean, Integer> state = new Pair<>(false, R.string.myst_label_graph);
        if (graph != null) {
            try {
                state = new Pair<>(graph.saveToPath(name, directoryPath), R.string.myst_label_graph);
            } catch (IllegalArgumentException e) {
                ExceptionLogger.log(e);
            }
        }
        states.add(state);
        return states;
    }

    private void updateBestFitText() {
        if (bestFitTextView != null && getActivity() != null) {
            final Resources res = getResources();
            final String text;

            if (bestFit == null) {
                text = res.getString(R.string.myst_label_best_fit);
            } else {
                if (bestFit.getIntercept() < 0) {
                    text = res.getString(R.string.myst_format_best_fit_negative,
                            bestFit.getSlope(), -1 * bestFit.getIntercept());
                } else {
                    text = res.getString(R.string.myst_format_best_fit_positive,
                            bestFit.getSlope(), bestFit.getIntercept());
                }
            }
            bestFitTextView.setText(text);
        }
    }
}
