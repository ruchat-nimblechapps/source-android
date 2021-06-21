package com.xdesign.android.mystrica.util;

import android.content.Context;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.xdesign.android.mystrica.R;

/**
 * @author keithkirk
 */
public class AbsorbanceFormatter implements ValueFormatter, YAxisValueFormatter {

    private final Context context;

    public AbsorbanceFormatter(Context context) {
        this.context = context;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return context.getResources().getString(R.string.myst_format_three_dp, value);
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return context.getResources().getString(R.string.myst_format_three_dp, value);
    }
}
