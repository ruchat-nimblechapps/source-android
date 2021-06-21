package com.xdesign.android.mystrica.models;

import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.interfaces.DeviceChangeListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * @author keithkirk
 */
public abstract class AbstractDevice implements Device {

    private final List<WeakReference<DeviceChangeListener>> listeners;

    private final String id;

    private String name;
    private boolean connected;

    public AbstractDevice(String id, String name) {
        this.listeners = new LinkedList<>();

        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        onNameChanged(name);
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void addListener(DeviceChangeListener listener) {
        listeners.add(new WeakReference<>(listener));
    }

    private void onNameChanged(String name) {
        for (WeakReference<DeviceChangeListener> listener : listeners) {
            if (listener.get() != null) {
                listener.get().onNameChange(name);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractDevice that = (AbstractDevice) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
