package com.xdesign.android.mystrica.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.activities.MainActivity;
import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.enums.SamplingRate;
import com.xdesign.android.mystrica.interfaces.Communicator;
import com.xdesign.android.mystrica.interfaces.CommunicatorListener;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.models.Duration;
import com.xdesign.android.mystrica.models.Log;
import com.xdesign.android.mystrica.models.LogEntry;
import com.xdesign.android.mystrica.util.Conversions;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author keithkirk
 */
public abstract class AbstractCommunicatorService extends Service
        implements Communicator, CommunicatorListener {

    private static final String NAME_CHANGE_PASSWORD = "mystr1ca";

    protected static final SamplingRate   DEFAULT_SAMPLING_RATE = SamplingRate.ONE_SECOND;
    protected static final Colour         DEFAULT_COLOUR = Colour.RED;
    protected static final ReadingType    DEFAULT_READING_TYPE = ReadingType.T;
    protected static final CharSequence   DEFAULT_NOTE = "";

    private static boolean isRunning = false;

    private final Set<WeakReference<CommunicatorListener>> listeners;

    protected final Log log;

    protected final Handler uiHandler;

    protected SamplingRate samplingRate;
    protected double transmittance;
    protected double absorbance;
    protected Colour colour;
    protected ReadingType readingType;
    protected CharSequence note;

    protected boolean capturing;

    private Date captureStarted;
    private Duration duration;

    private boolean durationRun;

    public static boolean isRunning() {
        return isRunning;
    }

    protected final Runnable timedLogRunnable = new Runnable() {
        @Override
        public void run() {
            final LogEntry entry = new LogEntry(
                    (new Date().getTime() - captureStarted.getTime()) / 1000,
                    transmittance,
                    absorbance,
                    note.toString(),
                    colour,
                    false);

            log.add(entry);

            if (durationRun) {

                duration = Duration.decrement(duration, samplingRate.getDelay() / 1000);

                if (duration != null) {
                    uiHandler.postDelayed(this, samplingRate.getDelay());
                } else {
                    durationRun = false;

                    stopCapture();

                    showDurationCompleteNotification();
                }
            } else {
                uiHandler.postDelayed(this, samplingRate.getDelay());
            }
        }
    };

    protected final Runnable singleLogRunnable = new Runnable() {
        @Override
        public void run() {
            final LogEntry entry = new LogEntry(
                    (new Date().getTime() - captureStarted.getTime()) / 1000,
                    transmittance,
                    absorbance,
                    note.toString(),
                    colour,
                    true);

            log.add(entry);
        }
    };

    public AbstractCommunicatorService() {
        this.listeners = new HashSet<>();
        this.uiHandler = new Handler();
        this.log = new Log();

        this.samplingRate   = DEFAULT_SAMPLING_RATE;
        this.colour         = DEFAULT_COLOUR;
        this.readingType    = DEFAULT_READING_TYPE;
        this.note           = DEFAULT_NOTE;

        this.log.setCurrentReadingType(this.readingType);
        this.capturing = false;

        setTransmittance(87.5);
    }

    protected void setTransmittance(double transmittance) {
        this.transmittance = transmittance;
        this.absorbance = Conversions.transmittanceToAbsorbance(transmittance);
    }

    protected abstract void updateDeviceName(String name);

    @Override
    public void onCreate() {
        super.onCreate();

        AbstractCommunicatorService.isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new CommunicatorServiceBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        AbstractCommunicatorService.isRunning = false;
    }

    @Override
    public boolean setupSuccess() {
        return true;
    }

    @Override
    public void showSetupProblemDialog(Context context) {
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public void addListener(CommunicatorListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void changeConnectedDeviceName(String name, String password) {
        if (NAME_CHANGE_PASSWORD.equals(password)) {
            updateDeviceName(name);
        } else {
            Toast.makeText(this, R.string.myst_toast_device_name_change_failure,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isCapturing() {
        return capturing;
    }

    @Override
    public void startCapture() {
        if (getConnectedDevice() != null && !capturing) {
            capturing = true;

            if (captureStarted == null) {
                captureStarted = new Date();
            }

            uiHandler.post(timedLogRunnable);

            onCaptureStarted();
        }
    }

    @Override
    public void stopCapture() {
        if (capturing) {
            uiHandler.removeCallbacks(timedLogRunnable);
            capturing = false;

            duration = null;
            if (durationRun) {
                Toast.makeText(this, R.string.myst_toast_duration_capture_cancelled, Toast.LENGTH_SHORT)
                        .show();
            }

            durationRun = false;
            onCaptureStopped();
        }
    }

    @Override
    public void captureForDuration(Duration duration) {
        stopCapture();

        this.duration = duration;
        this.durationRun = true;
        Toast.makeText(this, R.string.myst_toast_duration_capture_started, Toast.LENGTH_SHORT)
                .show();

        startCapture();
    }

    @Override
    public void takeInstantCapture() {
        if (captureStarted == null) {
            captureStarted = new Date();
        }

        uiHandler.post(singleLogRunnable);
    }

    @Override
    public double getCurrentTransmittance() {
        return transmittance;
    }

    @Override
    public double getCurrentAbsorbance() {
        return absorbance;
    }

    @Override
    public SamplingRate getSamplingRate() {
        return samplingRate;
    }

    @Override
    public void setSamplingRate(SamplingRate samplingRate) {
        this.samplingRate = samplingRate;

        if (capturing) {
            uiHandler.removeCallbacks(timedLogRunnable);
            uiHandler.post(timedLogRunnable);
        }

        onSamplingRateChanged(samplingRate);
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public void setColour(Colour colour) {
        this.colour = colour;
        onColourChanged(colour);
    }

    @Override
    public ReadingType getReadingType() {
        return readingType;
    }

    @Override
    public void setReadingType(ReadingType readingType) {
        this.readingType = readingType;
        this.log.setCurrentReadingType(readingType);
        onReadingTypeChanged(readingType);
    }

    @Override
    public CharSequence getNote() {
        return note;
    }

    @Override
    public void setNote(CharSequence note) {
        this.note = note;
        onNoteChanged(note);
    }

    @Override
    public void clearLogs() {
        log.clear();
        captureStarted = null;
    }

    @Override
    public void onDeviceConnection(final Device device) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onDeviceConnection(device);
                    }
                }
            }
        });
    }

    @Override
    public void onCaptureStarted() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onCaptureStarted();
                    }
                }
            }
        });
    }

    @Override
    public void onCaptureStopped() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onCaptureStopped();
                    }
                }
            }
        });
    }

    @Override
    public void onTransmittanceUpdated() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onTransmittanceUpdated();
                    }
                }
            }
        });
    }

    @Override
    public void onAbsorbanceUpdated() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onAbsorbanceUpdated();
                    }
                }
            }
        });
    }

    @Override
    public void onSamplingRateChanged(final SamplingRate samplingRate) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onSamplingRateChanged(samplingRate);
                    }
                }
            }
        });
    }

    @Override
    public void onColourChanged(final Colour colour) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onColourChanged(colour);
                    }
                }
            }
        });
    }

    @Override
    public void onReadingTypeChanged(final ReadingType readingType) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onReadingTypeChanged(readingType);
                    }
                }
            }
        });
    }

    @Override
    public void onNoteChanged(final CharSequence note) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WeakReference<CommunicatorListener> listener : listeners) {
                    if (listener.get() != null) {
                        listener.get().onNoteChanged(note);
                    }
                }
            }
        });
    }

    private void showDurationCompleteNotification() {
        final NotificationManagerCompat notificationManager
                = NotificationManagerCompat.from(this);
        final Intent intent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notificationManager.notify(0,
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.myst_icon_duration)
                        .setContentTitle(getString(R.string.myst_notification_title_duration_complete))
                        .setContentText(getString(R.string.myst_notification_text_duration_complete))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build());
    }
}
