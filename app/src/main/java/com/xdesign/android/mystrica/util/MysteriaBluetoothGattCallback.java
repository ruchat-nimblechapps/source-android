package com.xdesign.android.mystrica.util;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.ReadingType;

import java.util.UUID;

/**
 * @author keithkirk
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MysteriaBluetoothGattCallback extends BluetoothGattCallback {

    private static final String TAG = BluetoothGattCallback.class.getName();

    public static final UUID MYSTERIA_SERVICE_UUID
            = UUID.fromString("02401523-815E-4188-CB44-701C1E28E56C");
    public  static final UUID MYSTERIA_CHARACTERISTIC_UUID
            = UUID.fromString("02401525-815E-4188-CB44-701C1E28E56C");
    public static final UUID MYSTERIA_NAME_CHARACTERISTIC_UUID
            = UUID.fromString("02401526-815E-4188-CB44-701C1E28E56C");
    public static final UUID DEVICE_INFORMATION_SERVICE_UUID
            = UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB");
    public static final UUID HARDWARE_REVISION_STRING_UUID
            = UUID.fromString("00002A27-0000-1000-8000-00805F9B34FB");

    private static final String COLOUR_R_COMMAND = "rr";
    private static final String COLOUR_G_COMMAND = "rg";
    private static final String COLOUR_B_COMMAND = "rb";
    private static final String READING_TYPE_T_COMMAND = "tt";
    private static final String READING_TYPE_A_COMMAND = "ta";
    private static final String CALIBRATE_COMMAND = "c";

    private static final int COLOUR_R_VALUE = 0;
    private static final int COLOUR_G_VALUE = 1;
    private static final int COLOUR_B_VALUE = 2;
    private static final int READING_TYPE_T_VALUE = 1;
    private static final int READING_TYPE_A_VALUE = 0;

    private static final int VALUE_DECIMAL_OFFSET = 0;
    private static final int READING_TYPE_OFFSET = 2;
    private static final int COLOUR_OFFSET = 3;

    private static final int CONNECTION_TIMEOUT = 2 * 1000;

    private final Handler handler;
    private final Listener listener;

    private BluetoothGatt gatt;
    private BluetoothGattService mystService;
    private BluetoothGattService deviceInfoService;

    private BluetoothGattCharacteristic mystCharacteristic;
    private BluetoothGattCharacteristic mystNameCharacteristic;
    private BluetoothGattCharacteristic hardwareRevisionStringCharacteristic;

    private final Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            listener.deviceConnectionLost();
            onConnectionStateChange(gatt, 0, BluetoothProfile.STATE_DISCONNECTED);
        }
    };

    public MysteriaBluetoothGattCallback(Listener listener) {
        this.handler = new Handler();
        this.listener = listener;
    }

    public void updateReadingType(ReadingType type) {
        setValue(mystCharacteristic, commandForReadingType(type));
    }

    public void updateColour(Colour colour) {
        setValue(mystCharacteristic, commandForColour(colour));
    }

    public void calibrate() {
        handler.removeCallbacks(timeoutRunnable);
        setValue(mystCharacteristic, CALIBRATE_COMMAND);
    }

    public void updateName(String name) {
        setValue(mystNameCharacteristic, name);
    }

    private void setValue(BluetoothGattCharacteristic characteristic, String value) {
        if (characteristic != null) {
            characteristic.setValue(value);

            if (gatt != null) {
                gatt.writeCharacteristic(characteristic);
            }
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (characteristic != null) {
            if (characteristic.getUuid().equals(MYSTERIA_CHARACTERISTIC_UUID)) {
                Log.d(TAG, "Mystrica characteristic changed");

                handler.removeCallbacks(timeoutRunnable);

                int format = BluetoothGattCharacteristic.FORMAT_UINT8;

                final int colourValue = characteristic.getIntValue(format, COLOUR_OFFSET);
                final Colour colour = colourForValue(colourValue);
                listener.onColourUpdated(colour);
                Log.d(TAG, "Colour " + colour);

                final int typeValue = characteristic.getIntValue(format, READING_TYPE_OFFSET);
                final ReadingType type = readingTypeForValue(typeValue);
                listener.onReadingTypeUpdated(type);
                Log.d(TAG, "Type " + type);

                format = BluetoothGattCharacteristic.FORMAT_UINT16;

                final int valueDecimal = characteristic.getIntValue(format, VALUE_DECIMAL_OFFSET);
                final double reading = valueDecimal / 100.0;
                listener.onReadingUpdated(reading);
                Log.d(TAG, "Reading " + reading);

                handler.postDelayed(timeoutRunnable, CONNECTION_TIMEOUT);

            } else if (characteristic.getUuid().equals(MYSTERIA_NAME_CHARACTERISTIC_UUID)) {
                Log.d(TAG, "Mystrica Name characteristic changed");
            } else if (characteristic.getUuid().equals(HARDWARE_REVISION_STRING_UUID)) {
                Log.d(TAG, "Hardware Revision String characteristic changed");
            }
        }
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                listener.deviceConnectionEstablished();
                gatt.discoverServices();
                this.gatt = gatt;
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                handler.removeCallbacks(timeoutRunnable);
                listener.deviceConnectionClosed();

                this.gatt = null;
                mystService = null;
                deviceInfoService = null;

                mystCharacteristic = null;
                mystNameCharacteristic = null;
                hardwareRevisionStringCharacteristic = null;
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {

            String name;

            mystService = gatt.getService(MYSTERIA_SERVICE_UUID);
            if (mystService != null) {
                name = "Mysteria";

                mystCharacteristic
                        = mystService.getCharacteristic(MYSTERIA_CHARACTERISTIC_UUID);
                if (mystCharacteristic != null) {

                    setNotiificationsOnCharacteristic(gatt, mystCharacteristic, name);

                    updateReadingType(listener.getReadingType());
                    updateColour(listener.getColour());
                } else {
                    Log.e(TAG, String.format("Failed to retrieve %s characteristic from device",
                            name));
                }

                name = "Mysteria Name";

                mystNameCharacteristic
                        = mystService.getCharacteristic(MYSTERIA_NAME_CHARACTERISTIC_UUID);
                if (mystNameCharacteristic != null) {
                    setNotiificationsOnCharacteristic(gatt, mystNameCharacteristic, name);
                } else {
                    Log.e(TAG, String.format("Failed to retrieve %s characteristic from device",
                            name));
                }
            } else {
                Log.e(TAG, "Failed to retrieve Mysteria Service from device");
            }

            name = "Hardware Revision String";

            deviceInfoService = gatt.getService(DEVICE_INFORMATION_SERVICE_UUID);
            if (deviceInfoService != null) {
                hardwareRevisionStringCharacteristic
                        = deviceInfoService.getCharacteristic(HARDWARE_REVISION_STRING_UUID);

                if (hardwareRevisionStringCharacteristic != null) {
                    setNotiificationsOnCharacteristic(gatt, hardwareRevisionStringCharacteristic,
                            name);
                } else {
                    Log.e(TAG,
                            String.format("Failed to retrieve %s characteristic from device",
                                    name));
                }
            } else {
                Log.e(TAG, "Failed to retrieve Device Info Service from device");
            }
        } else {
            Log.e(TAG, "Failed to explore remote device");
        }
    }

    public void disconnectionRequested() {
        handler.removeCallbacks(timeoutRunnable);
    }

    private void setNotiificationsOnCharacteristic(BluetoothGatt gatt,
                                                   BluetoothGattCharacteristic characteristic,
                                                   String name) {
        if (gatt.setCharacteristicNotification(characteristic, true)) {

            for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (!gatt.writeDescriptor(descriptor)) {
                    Log.e(TAG,
                            String.format("Failed to set descriptor on %s characteristic", name));
                }
            }
        } else {
            Log.e(TAG, String.format("Failed to set notification on %s characteristic", name));
        }
    }

    private static String commandForColour(Colour colour) {
        switch (colour) {
            case RED:
                return COLOUR_R_COMMAND;
            case GREEN:
                return COLOUR_G_COMMAND;
            case BLUE:
                return COLOUR_B_COMMAND;
            default:
                return "";
        }
    }

    private static String commandForReadingType(ReadingType type) {
        switch (type) {
            case T:
                return READING_TYPE_T_COMMAND;
            case A:
                return READING_TYPE_A_COMMAND;
            default:
                return "";
        }
    }

    private static Colour colourForValue(int value) {
        switch (value) {
            case COLOUR_R_VALUE:
                return Colour.RED;
            case COLOUR_G_VALUE:
                return Colour.GREEN;
            case COLOUR_B_VALUE:
                return Colour.BLUE;
            default:
                return Colour.RED;
        }
    }

    private static ReadingType readingTypeForValue(int value) {
        switch (value) {
            case READING_TYPE_T_VALUE:
                return ReadingType.T;
            case READING_TYPE_A_VALUE:
                return ReadingType.A;
            default:
                return ReadingType.T;
        }
    }

    public interface Listener {

        void deviceConnectionEstablished();

        void deviceConnectionClosed();

        void deviceConnectionLost();

        Colour getColour();

        ReadingType getReadingType();

        void onColourUpdated(Colour colour);

        void onReadingTypeUpdated(ReadingType type);

        void onReadingUpdated(double reading);
    }
}
