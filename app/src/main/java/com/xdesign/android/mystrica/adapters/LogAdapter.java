package com.xdesign.android.mystrica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.interfaces.LogListener;
import com.xdesign.android.mystrica.models.LogEntry;
import com.xdesign.android.mystrica.viewholders.LogRowViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author keithkirk
 */
public class LogAdapter extends BaseAdapter implements LogListener {

    private final LayoutInflater inflater;
    private final Context context;
    private final List<LogEntry> logs;
    private final Set<Long> entryTimes;

    public LogAdapter(Context context) {
        this(context, new ArrayList<LogEntry>());
    }

    public LogAdapter(Context context, List<LogEntry> logs) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.logs = logs;
        this.entryTimes = new HashSet<>();
    }

    private void add(LogEntry log) {
        add(Collections.singletonList(log));
    }

    private void add(List<LogEntry> logs) {
        for (LogEntry entry : logs) {
            if (allowEntryAddition(entry)) {
                this.logs.add(entry);
            }
        }

        notifyDataSetChanged();
    }

    private void clear() {
        this.entryTimes.clear();
        this.logs.clear();
        notifyDataSetChanged();
    }

    public void setEnabled(int index, boolean enabled) {
        if (index < this.logs.size()) {
            this.logs.get(index).setEnabled(enabled);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public Object getItem(int position) {
        return logs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LogRowViewHolder holder;

        if (convertView != null) {
            holder = (LogRowViewHolder) convertView.getTag();
        } else {
            holder = new LogRowViewHolder(context);
            convertView = holder.createView(inflater, parent);
        }

        holder.setup(logs.get(position));

        return convertView;
    }

    @Override
    public void onNewEntry(LogEntry entry) {
        add(entry);
    }

    @Override
    public void onNewEntries(List<LogEntry> entries) {
        add(entries);
    }

    @Override
    public void onLogsCleared() {
        clear();
    }

    @Override
    public void onReadingTypeChanged(ReadingType type) {
    }

    private boolean allowEntryAddition(LogEntry entry) {
        if (entryTimes.contains(entry.getSeconds())) {
            return false;
        } else {
            entryTimes.add(entry.getSeconds());
            return true;
        }
    }
}
