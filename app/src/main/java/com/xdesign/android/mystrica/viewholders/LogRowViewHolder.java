package com.xdesign.android.mystrica.viewholders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdesign.android.common.lib.viewholders.AbstractViewHolder;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.models.LogEntry;
import com.xdesign.android.mystrica.util.DisplayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author keithkirk
 */
public class LogRowViewHolder extends AbstractViewHolder<LogEntry> {

    @BindView(R.id.myst_view_background)
    protected View background;

    @BindView(R.id.myst_text_view_column_one)
    protected TextView columnOneTextView;

    @BindView(R.id.myst_text_view_column_two)
    protected TextView columnTwoTextView;

    @BindView(R.id.myst_text_view_column_three)
    protected TextView columnThreeTextView;

    @BindView(R.id.myst_text_view_column_four)
    protected TextView columnFourTextView;

    @BindView(R.id.myst_layout_selection)
    protected View selectionLayer;

    private final Context context;

    public LogRowViewHolder(Context context) {
        this.context = context;
    }

    public View createView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayout(), parent, false);
        ButterKnife.bind(this, view);
        view.setTag(this);
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.myst_view_log_row;
    }

    @Override
    public void setup(LogEntry log) {
        background.setBackgroundResource(log.getColour().getBackgroundResource());

        columnOneTextView.setText(DisplayUtils.getDisplayableTime(context, log.getSeconds()));
        columnTwoTextView.setText(DisplayUtils.getPercentage(context, log.getTransmittance()));
        columnThreeTextView.setText(DisplayUtils.getRounded(context, log.getAbsorbance()));
        columnFourTextView.setText(log.getNote());

        selectionLayer.setVisibility(log.isEnabled() ? View.VISIBLE : View.INVISIBLE);
    }
}
