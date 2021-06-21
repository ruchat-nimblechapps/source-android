package com.xdesign.android.mystrica.util;

import android.content.Intent;

/**
 * @author keithkirk
 */
public class Product {

    private final Intent intent;
    private final boolean running;

    public Product(Intent intent, boolean running) {
        this.intent = intent;
        this.running = running;
    }

    public Intent getIntent() {
        return intent;
    }

    public boolean isRunning() {
        return running;
    }
}
