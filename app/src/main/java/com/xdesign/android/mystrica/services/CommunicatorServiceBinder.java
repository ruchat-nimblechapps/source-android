package com.xdesign.android.mystrica.services;

import android.os.Binder;

import com.xdesign.android.mystrica.interfaces.Communicator;

/**
 * @author keithkirk
 */
public class CommunicatorServiceBinder extends Binder {

    private final Communicator communicator;

    public CommunicatorServiceBinder(Communicator communicator) {
        this.communicator = communicator;
    }

    public Communicator getCommunicator() {
        return communicator;
    }
}
