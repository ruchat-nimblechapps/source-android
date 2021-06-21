package com.xdesign.android.mystrica.services;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.adapters.DeviceAdapter;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.models.TestDevice;

/**
 * @author keithkirk
 */
public class TestCommunicatorService extends AbstractCommunicatorService {

    private static final String[] DEVICES
            = new String[] {"Device 1", "Device 2", "Device 3"};

    private DeviceAdapter<TestDevice> deviceAdapter;

    private Device connectedDevice;

    @Override
    public void onCreate() {
        super.onCreate();

        setupDeviceAdapter();
    }

    @Override
    protected void updateDeviceName(String name) {
        connectedDevice.setName(name);
        Toast.makeText(this, R.string.myst_toast_device_name_change_success,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public DeviceAdapter getDeviceAdapter() {
        return deviceAdapter;
    }

    @Override
    public void connectToDevice(Device device) {
        stopCapture();

        if (connectedDevice != null) {
            connectedDevice.setConnected(false);
        }

        connectedDevice = device;

        if (device != null) {
            device.setConnected(true);
        }

        onDeviceConnection(device);
    }

    @Nullable
    @Override
    public Device getConnectedDevice() {
        return connectedDevice;
    }


    @Override
    public void calibrate() {
        setTransmittance(100.0);
    }

    private void setupDeviceAdapter() {
        deviceAdapter = new DeviceAdapter<>(this, this);

        int i = 0;
        for (String name : DEVICES) {
            final TestDevice device = new TestDevice(i + "", name);

            if (device.equals(connectedDevice)) {
                device.setConnected(true);
            }

            deviceAdapter.add(device);
            i++;
        }
    }
}
