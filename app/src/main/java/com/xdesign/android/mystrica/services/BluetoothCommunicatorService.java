package com.xdesign.android.mystrica.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.adapters.DeviceAdapter;
import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.models.BtDevice;
import com.xdesign.android.mystrica.util.MysteriaBluetoothGattCallback;

import java.util.UUID;

/**
 * @author keithkirk
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothCommunicatorService extends AbstractCommunicatorService
        implements BluetoothAdapter.LeScanCallback, MysteriaBluetoothGattCallback.Listener {

    private static final String TAG = BluetoothCommunicatorService.class.getSimpleName();

    private static final long SCAN_LENGTH = 5 * 1000;

    private final BluetoothAdapter bluetoothAdapter;

    private DeviceAdapter<BtDevice> deviceAdapter;

    private BtDevice connectingDevice;
    private BtDevice connectedDevice;

    private MysteriaBluetoothGattCallback gattCallback;
    private BluetoothGatt gatt;

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            respondToBroadcast(intent);
        }
    };

    public BluetoothCommunicatorService() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void updateDeviceName(String name) {
        if (gattCallback != null) {
            gattCallback.updateName(name);

            if (connectedDevice != null) {
                connectedDevice.setName(name);
            }

            Toast.makeText(this, R.string.myst_toast_device_name_change_success,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.myst_toast_device_name_change_failure,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.deviceAdapter = new DeviceAdapter<>(this, this);

        final IntentFilter stateFilter
                = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

        registerReceiver(bluetoothReceiver, stateFilter);

        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableBtIntent);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(bluetoothReceiver);

        connectToDevice(null);
    }

    @Override
    public boolean setupSuccess() {
        return bluetoothAdapter != null;
    }

    @Override
    public void showSetupProblemDialog(Context context) {
        new AlertDialog.Builder(context)
            .setTitle(R.string.myst_dialog_title_no_bluetooth)
            .setMessage(R.string.myst_dialog_text_no_bluetooth)
            .setCancelable(false)
            .show();
    }

    @Override
    public DeviceAdapter getDeviceAdapter() {
        deviceAdapter.clear();
        if (connectedDevice != null) {
            deviceAdapter.add(connectedDevice);
        }

        startScan();

        return deviceAdapter;
    }

    @Override
    public void connectToDevice(@Nullable Device device) {
        stopScan();

        if (connectedDevice != null) {
            connectedDevice.setConnected(false);
        }

        final BtDevice btDevice = deviceAdapter.getDevice(device);

        if (btDevice == null) {
            if (gattCallback != null) {
                gattCallback.disconnectionRequested();
            }

            if (gatt != null) {
                gatt.disconnect();

                if (gatt != null) {
                    gatt.close();
                }

                gatt = null;
                connectedDevice = null;

                onDeviceConnection(null);
            }
        } else {
            connectingDevice = btDevice;
            gattCallback = new MysteriaBluetoothGattCallback(this);

            gatt = btDevice.getDevice().connectGatt(this, false, gattCallback);
        }
    }

    @Nullable
    @Override
    public Device getConnectedDevice() {
        return connectedDevice;
    }

    @Override
    public void calibrate() {
        if (gattCallback != null) {
            gattCallback.calibrate();
        }
    }

    @Override
    public void setColour(Colour colour) {
        if (gattCallback != null) {
            gattCallback.updateColour(colour);
        } else {
            super.setColour(colour);
        }
    }

    @Override
    public void setReadingType(ReadingType readingType) {
        if (gattCallback != null) {
            gattCallback.updateReadingType(readingType);
        } else {
            super.setReadingType(readingType);
        }
    }

    private void startScan() {
        final boolean success = bluetoothAdapter.startLeScan(
                new UUID[]{MysteriaBluetoothGattCallback.MYSTERIA_SERVICE_UUID}, this);

        if (success) {
            Log.d(TAG, "Started LE scan");

            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan();
                }
            }, SCAN_LENGTH);
        } else {
            Log.e(TAG, "Failed to start LE scan");
        }
    }

    private void stopScan() {
        bluetoothAdapter.stopLeScan(this);
    }

    private void respondToBroadcast(Intent intent) {
        final String action = intent.getAction();

        switch (action) {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        startScan();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (!TextUtils.isEmpty(device.getName())
                && !TextUtils.isEmpty(device.getAddress())) {
            Log.d(TAG, "Device found: " + device.getName());
            deviceAdapter.add(new BtDevice(device));
        }
    }

    @Override
    public void deviceConnectionEstablished() {
        connectedDevice = connectingDevice;
        connectingDevice = null;
        connectedDevice.setConnected(true);
        onDeviceConnection(connectedDevice);
    }

    @Override
    public void deviceConnectionClosed() {
        connectToDevice(null);
    }

    @Override
    public void deviceConnectionLost() {
        uiHandler.removeCallbacks(timedLogRunnable);
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BluetoothCommunicatorService.this,
                        R.string.myst_toast_connection_lost, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onColourUpdated(Colour colour) {
        super.setColour(colour);
    }

    @Override
    public void onReadingTypeUpdated(ReadingType type) {
        super.setReadingType(type);
    }

    @Override
    public void onReadingUpdated(double reading) {
        setTransmittance(reading);
    }
}
