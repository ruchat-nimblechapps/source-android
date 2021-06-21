package com.xdesign.android.mystrica.util;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.xdesign.android.mystrica.adapters.LogAdapter;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.interfaces.BestFitManagerListener;
import com.xdesign.android.mystrica.interfaces.LogListener;
import com.xdesign.android.mystrica.models.BestFit;
import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.models.LogEntry;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author keithkirk
 */
public class BestFitManager implements OnChartValueSelectedListener, BestFitManagerListener, LogListener {

    private final Set<WeakReference<BestFitManagerListener>> listeners;
    private final Set<Integer> selectedPoints;

    private final Log log;

    private LineChart graph;
    private LogAdapter adapter;

    private boolean seperateEvents;

    public BestFitManager(Log log) {
        this.listeners = new HashSet<>();
        this.selectedPoints = new HashSet<>();

        this.log = log;

        this.log.addListener(this);

        seperateEvents = true;
    }

    public void addListener(BestFitManagerListener listener) {
        this.listeners.add(new WeakReference<>(listener));
    }

    public void setGraph(LineChart graph) {
        this.graph = graph;

        this.graph.setOnChartValueSelectedListener(this);
    }

    public void setLogAdapter(LogAdapter adapter) {
        this.adapter = adapter;
    }

    public void entryClicked(int index) {
        if (GraphUtils.INVALID_INDEX != index) {
            if (selectedPoints.contains(index)) {
                selectedPoints.remove(index);
                adapter.setEnabled(index, false);
            } else {
                selectedPoints.add(index);
                adapter.setEnabled(index, true);
            }

            updateBestFit();
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        final int index = GraphUtils.getIndexFromCustomXIndex(log,
                graph.getData().getDataSetByIndex(dataSetIndex).getYVals().indexOf(e));

        entryClicked(index);
    }

    @Override
    public void onNothingSelected() {
        for (int index : selectedPoints) {
            adapter.setEnabled(index, false);
        }

        selectedPoints.clear();

        updateBestFit();
    }

    @Override
    public void onBestFitUpdated(BestFit bestFit) {
        for (WeakReference<BestFitManagerListener> listener : listeners) {
            if (listener.get() != null) {
                listener.get().onBestFitUpdated(bestFit);
            }
        }
    }

    private void updateBestFit() {
        manageHighlights();
        calculateBestFit();
    }

    private void manageHighlights() {
        if (graph != null) {
            final Highlight[] highlights = new Highlight[selectedPoints.size()];

            int index = 0;
            for (int current : selectedPoints) {
                final int xIndex = graph.getData().getDataSetByIndex(GraphUtils.RESULT_DATA_SET_INDEX)
                        .getYVals().get(GraphUtils.getCustomXIndexFromIndex(log, current)).getXIndex();
                highlights[index] = new Highlight(xIndex,  GraphUtils.RESULT_DATA_SET_INDEX);

                index++;
            }

            graph.highlightValues(highlights);
        }
    }

    private void calculateBestFit() {
        if (graph != null) {
            final BestFit bestFit;

            if (selectedPoints.size() < 2) {
                bestFit = null;
            } else {
                double sumX = 0.0;
                double sumY = 0.0;
                double sumXY = 0.0;
                double sumX2 = 0.0;

                for (int current : selectedPoints) {
                    final LineDataSet dataSet = graph.getLineData()
                            .getDataSetByIndex(GraphUtils.RESULT_DATA_SET_INDEX);

                    final double x = dataSet.getYVals().get(GraphUtils.getCustomXIndexFromIndex(log, current)).getXIndex();
                    final double y = dataSet.getYValForXIndex((int) x);

                    sumX += x;
                    sumY += y;
                    sumXY += x * y;
                    sumX2 += x * x;
                }

                final int n = selectedPoints.size();
                final double meanX = sumX / n;
                final double meanY = sumY / n;
                final double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
                final double intercept = meanY - (slope * meanX);

                bestFit = new BestFit(slope, intercept);
            }

            onBestFitUpdated(bestFit);
        }
    }

    private void examineSeperateEvents() {
        if (seperateEvents) {
            if (!log.isValidForNewX()) {
                seperateEvents = false;

                updateBestFit();
            }
        }
    }

    @Override
    public void onNewEntry(LogEntry entry) {
        examineSeperateEvents();
    }

    @Override
    public void onNewEntries(List<LogEntry> entries) {
        examineSeperateEvents();
    }

    @Override
    public void onLogsCleared() {
        onNothingSelected();
    }

    @Override
    public void onReadingTypeChanged(ReadingType type) {
        updateBestFit();
    }
}
