package com.xdesign.android.mystrica.interfaces;

import android.util.Pair;

import java.util.List;

/**
 * @author keithkirk
 */
public interface FileManagerListener {

    List<Pair<Boolean, Integer>> saveFile(String directoryPath, String name);
}
