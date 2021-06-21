package com.xdesign.android.mystrica.interfaces;

import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.models.LogEntry;

import java.util.List;

/**
 * @author keithkirk
 */
public interface LogListener {

    void onNewEntry(LogEntry entry);

    void onNewEntries(List<LogEntry> entries);

    void onLogsCleared();

    void onReadingTypeChanged(ReadingType type);
}
