package com.xdesign.android.common.harness.fragments;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.harness.activities.TestActivity;
import com.xdesign.android.common.lib.fragments.AlertDialogFragment;
import com.xdesign.android.common.lib.fragments.AlertDialogFragment.Builder;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public final class AlertDialogFragmentTest {

    @Rule
    public final ActivityTestRule<TestActivity> rule = new ActivityTestRule<>(TestActivity.class);

    private View contentStub;
    private TextView contentTextView;
    private Button positiveButton;
    private Button neutralButton;
    private Button negativeButton;

    @After
    public void after() {
        contentStub = null;
        contentTextView = null;
        positiveButton = null;
        neutralButton = null;
        negativeButton = null;
    }

    @Test
    public void constructDefault() {
        final AlertDialogFragment fragment = new AlertDialogFragment();

        showDialogFragment(fragment);
        assertTrue(fragment.isVisible());
        assertTrue(fragment.getDialog().isShowing());

        dismissDialogFragment(fragment);
        assertFalse(fragment.isVisible());
    }

    @Test
    public void testConstructBuilder() {
        final AlertDialogFragment fragment =
                new AlertDialogFragment.Builder(getActivity())
                        .build();

        showDialogFragment(fragment);
        assertTrue(fragment.isVisible());
        assertTrue(fragment.getDialog().isShowing());

        dismissDialogFragment(fragment);
        assertFalse(fragment.isVisible());
    }

    @Test
    public void builderContent() {
        final AlertDialogFragment fragment = new Builder(getActivity())
                .content("Test")
                .build();

        showDialogFragment(fragment);

        assertTrue(contentTextView.isShown());
        assertEquals("Test", contentTextView.getText());

        assertFalse(contentStub.isShown());
        assertFalse(positiveButton.isShown());
        assertFalse(neutralButton.isShown());
        assertFalse(negativeButton.isShown());
    }

    @Test
    public void builderContentInt() {
        final Builder actual = new Builder(getActivity())
                .content(R.string.test);

        assertEquals(
                new Builder(getActivity()).content(getActivity().getText(
                        R.string.test)),
                actual);
    }

    @Test
    public void builderContentLayout() {
        final AlertDialogFragment fragment = new Builder(getActivity())
                .contentLayout(R.layout.test)
                .build();

        showDialogFragment(fragment, R.id.test);

        assertNotNull(contentStub);

        assertFalse(contentTextView.isShown());
        assertFalse(positiveButton.isShown());
        assertFalse(neutralButton.isShown());
        assertFalse(negativeButton.isShown());
    }

    @Ignore
    @Test
    /**
     * Test is not implemented as it would require specifying a resource
     * which would need to be present in the underlying production code.
     */
    public void builderContentWithArguments() {}

    @Test(expected = IllegalArgumentException.class)
    public void builderContentWithArgumentsWrongNumber() {
        new Builder(getActivity())
                .contentLayout(2015, new int[] {1, 2}, new String[] {"A"})
                .build();
    }

    @Test
    public void builderContentReplaced() {
        Builder actual = new Builder(getActivity())
                .content("Test")
                .contentLayout(2015);
        assertEquals(new Builder(getActivity()).contentLayout(2015), actual);
        
        actual = new Builder(getActivity())
                .content("Test")
                .contentLayout(2015, new int[] {1}, new String[] {"A"});
        assertEquals(
                new Builder(getActivity()).contentLayout(
                        2015, new int[] {1}, new String[] {"A"}),
                actual);

        actual = new Builder(getActivity())
                .contentLayout(2015)
                .content("Test");
        assertEquals(new Builder(getActivity()).content("Test"), actual);
    }
    
    @Test
    public void builderCancelableByDefault() {
        final AlertDialogFragment fragment = new Builder(getActivity())
                .build();

        showDialogFragment(fragment);
        
        assertTrue(fragment.isCancelable());
    }

    @Test
    public void builderNotCancelable() {
        final AlertDialogFragment fragment = new Builder(getActivity())
                .cancelable(false)
                .build();

        showDialogFragment(fragment);

        assertFalse(fragment.isCancelable());
    }

    @Test
    public void builderPositiveText() throws Throwable {
        final OnClickListener listener = mock(OnClickListener.class);
        final AlertDialogFragment fragment = new Builder(getActivity())
                .positiveText("Test", listener)
                .build();

        showDialogFragment(fragment);
        assertTrue(positiveButton.isShown());
        assertEquals("Test", positiveButton.getText());
        assertFalse(neutralButton.isShown());
        assertFalse(negativeButton.isShown());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                positiveButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        verify(listener).onClick(
                eq(fragment.getDialog()), eq(DialogInterface.BUTTON_POSITIVE));
    }

    @Test
    public void builderPositiveTextInt() {
        final OnClickListener listener = mock(OnClickListener.class);
        final Builder actual = new Builder(getActivity())
                .positiveText(R.string.test, listener);

        assertEquals(
                new Builder(getActivity()).positiveText(
                        getActivity().getText(R.string.test),
                        listener),
                actual);
    }

    @Test
    public void builderPositiveTextNullListener() throws Throwable {
        final OnClickListener listener = mock(OnClickListener.class);
        final AlertDialogFragment fragment = new Builder(getActivity())
                .positiveText("Test", listener)
                .build();
        fragment.setPositiveListener(null);

        showDialogFragment(fragment);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                positiveButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        verify(listener, never()).onClick(
                any(DialogInterface.class), any(int.class));
    }

    @Test
    public void builderNeutralText() throws Throwable {
        final OnClickListener listener = mock(OnClickListener.class);
        final AlertDialogFragment fragment = new Builder(getActivity())
                .neutralText("Test", listener)
                .build();

        showDialogFragment(fragment);
        assertTrue(neutralButton.isShown());
        assertEquals("Test", neutralButton.getText());
        assertFalse(positiveButton.isShown());
        assertFalse(negativeButton.isShown());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                neutralButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        verify(listener).onClick(
                eq(fragment.getDialog()),
                eq(DialogInterface.BUTTON_NEUTRAL));
    }

    @Test
    public void builderNeutralTextInt() {
        final OnClickListener listener = mock(OnClickListener.class);
        final Builder actual = new Builder(getActivity())
                .neutralText(R.string.test, listener);

        assertEquals(
                new Builder(getActivity()).neutralText(
                        getActivity().getText(R.string.test),
                        listener),
                actual);
    }

    @Test
    public void builderNeutralTextNullListener() throws Throwable {
        final OnClickListener listener = mock(OnClickListener.class);
        final AlertDialogFragment fragment = new Builder(getActivity())
                .neutralText("Test", null)
                .build();
        fragment.setNeutralListener(null);

        showDialogFragment(fragment);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                neutralButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        verify(listener, never()).onClick(
                any(DialogInterface.class), any(int.class));
    }

    @Test
    public void builderNegativeText() throws Throwable {
        final OnClickListener listener = mock(OnClickListener.class);
        final AlertDialogFragment fragment = new Builder(getActivity())
                .negativeText("Test", listener)
                .build();

        showDialogFragment(fragment);
        assertTrue(negativeButton.isShown());
        assertEquals("Test", negativeButton.getText());
        assertFalse(positiveButton.isShown());
        assertFalse(neutralButton.isShown());

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                negativeButton.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();
        verify(listener).onClick(
                eq(fragment.getDialog()),
                eq(DialogInterface.BUTTON_NEGATIVE));
    }

    @Test
    public void builderNegativeTextInt() {
        final OnClickListener listener = mock(OnClickListener.class);
        final Builder actual = new Builder(getActivity())
                .negativeText(R.string.test, listener);

        assertEquals(
                new Builder(getActivity()).negativeText(
                        getActivity().getText(R.string.test),
                        listener),
                actual);
    }

    @Test
    public void builderNegativeTextNullListener() throws Throwable {
        final OnClickListener listener = mock(OnClickListener.class);
        final AlertDialogFragment fragment = new Builder(getActivity())
                .negativeText("Test", null)
                .build();
        fragment.setNegativeListener(null);

        showDialogFragment(fragment);
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                negativeButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        verify(listener, never()).onClick(
                any(DialogInterface.class), any(int.class));
    }
    
    @Test
    public void testCancelableDefault() {
        final AlertDialogFragment fragment =
                new Builder(getActivity()).content("").build();
        
        showDialogFragment(fragment);
        getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();

        assertFalse(fragment.isVisible());
        assertNull(fragment.getDialog());
    }

    @Test
    public void cancelableTrue() {
        final AlertDialogFragment fragment =
                new Builder(getActivity()).content("").cancelable(true).build();

        showDialogFragment(fragment);
        getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();

        assertFalse(fragment.isVisible());
        assertNull(fragment.getDialog());
    }

    @Test
    public void cancelableFalse() {
        final AlertDialogFragment fragment =
                new Builder(getActivity()).content("").cancelable(false).build();

        showDialogFragment(fragment);
        getInstrumentation().sendCharacterSync(KeyEvent.KEYCODE_BACK);
        getInstrumentation().waitForIdleSync();

        assertTrue(fragment.isVisible());
        assertTrue(fragment.getDialog().isShowing());
    }

    private void showDialogFragment(DialogFragment fragment) {
        showDialogFragment(fragment, com.xdesign.android.common.lib.R.id.alert_dialog_content_stub);
    }

    private void showDialogFragment(DialogFragment fragment, @IdRes int stubId) {
        fragment.show(
                getActivity().getSupportFragmentManager(),
                AlertDialogFragment.TAG);
        getInstrumentation().waitForIdleSync();

        contentStub = fragment.getView().findViewById(stubId);
        contentTextView = (TextView) fragment.getView().findViewById(
                com.xdesign.android.common.lib.R.id.alert_dialog_content_text);
        positiveButton = (Button) fragment.getView().findViewById(
                com.xdesign.android.common.lib.R.id.alert_dialog_positive_button);
        neutralButton = (Button) fragment.getView().findViewById(
                com.xdesign.android.common.lib.R.id.alert_dialog_neutral_button);
        negativeButton = (Button) fragment.getView().findViewById(
                com.xdesign.android.common.lib.R.id.alert_dialog_negative_button);
    }

    private void dismissDialogFragment(DialogFragment fragment) {
        fragment.dismiss();
        getInstrumentation().waitForIdleSync();
    }

    private TestActivity getActivity() {
        return rule.getActivity();
    }

    private static Instrumentation getInstrumentation() {
        return InstrumentationRegistry.getInstrumentation();
    }
}
