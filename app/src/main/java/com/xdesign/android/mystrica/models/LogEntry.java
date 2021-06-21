package com.xdesign.android.mystrica.models;

import com.xdesign.android.mystrica.enums.Colour;

/**
 * @author keithkirk
 */
public class LogEntry {

    private final long seconds;
    private final double transmittance;
    private final double absorbance;
    private final String note;
    private final Colour colour;
    private final boolean singleCapture;

    private boolean enabled;

    public LogEntry(long seconds,
                    double transmittance,
                    double absorbance,
                    String note,
                    Colour colour,
                    boolean singleCapture) {
        this.seconds = seconds;
        this.transmittance = transmittance;
        this.absorbance = absorbance;
        this.note = note;
        this.colour = colour;
        this.singleCapture = singleCapture;
    }

    public long getSeconds() {
        return seconds;
    }

    public double getTransmittance() {
        return transmittance;
    }

    public double getAbsorbance() {
        return absorbance;
    }

    public String getNote() {
        return note;
    }

    public Colour getColour() {
        return colour;
    }

    public boolean isSingleCapture() {
        return singleCapture;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
