package com.xdesign.android.mystrica.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

import com.xdesign.android.mystrica.BuildConfig;
import com.xdesign.android.mystrica.services.BluetoothCommunicatorService;
import com.xdesign.android.mystrica.services.TestCommunicatorService;

/**
 * @author keithkirk
 *
 * A factory to create Communicator Services based on state and specify if they're running.
 */
public class CommunicatorServiceFactory {

    /**
     * Builds a BluetoothCommunicatorService unless the app is a debug build and the device does
     * not support bluetooth, this means that debug build on emulators should be the main
     * circumstance in which the TestCommunicatorService is used.
     *
     * @param context The calling context, used to build an intent.
     * @return The product containing the intent to launch the service and whether it is running
     * already or not.
     */
    public static Product buildCommunicatorService(Context context) {
        final Intent intent;
        final boolean running;

        if (BuildConfig.DEBUG && BluetoothAdapter.getDefaultAdapter() == null) {
            intent = new Intent(context, TestCommunicatorService.class);
            running = TestCommunicatorService.isRunning();
        } else {
            intent = new Intent(context, BluetoothCommunicatorService.class);
            running = BluetoothCommunicatorService.isRunning();
        }

        return new Product(intent, running);
    }

    private CommunicatorServiceFactory() {
    }

}
