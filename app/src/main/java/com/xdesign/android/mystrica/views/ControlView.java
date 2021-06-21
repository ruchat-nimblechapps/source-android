package com.xdesign.android.mystrica.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.adapters.SessionAdapter;
import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.enums.SamplingRate;
import com.xdesign.android.mystrica.interfaces.Communicator;
import com.xdesign.android.mystrica.interfaces.CommunicatorListener;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.util.DisplayUtils;
import com.xdesign.android.mystrica.util.FileManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * @author keithkirk
 */
public class ControlView extends RelativeLayout implements CommunicatorListener {

    @BindView(R.id.myst_radio_button_r)
    protected RadioButton rRadioButton;

    @BindView(R.id.myst_radio_button_g)
    protected RadioButton gRadioButton;

    @BindView(R.id.myst_radio_button_b)
    protected RadioButton bRadioButton;

    @BindView(R.id.myst_radio_button_t)
    protected RadioButton tRadioButton;

    @BindView(R.id.myst_radio_button_a)
    protected RadioButton aRadioButton;

    @BindView(R.id.myst_text_view_reading)
    protected TextView readingTextView;

    @BindView(R.id.myst_button_calibrate)
    protected TextView calibrateButton;

    @BindView(R.id.myst_edit_text_note)
    protected EditText viewNote;

    @BindView(R.id.myst_button_single_capture)
    protected ImageView singleCaptureButton;

    @BindView(R.id.myst_image_view_start_stop)
    protected ImageView startStopButton;

    @BindView(R.id.myst_button_save)
    protected View viewSave;

    @BindView(R.id.myst_text_view_save)
    protected TextView textViewSave;

    @BindView(R.id.myst_button_sessions)
    protected View viewSessions;

    @BindView(R.id.myst_text_view_sessions)
    protected TextView textViewSessions;

    @BindView(R.id.myst_button_clear)
    protected View viewClear;

    @BindView(R.id.myst_text_view_clear)
    protected TextView textViewClear;

    private FileManager fileManager;

    private Communicator communicator;

    private ListAdapter sessionAdapter;
    private File selectedSession;

