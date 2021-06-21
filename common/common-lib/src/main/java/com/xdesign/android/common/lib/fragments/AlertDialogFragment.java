package com.xdesign.android.common.lib.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.xdesign.android.common.lib.R;

import java.util.Arrays;
import java.util.Locale;

import static com.xdesign.android.common.lib.utils.ObjectUtil.safeEquals;

/**
 * {@link DialogFragment} which allows for styling of the buttons and content
 * and displays without a title (like an {@link android.app.AlertDialog}).
 * <p>
 * Construction should be done using {@link Builder}. It is up to the caller to
 * dismiss the dialog when {@link OnClickListener}s are set and invoked for a
 * particular button type.
 */
public final class AlertDialogFragment extends DialogFragment {

    public static final String TAG = AlertDialogFragment.class.getSimpleName();

    private static final String ARGUMENT_CONTENT_TEXT =
            "content_text"; // CharSequence
    private static final String ARGUMENT_CONTENT_LAYOUT_RES_ID =
            "content_layout_res_id"; // int
    private static final String ARGUMENT_CONTENT_LAYOUT_IDS =
            "content_layout_ids"; // int[]
    private static final String ARGUMENT_CONTENT_LAYOUT_TEXTS =
            "content_layout_texts"; // CharSequence[]
    private static final String ARGUMENT_CANCELABLE =
            "cancelable"; // boolean
    private static final String ARGUMENT_POSITIVE_TEXT =
            "positive_text"; // CharSequence
    private static final String ARGUMENT_NEUTRAL_TEXT =
            "neutral_text"; // CharSequence
    private static final String ARGUMENT_NEGATIVE_TEXT =
            "negative_text"; // CharSequence

    private CharSequence contentText;
    private int contentLayoutResId;
    private int[] contentLayoutIds;
    private CharSequence[] contentLayoutTexts;

    private boolean cancelable;

    private CharSequence positiveText;
    private CharSequence neutralText;
    private CharSequence negativeText;
    private OnClickListener positiveListener;
    private OnClickListener neutralListener;
    private OnClickListener negativeListener;

    private ViewStub contentStub;
    private TextView contentTextView;
    private Button positiveButton;
    private Button neutralButton;
    private Button negativeButton;

    public AlertDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            contentText = getArguments().getCharSequence(ARGUMENT_CONTENT_TEXT);
            contentLayoutResId = getArguments().getInt(
                    ARGUMENT_CONTENT_LAYOUT_RES_ID);
            contentLayoutIds = getArguments().getIntArray(
                    ARGUMENT_CONTENT_LAYOUT_IDS);
            contentLayoutTexts = getArguments().getCharSequenceArray(
                    ARGUMENT_CONTENT_LAYOUT_TEXTS);

            cancelable = getArguments().getBoolean(ARGUMENT_CANCELABLE);

            positiveText = getArguments().getCharSequence(ARGUMENT_POSITIVE_TEXT);
            neutralText = getArguments().getCharSequence(ARGUMENT_NEUTRAL_TEXT);
            negativeText = getArguments().getCharSequence(ARGUMENT_NEGATIVE_TEXT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);

