package com.xdesign.android.mystrica.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;

/**
 * @author keithkirk
 */
public class CustomLineChart extends LineChart {

    public CustomLineChart(Context context) {
        super(context);
    }

    public CustomLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * Moves the description to the top left
     */
    @Override
    protected void drawDescription(Canvas c) {
        setDescriptionPosition(getViewPortHandler().offsetLeft() + 10,
                getViewPortHandler().offsetTop() + 10);
        mDescPaint.setTextAlign(Paint.Align.LEFT);
        super.drawDescription(c);
    }


}
