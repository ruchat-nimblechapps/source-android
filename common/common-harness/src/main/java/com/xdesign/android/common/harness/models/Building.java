package com.xdesign.android.common.harness.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.xdesign.android.common.lib.annotations.DeletionOrder;

@Table(name = "buildings", id = BaseColumns._ID)
@DeletionOrder(2)
public class Building extends Model {
    // NOP
}
