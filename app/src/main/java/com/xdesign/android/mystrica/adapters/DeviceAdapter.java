package com.xdesign.android.mystrica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xdesign.android.mystrica.interfaces.Communicator;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.viewholders.DeviceViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author keithkirk
 */
public class DeviceAdapter<T extends Device> extends BaseAdapter {

    private final LayoutInflater inflater;
    private final List<T> devices;
    private final Communicator communicator;

    private Context displayContext;

    public DeviceAdapter(Context context, Communicator communicator) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.devices = new ArrayList<>();
        this.communicator = communicator;
    }

    public void setDisplayContext(Context displayContext) {
        this.displayContext = displayContext;
    }

    public void add(T device) {
        if (!devices.contains(device)) {
            devices.add(device);
            notifyDataSetChanged();
        }
    }

    public T getDevice(Device device) {
        for (T current  : devices) {
            if (current.equals(device)) {
                return current;
            }
        }

        return null;
    }

    public void clear() {
        devices.clear();
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DeviceViewHolder holder;

        if (convertView != null) {
            holder = (DeviceViewHolder) convertView.getTag();
        } else {
            holder = new DeviceViewHolder(displayContext, communicator);
            convertView = holder.createView(inflater, parent);
        }

        holder.setup(devices.get(position));

        return convertView;
    }
}
