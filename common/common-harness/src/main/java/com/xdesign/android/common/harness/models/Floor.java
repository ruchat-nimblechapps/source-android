package com.xdesign.android.common.harness.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.xdesign.android.common.lib.annotations.DeletionOrder;

@Table(name = "floors", id = BaseColumns._ID)
@DeletionOrder(1)
public class Floor extends Model {
    
    @Column(name = "building")
    public Building building;
    
    public Floor(Building building) {
        this.building = building;
    }
}
