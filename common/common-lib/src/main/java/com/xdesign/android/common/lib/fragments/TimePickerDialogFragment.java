package com.xdesign.android.common.lib.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public final class TimePickerDialogFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG =
            TimePickerDialogFragment.class.getSimpleName();

    /**
     * {@link long} type.
     */
    private static final String ARGUMENT_TIME = "time";

    private Calendar calendar;
    
    private OnTimePickedListener listener;
    private OnDismissListener dismissListener;

    public static TimePickerDialogFragment create(Date date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null, or use default constructor");
        }

        final Bundle arguments = new Bundle(1);
        arguments.putLong(ARGUMENT_TIME, date.getTime());

        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();

        final Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ARGUMENT_TIME)) {
            calendar.setTimeInMillis(arguments.getLong(ARGUMENT_TIME));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(
                getActivity(),
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        if (listener != null) {
            listener.onTimePicked(calendar.getTime());
        }
    }

    public void setOnTimePickedListener(OnTimePickedListener listener) {
        this.listener = listener;
    }
    
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        dismissListener = listener;
    }

    public interface OnTimePickedListener {

        void onTimePicked(Date date);
    }
}
