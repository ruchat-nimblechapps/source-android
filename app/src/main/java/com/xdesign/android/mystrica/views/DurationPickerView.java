package com.xdesign.android.mystrica.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.models.Duration;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author keithkirk
 */
public class DurationPickerView extends RelativeLayout {

    @BindView(R.id.myst_number_picker_hour)
    protected NumberPicker hoursPicker;

    @BindView(R.id.myst_number_picker_minute)
    protected NumberPicker minutesPicker;

    @BindView(R.id.myst_number_picker_second)
    protected NumberPicker secondsPicker;

    public DurationPickerView(Context context) {
        this(context, null);
    }

    public DurationPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DurationPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DurationPickerView(Context context, AttributeSet attrs, int defStyleAttr,
                              int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.myst_view_duration_picker, this);

        ButterKnife.bind(this);

        hoursPicker.setMinValue(Duration.MIN_HOURS);
        hoursPicker.setMaxValue(Duration.MAX_HOURS);

        minutesPicker.setMinValue(Duration.MIN_MINUTES);
        minutesPicker.setMaxValue(Duration.MAX_MINUTES);

        secondsPicker.setMinValue(Duration.MIN_SECONDS);
        secondsPicker.setMaxValue(Duration.MAX_SECONDS);
    }

    public Duration getSelectedDuration() {
        return new Duration(
                hoursPicker.getValue(),
                minutesPicker.getValue(),
                secondsPicker.getValue()
        );
    }
}