    private DialogInterface.OnClickListener negativeClick
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener sessionClickListener
            = new DialogInterface.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(DialogInterface dialog, int which) {
            selectedSession = (File) sessionAdapter.getItem(which);

            dialog.dismiss();

            if (communicator.getLog().isSaved()) {
                fileManager.loadSession(selectedSession);
            } else {
                showWarningDialog(positiveClickLoad);
            }
        }
    };

    private DialogInterface.OnClickListener positiveClickLoad
            = new DialogInterface.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(DialogInterface dialog, int which) {
            fileManager.loadSession(selectedSession);

            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener positiveClickClear
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (communicator != null) {
                communicator.clearLogs();
            }
            dialog.dismiss();
        }
    };

    public ControlView(Context context) {
        this(context, null);
    }

    public ControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ControlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.myst_view_control, this);

        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;

        if (communicator != null) {
            if (communicator.isCapturing()) {
                onCaptureStarted();
            } else {
                onCaptureStopped();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;

        if (communicator != null) {
            communicator.addListener(this);

            onDeviceConnection(communicator.getConnectedDevice());

            setupColourButtons(communicator.getColour());
            setupReadingTypeButtons(communicator.getReadingType());
            setupReadingText();

            if (communicator.isCapturing()) {
                onCaptureStarted();
            } else {
                onCaptureStopped();
            }
        }
    }

    private void setupColourButtons(Colour colour) {
        if (communicator.getConnectedDevice() != null) {
            switch (colour) {
                case RED:
                    rRadioButton.setChecked(true);
                    break;
                case GREEN:
                    gRadioButton.setChecked(true);
                    break;
                case BLUE:
                    bRadioButton.setChecked(true);
                    break;
            }
        } else {
            rRadioButton.setChecked(false);
            gRadioButton.setChecked(false);
            bRadioButton.setChecked(false);
        }
    }

    private void setupReadingTypeButtons(ReadingType readingType) {
        switch(readingType) {
            case T:
                tRadioButton.setChecked(true);
                break;
            case A:
                aRadioButton.setChecked(true);
                break;
        }
    }

    private void setupReadingText() {
        if (communicator.getConnectedDevice() != null) {
            switch (communicator.getReadingType()) {
                case T:
                    readingTextView.setText(DisplayUtils.getPercentage(getContext(),
                            communicator.getCurrentTransmittance()));
                    break;
                case A:
                    readingTextView.setText(DisplayUtils.getRounded(getContext(),
                            communicator.getCurrentAbsorbance()));
                    break;
            }
        } else {
            readingTextView.setText(R.string.myst_label_no_reading);
        }
    }

    @Override
    public void onDeviceConnection(Device device) {
        setupReadingTypeButtons(communicator.getReadingType());
        setupColourButtons(communicator.getColour());
        setupReadingText();

        rRadioButton.setEnabled(device != null);
        gRadioButton.setEnabled(device != null);
        bRadioButton.setEnabled(device != null);
        calibrateButton.setEnabled(device != null);
        calibrateButton.setTextColor(getContext().getResources().getColor(device != null
                ? R.color.myst_text_green
                : R.color.myst_text_grey));
        viewNote.setEnabled(device != null);
        singleCaptureButton.setEnabled(device != null);
        singleCaptureButton.setImageResource(device != null
                ? R.drawable.myst_button_capture
                : R.drawable.myst_button_capture_disabled);
        startStopButton.setEnabled(device != null);
        startStopButton.setImageResource(device != null
                ? R.drawable.myst_button_rec
                : R.drawable.myst_button_rec_disabled);
    }

    @Override
    public void onCaptureStarted() {
        startStopButton.setImageResource(R.drawable.myst_button_pause);

        viewSave.setEnabled(false);
        textViewSave.setTextColor(getContext().getResources().getColor(R.color.myst_text_grey));
        viewSessions.setEnabled(false);
        textViewSessions.setTextColor(getContext().getResources().getColor(R.color.myst_text_grey));
        viewClear.setEnabled(false);
        textViewClear.setTextColor(getContext().getResources().getColor(R.color.myst_text_grey));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCaptureStopped() {
        startStopButton.setImageResource(R.drawable.myst_button_rec);

        viewSave.setEnabled(fileManager != null && fileManager.canDataBeWritten());
        textViewSave.setTextColor(getContext().getResources().getColor(
                fileManager != null && fileManager.canDataBeWritten()
                ? R.color.myst_text_green
                : R.color.myst_text_grey));
        viewSessions.setEnabled(fileManager != null && fileManager.canDataBeRead());
        textViewSessions.setTextColor(getContext().getResources().getColor(
                fileManager != null && fileManager.canDataBeRead()
                ? R.color.myst_text_green
                : R.color.myst_text_grey));
        viewClear.setEnabled(true);
        textViewClear.setTextColor(getContext().getResources().getColor(R.color.myst_text_green));
    }

    @Override
    public void onTransmittanceUpdated() {
        setupReadingText();
    }

    @Override
    public void onAbsorbanceUpdated() {
        setupReadingText();
    }

    @Override
    public void onSamplingRateChanged(SamplingRate samplingRate) {

    }

    @Override
    public void onColourChanged(Colour colour) {
        rRadioButton.setChecked(Colour.RED.equals(colour));
        gRadioButton.setChecked(Colour.GREEN.equals(colour));
        bRadioButton.setChecked(Colour.BLUE.equals(colour));

        setupReadingText();
    }

    @Override
    public void onReadingTypeChanged(ReadingType readingType) {
        aRadioButton.setChecked(ReadingType.A.equals(readingType));
        tRadioButton.setChecked(ReadingType.T.equals(readingType));

        setupReadingText();
    }

    @Override
    public void onNoteChanged(CharSequence note) {

    }

    @OnCheckedChanged(R.id.myst_radio_button_r)
    protected void rRadioButtonChanged(boolean checked) {
        if (checked) {
            if (communicator != null) {
                communicator.setColour(Colour.RED);
            }
        }
    }

    @OnCheckedChanged(R.id.myst_radio_button_g)
    protected void gRadioButtonChanged(boolean checked) {
        if (checked) {
            if (communicator != null) {
                communicator.setColour(Colour.GREEN);
            }
        }
    }

    @OnCheckedChanged(R.id.myst_radio_button_b)
    protected void bRadioButtonChanged(boolean checked) {
        if (checked) {
            if (communicator != null) {
                communicator.setColour(Colour.BLUE);
            }
        }
    }

    @OnCheckedChanged(R.id.myst_radio_button_t)
    protected void tRadioButtonChanged(boolean checked) {
        if (checked) {
            if (communicator != null) {
                communicator.setReadingType(ReadingType.T);
            }
        }
    }

    @OnCheckedChanged(R.id.myst_radio_button_a)
    protected void aRadioButtonChanged(boolean checked) {
        if (checked) {
            if (communicator != null) {
                communicator.setReadingType(ReadingType.A);
            }
        }
    }

    @OnTextChanged(R.id.myst_edit_text_note)
    protected void noteChanged(CharSequence note) {
        if (communicator != null) {
            communicator.setNote(note);
        }
    }

    @OnClick(R.id.myst_button_calibrate)
    protected void calibrateClicked() {
        if (communicator != null) {
            communicator.calibrate();
        }
    }

    @OnClick(R.id.myst_button_single_capture)
    protected void singleCaptureClicked() {
        if (communicator != null) {
            communicator.takeInstantCapture();
        }
    }

    @OnClick(R.id.myst_image_view_start_stop)
    protected void stopStartClicked() {
        if (communicator != null) {
            if (communicator.isCapturing()) {
                communicator.stopCapture();
            } else {
                communicator.startCapture();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.myst_button_save)
    protected void saveClicked() {
        if (fileManager != null && communicator != null) {
            fileManager.save();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.myst_button_sessions)
    protected void sessionsClicked() {
        if (fileManager != null) {
            sessionAdapter = new SessionAdapter(getContext(), fileManager.getSessions(), fileManager.isWriteEnabled());

            new AlertDialog.Builder(getContext())
                    .setAdapter(sessionAdapter, sessionClickListener)
                    .setTitle(R.string.myst_dialog_title_sessions)
                    .setNegativeButton(R.string.myst_dialog_button_negative_session,
                            negativeClick)
                    .show();
        }
    }

    @OnClick(R.id.myst_button_clear)
    protected void clearClicked() {
        if (communicator != null) {
            if (!communicator.getLog().isSaved()) {
                showWarningDialog(positiveClickClear);
            } else {
                communicator.clearLogs();
            }
        }
        viewNote.setText("");
    }

    private void showWarningDialog(DialogInterface.OnClickListener positiveListener) {
         new AlertDialog.Builder(getContext())
                .setTitle(R.string.myst_dialog_title_log_removal)
                .setMessage(R.string.myst_dialog_body_log_removal)
                .setPositiveButton(R.string.myst_dialog_button_positive_log_removal,
                        positiveListener)
                .setNegativeButton(R.string.myst_dialog_button_negative_log_removal,
                        negativeClick)
                .show();
    }
}
