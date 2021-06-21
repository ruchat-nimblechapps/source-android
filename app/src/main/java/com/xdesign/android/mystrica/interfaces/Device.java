package com.xdesign.android.mystrica.interfaces;

/**
 * @author keithkirk
 */
public interface Device {

    String getName();

    void setName(String name);

    boolean isConnected();

    void setConnected(boolean connected);

    void addListener(DeviceChangeListener listener);
}
