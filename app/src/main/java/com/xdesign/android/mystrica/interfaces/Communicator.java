package com.xdesign.android.mystrica.interfaces;

import android.content.Context;

import androidx.annotation.Nullable;

import com.xdesign.android.mystrica.adapters.DeviceAdapter;
import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.SamplingRate;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.models.Duration;
import com.xdesign.android.mystrica.models.Log;

/**
 * @author keithkirk
 */
public interface Communicator {

    boolean setupSuccess();

    @Nullable
    void showSetupProblemDialog(Context context);

    Log getLog();

    void addListener(CommunicatorListener listener);

    DeviceAdapter getDeviceAdapter();

    void connectToDevice(@Nullable Device device);

    @Nullable
    Device getConnectedDevice();

    void changeConnectedDeviceName(String name, String password);

    void calibrate();

    boolean isCapturing();

    void startCapture();

    void stopCapture();

    void captureForDuration(Duration duration);

    void takeInstantCapture();

    double getCurrentTransmittance();

    double getCurrentAbsorbance();

    SamplingRate getSamplingRate();

    void setSamplingRate(SamplingRate samplingRate);

    Colour getColour();

    void setColour(Colour colour);

    ReadingType getReadingType();

    void setReadingType(ReadingType readingType);

    CharSequence getNote();

    void setNote(CharSequence note);

    void clearLogs();
}
