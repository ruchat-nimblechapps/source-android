package com.xdesign.android.mystrica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xdesign.android.mystrica.viewholders.SessionViewHolder;

import java.io.File;
import java.util.List;

/**
 * @author keithkirk
 */
public class SessionAdapter extends BaseAdapter implements SessionViewHolder.Listener {

    private final LayoutInflater inflater;
    private final Context context;
    private final List<File> sessions;
    private final boolean writeEnabled;

    public SessionAdapter(Context context, List<File> sessions, boolean writeEnabled) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.sessions = sessions;
        this.writeEnabled = writeEnabled;
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Object getItem(int position) {
        return sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SessionViewHolder holder;

        if (convertView != null) {
            holder = (SessionViewHolder) convertView.getTag();
        } else {
            holder = new SessionViewHolder(context, this, writeEnabled);
            convertView = holder.createView(inflater, parent);
        }

        holder.setup(sessions.get(position));

        return convertView;
    }

    @Override
    public void onFileRenamed(File oldFile, File newFile) {
        int index = sessions.indexOf(oldFile);
        sessions.remove(index);
        sessions.add(index, newFile);
    }

    @Override
    public void onFileDeleted(File file) {
        sessions.remove(file);
        notifyDataSetChanged();
    }
}
