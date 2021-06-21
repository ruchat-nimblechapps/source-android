package com.xdesign.android.common.lib.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;

import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public final class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String TAG =
            DatePickerDialogFragment.class.getSimpleName();

    /**
     * {@link long} type.
     */
    private static final String ARGUMENT_TIME = "time";

    private Calendar calendar;
    
    private OnDatePickedListener listener;
    private OnDismissListener dismissListener;

    public static DatePickerDialogFragment create(Date date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null, or use default constructor");
        }

        final Bundle arguments = new Bundle(1);
        arguments.putLong(ARGUMENT_TIME, date.getTime());

        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();
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
        return new DatePickerDialog(
                getActivity(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onDateSet(
            DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DATE, dayOfMonth);

        if (listener != null) {
            listener.onDatePicked(calendar.getTime());
        }
    }

    public void setOnDatePickedListener(OnDatePickedListener listener) {
        this.listener = listener;
    }

    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        dismissListener = listener;
    }

    public interface OnDatePickedListener {

        void onDatePicked(Date date);
    }
}
