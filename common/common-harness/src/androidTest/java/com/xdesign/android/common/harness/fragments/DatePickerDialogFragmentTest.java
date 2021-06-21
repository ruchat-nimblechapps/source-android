package com.xdesign.android.common.harness.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.xdesign.android.common.harness.activities.TestActivity;
import com.xdesign.android.common.lib.fragments.DatePickerDialogFragment;
import com.xdesign.android.common.lib.fragments.DatePickerDialogFragment.OnDatePickedListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public final class DatePickerDialogFragmentTest {

    private static final int TIMEOUT = 1000;
    private static final int TIME_DIFFERENCE = 100;

    @Rule
    public final ActivityTestRule<TestActivity> rule = new ActivityTestRule<>(TestActivity.class);

    @Test
    public void setOnDatePickedListener() {
        final OnDatePickedListener listener = mock(OnDatePickedListener.class);
        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setOnDatePickedListener(listener);

        showDialogFragment(fragment);
        fragment.onDateSet(mock(DatePicker.class), 2015, 2, 17);

        verify(listener).onDatePicked(any(Date.class));
    }

    @Test
    public void setOnDatePickedListenerNull() {
        final OnDatePickedListener listener = mock(OnDatePickedListener.class);
        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setOnDatePickedListener(listener);
        fragment.setOnDatePickedListener(null);

        showDialogFragment(fragment);
        fragment.onDateSet(mock(DatePicker.class), 2015, 2, 17);

        verify(listener, never()).onDatePicked(any(Date.class));
    }

    @Test
    public void setOnDatePickedListenerNotInvoked() {
        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();

        showDialogFragment(fragment);
        fragment.onDateSet(mock(DatePicker.class), 2015, 2, 17);
    }

    @Test
    public void setOnDismissListener() {
        final OnDismissListener listener = mock(OnDismissListener.class);
        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setOnDismissListener(listener);

        showDialogFragment(fragment);
        dismissDialogFragment(fragment);

        verify(listener).onDismiss(any(Dialog.class));
    }

    @Test
    public void setOnDismissListenerNull() {
        final DialogInterface.OnDismissListener listener =
                mock(DialogInterface.OnDismissListener.class);
        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setOnDismissListener(listener);
        fragment.setOnDismissListener(null);

        showDialogFragment(fragment);
        dismissDialogFragment(fragment);

        verify(listener, never()).onDismiss(any(Dialog.class));
    }

    @Test
    public void createdWithoutDate() {
        // pre
        final Date now = new Date();
        final OnDatePickedListener listener = mock(OnDatePickedListener.class);
        final DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setOnDatePickedListener(listener);

        // exec
        final DatePickerDialog dialog = showDialogFragment(fragment);
        dialog.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

        // post
        final ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(listener).onDatePicked(captor.capture());
        assertTrue((captor.getValue().getTime() - now.getTime()) < TIME_DIFFERENCE);
    }

    @Test
    public void createdWithDate() {
        // pre
        final Date now = new Date();
        final OnDatePickedListener listener = mock(OnDatePickedListener.class);
        final DatePickerDialogFragment fragment = DatePickerDialogFragment.create(now);
        fragment.setOnDatePickedListener(listener);

        // exec
        final DatePickerDialog dialog = showDialogFragment(fragment);
        dialog.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

        // post
        final ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(listener).onDatePicked(captor.capture());
        assertEquals(now, captor.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createdWithNullDate() {
        DatePickerDialogFragment.create(null);
    }

    @Test
    public void dateChanged() {
        // pre
        final Date now = new Date();
        final OnDatePickedListener listener = mock(OnDatePickedListener.class);
        final DatePickerDialogFragment fragment = DatePickerDialogFragment.create(now);
        fragment.setOnDatePickedListener(listener);

        // exec
        final DatePickerDialog dialog = showDialogFragment(fragment);
        dialog.updateDate(2015, 2, 17);
        dialog.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

        // post
        final ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(listener).onDatePicked(captor.capture());

        final Calendar result = Calendar.getInstance();
        result.setTime(captor.getValue());

        assertEquals(2015, result.get(Calendar.YEAR));
        assertEquals(2, result.get(Calendar.MONTH));
        assertEquals(17, result.get(Calendar.DATE));

        final Calendar expected = Calendar.getInstance();
        expected.setTime(now);
        assertEquals(expected.get(Calendar.HOUR_OF_DAY), result.get(Calendar.HOUR_OF_DAY));
        assertEquals(expected.get(Calendar.MINUTE), result.get(Calendar.MINUTE));
    }

    private DatePickerDialog showDialogFragment(final DialogFragment fragment) {
        fragment.show(rule.getActivity().getSupportFragmentManager(), "TAG");
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<DatePickerDialog> ref = new AtomicReference<>();
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ref.set((DatePickerDialog) fragment.onCreateDialog(null));
                latch.countDown();
            }
        });

        try {
            latch.await(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ref.get();
    }

    private void dismissDialogFragment(DialogFragment fragment) {
        fragment.dismiss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }
}
