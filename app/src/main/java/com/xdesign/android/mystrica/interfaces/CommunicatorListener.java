package com.xdesign.android.mystrica.interfaces;

import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.SamplingRate;
import com.xdesign.android.mystrica.enums.ReadingType;

/**
 * @author keithkirk
 */
public interface CommunicatorListener {

    void onDeviceConnection(Device device);

    void onCaptureStarted();

    void onCaptureStopped();

    void onTransmittanceUpdated();

    void onAbsorbanceUpdated();

    void onSamplingRateChanged(SamplingRate samplingRate);

    void onColourChanged(Colour colour);

    void onReadingTypeChanged(ReadingType readingType);

    void onNoteChanged(CharSequence note);
}
