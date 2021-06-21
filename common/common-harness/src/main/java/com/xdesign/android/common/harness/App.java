package com.xdesign.android.common.harness;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.xdesign.android.common.harness.models.Building;
import com.xdesign.android.common.harness.models.Desk;
import com.xdesign.android.common.harness.models.Floor;

public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(new Configuration.Builder(this)
                .setDatabaseName("data.db")
                .setDatabaseVersion(1)
                .addModelClass(Building.class)
                .addModelClass(Desk.class)
                .addModelClass(Floor.class)
                .create());
    }
}
