package com.xdesign.android.mystrica.models;


import android.bluetooth.BluetoothDevice;

/**
 * @author keithkirk
 */
public class BtDevice extends AbstractDevice {

    private final BluetoothDevice device;

    public BtDevice(BluetoothDevice device) {
        super(device.getAddress(), device.getName());

        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
