package com.xdesign.android.mystrica.adapters;

import android.content.Context;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.xdesign.android.common.lib.utils.ExceptionLogger;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.interfaces.BestFitManagerListener;
import com.xdesign.android.mystrica.interfaces.LogListener;
import com.xdesign.android.mystrica.models.BestFit;
import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.models.LogEntry;
import com.xdesign.android.mystrica.util.AbsorbanceFormatter;
import com.xdesign.android.mystrica.util.DisplayUtils;
import com.xdesign.android.mystrica.util.GraphUtils;
import com.xdesign.android.mystrica.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author keithkirk
 */
public class LineGraphAdapter implements LogListener, BestFitManagerListener {

    //TODO Improve efficiency by not rebuilding the data set each time
    // Add new values to the set and only rebuild when swapping from a custom X axi to time
    // ie. when the log has suddenly become invalid for new axis.

    private final Context context;
    private final LineChart graph;
    private final Log log;

    private BestFit bestFit;

    public LineGraphAdapter(Context context, LineChart graph, Log log) {
        this.context = context;
        this.graph = graph;
        this.log = log;

        log.addListener(this);

        graph.getLegend().setEnabled(false);
        graph.getAxisRight().setEnabled(false);
        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        setData();
    }

    @Override
    public void onNewEntry(LogEntry entry) {
        setData();
    }

    @Override
    public void onNewEntries(List<LogEntry> entries) {
        setData();
    }

    @Override
    public void onLogsCleared() {
        setData();
    }

    @Override
    public void onReadingTypeChanged(ReadingType type) {
        setData();
    }

    @Override
    public void onBestFitUpdated(BestFit bestFit) {
        this.bestFit = bestFit;

        setData();
    }

    private void setData() {
        final LineData data = buildLineData();
        if (data != null) {
            graph.setData(data);
            graph.setDescription(getDescription());
        } else {
            graph.setDescription(context.getResources().getString(R.string.myst_label_graph_update_problem));
        }
        graph.getAxisLeft().setValueFormatter(getYAxisFormatter());
        graph.getAxisLeft().setLabelCount(GraphUtils.TOTAL_LABELS, false);
        graph.invalidate();
    }

    private LineData buildLineData() {
        final List<LogEntry> entries = GraphUtils.entriesFromLog(log);

        final List<String> xVals = buildXVals(entries);
        final LineDataSet yVals = buildYVals(entries);
        final LineDataSet bestFit = buildBestFit(xVals);

        final List<LineDataSet> dataSets = new ArrayList<>(2);
        dataSets.add(yVals);
        dataSets.add(bestFit);

        if (yVals.getYVals().size() > xVals.size() || bestFit.getYVals().size() > xVals.size()) {
            final String message = "Problem building graph data: xVals size: " + xVals.size() + ", yVals size: " + yVals.getYVals().size() + ", bestFit size: " + bestFit.getYVals().size();
            ExceptionLogger.log(new Exception(message), message);

            return null;
        } else {
            return new LineData(xVals, dataSets);
        }
    }

    /*
     * labelNum : How many labels to display on the x axis
     * gapNumber: How many gaps occur, the spaces between labels
     * scaleFactor : Multiple of gapCapacity to handle long decimals
     * gapCapacity : How many labels will occur fit inside each gap, gaps are uniform size
     *
     * A label is created for each number between min and max, max is scaled up if using the natural
     * max leads to a non whole number for the gapCapacity.
     */
    private List<String> buildXVals(List<LogEntry> entries) {
        final List<String> vals = new ArrayList<>();

        graph.getXAxis().resetLabelsToSkip();

        final int labelNum = entries.size() < GraphUtils.TOTAL_LABELS ? entries.size() : GraphUtils.TOTAL_LABELS;

        if (labelNum > 0) {
            final int gapNumber = labelNum - 1;
            final int scaleFactor = GraphUtils.getScalingFactor(log.getMaxDecimalPlaces());

            final long min = (long) (GraphUtils.getXVal(entries.get(0), log) * scaleFactor);
            long max = (long) (GraphUtils.getXVal(entries.get(entries.size() - 1), log) * scaleFactor);

            if (gapNumber > 0) {
                double tempGapSize = ((max - min) * 1.0) / gapNumber;

                if (!GraphUtils.isWhole(tempGapSize)) {
                    max = (long) ((Math.ceil(tempGapSize) * gapNumber) + min);
                }

                final int gapCapacity = (int)(Math.ceil(tempGapSize) - 1);

                graph.getXAxis().setLabelsToSkip(gapCapacity);
            }

            try {
                for (long label = min ; label <= max ; label++) {
                    vals.add(formatValue((label * 1.0) / scaleFactor));
                }
            } catch (OutOfMemoryError e) {
                vals.clear();

                ExceptionLogger.log(e);
            }
        }

        return vals;
    }

    private LineDataSet buildYVals(List<LogEntry> entries) {
        final List<Entry> dataPoints = new ArrayList<>(entries.size());
        final List<Integer> colours = new ArrayList<>(entries.size());

        for (LogEntry entry : entries) {
            final double value = GraphUtils.getYVal(entry, log);

            final Entry dataPoint = new Entry((float) Utils.round(value, GraphUtils.DECIMAL_PLACES),
                    ((int) GraphUtils.getOffsettedXVal(entry, log)));

            dataPoints.add(dataPoint);

            colours.add(context.getResources().getColor(entry.getColour().getLineResource()));
        }

        final LineDataSet lineDataSet = new LineDataSet(dataPoints, "");

        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColors(colours);
        lineDataSet.setCircleColors(colours);
        lineDataSet.setHighLightColor(context.getResources().getColor(R.color.myst_light_purple));

        return lineDataSet;
    }

    private LineDataSet buildBestFit(List<String> labels) {
        final List<Entry> dataPoints = new ArrayList<>(2);

        if (bestFit != null && labels.size() > 2) {
            final double startX = log.isValidForNewX()
                    ? Double.parseDouble(labels.get(0))
                    : Utils.parseTime(labels.get(0));

            final double endX = log.isValidForNewX()
                    ? Double.parseDouble(labels.get(labels.size() - 1))
                    : Utils.parseTime(labels.get(labels.size() - 1));

            final Entry start = new Entry((float) Utils.round(bestFit.getY(startX), GraphUtils.DECIMAL_PLACES), (int) startX);
            final Entry end = new Entry((float) Utils.round(bestFit.getY(endX), GraphUtils.DECIMAL_PLACES), (int) endX);

            dataPoints.add(start);
            dataPoints.add(end);
        }

        final LineDataSet lineDataSet = new LineDataSet(dataPoints, "");

        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(context.getResources().getColor(R.color.myst_orange));
        lineDataSet.setCircleColor(context.getResources().getColor(R.color.myst_orange));

        return lineDataSet;
    }

    private String getDescription() {
        switch (log.getCurrentReadingType()) {
            case T:
                return context.getResources().getString(R.string.myst_label_transmittance);
            case A:
                return context.getResources().getString(R.string.myst_label_absorbance);
            default:
                return "";
        }
    }

    private YAxisValueFormatter getYAxisFormatter() {
        switch (log.getCurrentReadingType()) {
            case T:
                return new PercentFormatter();
            case A:
                return new AbsorbanceFormatter(context);
            default:
                return null;
        }
    }

    private String formatValue(double value) {
        return log.isValidForNewX()
                ? String.valueOf(value)
                : DisplayUtils.getDisplayableTime(context, (long) value);
    }
}
