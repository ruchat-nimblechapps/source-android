package com.xdesign.android.common.testing.utils;

import android.app.Instrumentation;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link LayoutInflater} which allows for easy inflating of custom layouts
 * contained within the test project, using the target context.
 */
public class TestLayoutInflater extends LayoutInflater {

    private final Instrumentation instrumentation;

    public TestLayoutInflater(final Instrumentation instrumentation) {
        super(instrumentation.getTargetContext());

        this.instrumentation = instrumentation;
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return LayoutInflater.from(newContext);
    }

    @Override
    public View inflate(int resource, ViewGroup root) {
        return inflate(
                instrumentation.getContext().getResources().getLayout(resource),
                root);
    }

    @Override
    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        return inflate(
                instrumentation.getContext().getResources().getLayout(resource),
                root,
                attachToRoot);
    }
}
