package com.xdesign.android.common.harness.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.xdesign.android.common.harness.activities.TestActivity;
import com.xdesign.android.common.lib.fragments.TimePickerDialogFragment;
import com.xdesign.android.common.lib.fragments.TimePickerDialogFragment.OnTimePickedListener;

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
public final class TimePickerDialogFragmentTest {

    private static final int TIMEOUT = 1000;
    private static final int TIME_DIFFERENCE = 100;

    @Rule
    public final ActivityTestRule<TestActivity> rule = new ActivityTestRule<>(TestActivity.class);

    @Test
    public void setOnTimePickedListener() {
        final OnTimePickedListener listener = mock(OnTimePickedListener.class);
        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setOnTimePickedListener(listener);

        showDialogFragment(fragment);
        fragment.onTimeSet(mock(TimePicker.class), 16, 45);

        verify(listener).onTimePicked(any(Date.class));
    }

    @Test
    public void setOnTimePickedListenerNull() {
        final OnTimePickedListener listener = mock(OnTimePickedListener.class);
        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setOnTimePickedListener(listener);
        fragment.setOnTimePickedListener(null);

        showDialogFragment(fragment);
        fragment.onTimeSet(mock(TimePicker.class), 16, 45);

        verify(listener, never()).onTimePicked(any(Date.class));
    }
    
    @Test
    public void setOnDismissListener() {
        final OnDismissListener listener = mock(OnDismissListener.class);
        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setOnDismissListener(listener);

        showDialogFragment(fragment);
        dismissDialogFragment(fragment);
        
        verify(listener).onDismiss(any(Dialog.class));
    }

    @Test
    public void setOnDismissListenerNull() {
        final OnDismissListener listener = mock(OnDismissListener.class);
        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
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
        final OnTimePickedListener listener = mock(OnTimePickedListener.class);
        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setOnTimePickedListener(listener);

        // exec
        final TimePickerDialog dialog = showDialogFragment(fragment);
        dialog.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

        // post
        final ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(listener).onTimePicked(captor.capture());
        assertTrue((captor.getValue().getTime() - now.getTime()) < TIME_DIFFERENCE);
    }

    @Test
    public void createdWithDate() {
        // pre
        final Date now = new Date();
        final OnTimePickedListener listener = mock(OnTimePickedListener.class);
        final TimePickerDialogFragment fragment = TimePickerDialogFragment.create(now);
        fragment.setOnTimePickedListener(listener);

        // exec
        final TimePickerDialog dialog = showDialogFragment(fragment);
        dialog.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

        // post
        final ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(listener).onTimePicked(captor.capture());
        assertEquals(now, captor.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createdWithNullDate() {
        TimePickerDialogFragment.create(null);
    }

    @Test
    public void timeChanged() {
        // pre
        final Date now = new Date();
        final OnTimePickedListener listener = mock(OnTimePickedListener.class);
        final TimePickerDialogFragment fragment = TimePickerDialogFragment.create(now);
        fragment.setOnTimePickedListener(listener);

        // exec
        final TimePickerDialog dialog = showDialogFragment(fragment);
        dialog.updateTime(16, 45);
        dialog.onClick(dialog, DialogInterface.BUTTON_POSITIVE);

        // post
        final ArgumentCaptor<Date> captor = ArgumentCaptor.forClass(Date.class);
        verify(listener).onTimePicked(captor.capture());

        final Calendar result = Calendar.getInstance();
        result.setTime(captor.getValue());

        assertEquals(16, result.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, result.get(Calendar.MINUTE));

        final Calendar expected = Calendar.getInstance();
        expected.setTime(now);
        assertEquals(expected.get(Calendar.YEAR), result.get(Calendar.YEAR));
        assertEquals(expected.get(Calendar.MONTH), result.get(Calendar.MONTH));
        assertEquals(expected.get(Calendar.DATE), result.get(Calendar.DATE));
    }

    private TimePickerDialog showDialogFragment(final DialogFragment fragment) {
        fragment.show(rule.getActivity().getSupportFragmentManager(), "TAG");
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<TimePickerDialog> ref = new AtomicReference<>();
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                ref.set((TimePickerDialog) fragment.onCreateDialog(null));
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