        // hide the title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (getDialog() == null) {
            setShowsDialog(false);
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_alert_dialog, container, false);

        contentStub =
                (ViewStub) view.findViewById(R.id.alert_dialog_content_stub);
        contentTextView =
                (TextView) view.findViewById(R.id.alert_dialog_content_text);
        positiveButton =
                (Button) view.findViewById(R.id.alert_dialog_positive_button);
        neutralButton =
                (Button) view.findViewById(R.id.alert_dialog_neutral_button);
        negativeButton =
                (Button) view.findViewById(R.id.alert_dialog_negative_button);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (contentLayoutResId != 0) {
            contentStub.setLayoutResource(contentLayoutResId);

            final View stubView = contentStub.inflate();

            if (contentLayoutIds != null && contentLayoutIds.length > 0) {
                for (int i = 0; i < contentLayoutIds.length; i++) {
                    final View viewWithId = stubView.findViewById(
                            contentLayoutIds[i]);

                    if (viewWithId instanceof TextView) {
                        ((TextView) viewWithId).setText(contentLayoutTexts[i]);
                    } else {
                        Log.w(TAG, String.format(
                                Locale.ENGLISH,
                                "View with id %1$d has type %2$s instead of expected TextView",
                                contentLayoutIds[i],
                                viewWithId.getClass().getSimpleName()));
                    }
                }
            }

            contentStub.setVisibility(View.VISIBLE);
        } else {
            contentTextView.setVisibility(View.VISIBLE);
            contentTextView.setText(contentText);
        }

        setCancelable(cancelable);

        if (positiveText != null) {
            positiveButton.setVisibility(View.VISIBLE);
            positiveButton.setText(positiveText);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (positiveListener != null) {
                        positiveListener.onClick(
                                getDialog(), DialogInterface.BUTTON_POSITIVE);
                    } else {
                        dismiss();
                    }
                }
            });
        }
        if (neutralText != null) {
            neutralButton.setVisibility(View.VISIBLE);
            neutralButton.setText(neutralText);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (neutralListener != null) {
                        neutralListener.onClick(
                                getDialog(), DialogInterface.BUTTON_NEUTRAL);
                    } else {
                        dismiss();
                    }
                }
            });
        }
        if (negativeText != null) {
            negativeButton.setVisibility(View.VISIBLE);
            negativeButton.setText(negativeText);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (negativeListener != null) {
                        negativeListener.onClick(
                                getDialog(), DialogInterface.BUTTON_NEGATIVE);
                    } else {
                        dismiss();
                    }
                }
            });
        }
    }

    public void setPositiveListener(@Nullable OnClickListener listener) {
        positiveListener = listener;
    }

    public void setNeutralListener(@Nullable OnClickListener listener) {
        neutralListener = listener;
    }

    public void setNegativeListener(@Nullable OnClickListener listener) {
        negativeListener = listener;
    }

    public static class Builder {

        private final Resources resources;

        private CharSequence contentText;
        private int contentLayoutResId;
        private int[] contentLayoutIds;
        private CharSequence[] contentLayoutTexts;

        private boolean cancelable = true;

        private CharSequence positiveText;
        private CharSequence neutralText;
        private CharSequence negativeText;
        private OnClickListener positiveListener;
        private OnClickListener neutralListener;
        private OnClickListener negativeListener;

        public Builder(Context context) {
            resources = context.getResources();
        }

        public Builder content(CharSequence text) {
            contentText = text;
            contentLayoutResId = 0;
            return this;
        }

        public Builder content(int textId) {
            return content(resources.getText(textId));
        }

        public Builder contentLayout(int resId) {
            contentLayoutResId = resId;
            contentText = null;
            return this;
        }

        public Builder contentLayout(int resId, int[] ids, CharSequence[] texts) {
            if (ids.length != texts.length) {
                throw new IllegalArgumentException(
                        "Length of ids and texts needs to match");
            }

            contentLayoutResId = resId;
            contentLayoutIds = ids;
            contentLayoutTexts = texts;
            contentText = null;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder positiveText(
                CharSequence text, @Nullable OnClickListener listener) {

            positiveText = text;
            positiveListener = listener;
            return this;
        }

        public Builder positiveText(
                int textId, @Nullable OnClickListener listener) {

            return positiveText(resources.getText(textId), listener);
        }

        public Builder neutralText(
                CharSequence text, @Nullable OnClickListener listener) {

            neutralText = text;
            neutralListener = listener;
            return this;
        }

        public Builder neutralText(
                int textId, @Nullable OnClickListener listener) {

            return neutralText(resources.getText(textId), listener);
        }

        public Builder negativeText(CharSequence text, OnClickListener listener) {
            negativeText = text;
            negativeListener = listener;
            return this;
        }

        public Builder negativeText(
                int textId, @Nullable OnClickListener listener) {

            return negativeText(resources.getText(textId), listener);
        }

        public AlertDialogFragment build() {
            final Bundle arguments = new Bundle(9);
            arguments.putCharSequence(ARGUMENT_CONTENT_TEXT, contentText);
            arguments.putInt(ARGUMENT_CONTENT_LAYOUT_RES_ID, contentLayoutResId);
            arguments.putIntArray(ARGUMENT_CONTENT_LAYOUT_IDS, contentLayoutIds);
            arguments.putCharSequenceArray(
                    ARGUMENT_CONTENT_LAYOUT_TEXTS, contentLayoutTexts);

            arguments.putBoolean(ARGUMENT_CANCELABLE, cancelable);

            arguments.putCharSequence(ARGUMENT_POSITIVE_TEXT, positiveText);
            arguments.putCharSequence(ARGUMENT_NEUTRAL_TEXT, neutralText);
            arguments.putCharSequence(ARGUMENT_NEGATIVE_TEXT, negativeText);

            final AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.setArguments(arguments);
            dialog.setPositiveListener(positiveListener);
            dialog.setNeutralListener(neutralListener);
            dialog.setNegativeListener(negativeListener);

            return dialog;
        }

        @SuppressWarnings("checkstyle:cyclomaticcomplexity")
        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof Builder)) {
                return false;
            }

            final Builder other = (Builder) o;
            return safeEquals(resources, other.resources)
                    && safeEquals(contentText, other.contentText)
                    && contentLayoutResId == other.contentLayoutResId
                    && Arrays.equals(contentLayoutIds, other.contentLayoutIds)
                    && Arrays.equals(contentLayoutTexts, other.contentLayoutTexts)
                    && cancelable == other.cancelable
                    && safeEquals(positiveText, other.positiveText)
                    && safeEquals(neutralText, other.neutralText)
                    && safeEquals(negativeText, other.negativeText)
                    && safeEquals(positiveListener, other.positiveListener)
                    && safeEquals(neutralListener, other.neutralListener)
                    && safeEquals(negativeListener, other.negativeListener);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[]{
                    resources,
                    contentText,
                    contentLayoutResId,
                    contentLayoutIds,
                    contentLayoutTexts,
                    cancelable,
                    positiveText,
                    neutralText,
                    negativeText,
                    positiveListener,
                    neutralListener,
                    negativeListener});
        }
    }
}
