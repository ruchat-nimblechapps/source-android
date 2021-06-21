package com.xdesign.android.mystrica.activities;

import android.Manifest;
import androidx.fragment.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xdesign.android.common.lib.activities.BaseActivity;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.adapters.DeviceAdapter;
import com.xdesign.android.mystrica.adapters.MystFragmentPagerAdapter;
import com.xdesign.android.mystrica.adapters.Page;
import com.xdesign.android.mystrica.enums.Colour;
import com.xdesign.android.mystrica.enums.ReadingType;
import com.xdesign.android.mystrica.enums.SamplingRate;
import com.xdesign.android.mystrica.fragments.GraphFragment;
import com.xdesign.android.mystrica.fragments.TableFragment;
import com.xdesign.android.mystrica.interfaces.Communicator;
import com.xdesign.android.mystrica.interfaces.CommunicatorListener;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.interfaces.DeviceChangeListener;
import com.xdesign.android.mystrica.services.CommunicatorServiceBinder;
import com.xdesign.android.mystrica.util.BestFitManager;
import com.xdesign.android.mystrica.util.CommunicatorServiceFactory;
import com.xdesign.android.mystrica.util.FileManager;
import com.xdesign.android.mystrica.util.Product;
import com.xdesign.android.mystrica.views.AboutView;
import com.xdesign.android.mystrica.views.ControlView;
import com.xdesign.android.mystrica.views.DurationPickerView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author keithkirk
 */
public class MainActivity extends BaseActivity
        implements CommunicatorListener, DeviceChangeListener, ServiceConnection {

    private static final int PERMISSIONS_REQUEST_COURSE_LOCATION = 0;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @BindView(R.id.myst_group_device)
    protected View deviceGroup;

    @BindView(R.id.myst_text_view_device)
    protected TextView deviceTextView;

    @BindView(R.id.myst_group_duration)
    protected View durationGroup;

    @BindView(R.id.myst_image_view_duration)
    protected ImageView durationImageView;

    @BindView(R.id.myst_view_tabs)
    protected TabLayout tabLayout;

    @Nullable
    @BindView(R.id.myst_view_pager)
    protected ViewPager fragmentPager;

    protected TableFragment tableFragment = (TableFragment)
            getSupportFragmentManager().findFragmentById(R.id.myst_fragment_table);

    protected GraphFragment graphFragment;

    @BindView(R.id.myst_view_control)
    protected ControlView controlView;

    private Communicator communicator;
    private FileManager fileManager;
    private BestFitManager bestFitManager;

    private DeviceAdapter deviceAdapter;

    private NumberPicker frequencyPicker;

    private DurationPickerView durationPickerView;

    private DialogInterface.OnClickListener negativeClick
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener devicePositiveClickListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            deviceGroup.setBackgroundResource(R.drawable.myst_background_no_device_selected);
            deviceTextView.setText(R.string.myst_label_disconnecting);
            communicator.connectToDevice(null);

            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener deviceClickListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final Device device = (Device) deviceAdapter.getItem(which);

            deviceGroup.setBackgroundResource(R.drawable.myst_background_no_device_selected);
            deviceTextView.setText(R.string.myst_label_connecting);
            communicator.connectToDevice(device);

            dialog.dismiss();

        }
    };

    private DialogInterface.OnClickListener frequencyPositiveClick
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            communicator.setSamplingRate(SamplingRate.values()[frequencyPicker.getValue()]);

            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener durationPositiveClick
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            communicator.captureForDuration(durationPickerView.getSelectedDuration());

            dialog.dismiss();
        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myst_activity_main);
        ButterKnife.bind(this);

        if (isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        fragmentPager = findViewById(R.id.myst_view_pager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (hasLocationPermission()) {
            startSetup();
        } else {
            requestLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (deviceAdapter != null) {
            deviceAdapter.setDisplayContext(null);
        }

        unbindService(this);
    }

    @Override
    public void onDeviceConnection(Device device) {
        deviceGroup.setBackgroundResource(device == null
                ? R.drawable.myst_background_no_device_selected
                : R.drawable.myst_background_device_selected);
        deviceTextView.setText(device == null
                ? getString(R.string.myst_label_connect)
                : device.getName());
        durationGroup.setEnabled(device != null);
        durationImageView.setImageResource(device != null
                ? R.drawable.myst_icon_duration
                : R.drawable.myst_icon_duration_disabled);

        if (device != null) {
            device.addListener(this);
        }
    }

    @Override
    public void onCaptureStarted() {

    }

    @Override
    public void onCaptureStopped() {

    }

    @Override
    public void onTransmittanceUpdated() {

    }

    @Override
    public void onAbsorbanceUpdated() {

    }

    @Override
    public void onSamplingRateChanged(SamplingRate samplingRate) {

    }

    @Override
    public void onColourChanged(Colour colour) {

    }

    @Override
    public void onReadingTypeChanged(ReadingType readingType) {

    }

    @Override
    public void onNoteChanged(CharSequence note) {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_COURSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startSetup();
                } else {
                    requestLocationPermission();
                }
                break;
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                fileManager.setWriteEnabled(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_COURSE_LOCATION);
    }

    private boolean hasWritePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestWritePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    private void startSetup() {
        final Product product = CommunicatorServiceFactory.buildCommunicatorService(this);

        final Intent intent = product.getIntent();

        if (!product.isRunning()) {
            startService(intent);
        }

        bindService(intent, this, Context.BIND_AUTO_CREATE);

        if (deviceAdapter != null) {
            deviceAdapter.setDisplayContext(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupFileManager() {
        fileManager = new FileManager(this, communicator.getLog());

        if (hasWritePermission()) {
            fileManager.setWriteEnabled(true);
        } else {
            requestWritePermission();
        }
    }

    private void setupBestFitManager() {
        bestFitManager = new BestFitManager(communicator.getLog());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupFragments() {
        if (!isTablet()) {
            tableFragment = new TableFragment();
            graphFragment = new GraphFragment();
        } else {
            tableFragment = (TableFragment)
                    getSupportFragmentManager().findFragmentById(R.id.myst_fragment_table);
            graphFragment = (GraphFragment)
                    getSupportFragmentManager().findFragmentById(R.id.myst_fragment_graph);
        }

        tableFragment.setLog(communicator.getLog());
        tableFragment.setBestFitManager(bestFitManager);
        graphFragment.setLog(communicator.getLog());
        graphFragment.setBestFitManager(bestFitManager);
        fileManager.addListener(graphFragment);
    }

    private void setupViewPager() {
        final List<Page> pages = new ArrayList<>();

        pages.add(new Page(tableFragment, getString(R.string.myst_fragment_table)));
        pages.add(new Page(graphFragment, getString(R.string.myst_fragment_graph)));

        final FragmentPagerAdapter adapter = new MystFragmentPagerAdapter(
                getSupportFragmentManager(), pages);

        fragmentPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(fragmentPager);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setupControlView() {
        controlView.setCommunicator(communicator);
        controlView.setFileManager(fileManager);
    }

    @OnClick(R.id.myst_group_device)
    protected void deviceClicked() {
        deviceAdapter = communicator.getDeviceAdapter();
        deviceAdapter.setDisplayContext(this);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setAdapter(deviceAdapter, deviceClickListener)
                .setTitle(R.string.myst_dialog_title_devices)
                .setNegativeButton(R.string.myst_dialog_button_negative_devices,
                        negativeClick);

        if (communicator.getConnectedDevice() != null) {
            builder.setPositiveButton(R.string.myst_dialog_button_positive_devices,
                    devicePositiveClickListener);
        }

        builder.show();
    }

    @OnClick(R.id.myst_group_about)
    protected void aboutClicked() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.myst_dialog_title_about)
                .setView(new AboutView(this))
                .setNegativeButton(R.string.myst_dialog_button_negative_about, negativeClick)
                .show();
    }

    @OnClick(R.id.myst_group_duration)
    protected void durationClicked() {
        if (communicator.getConnectedDevice() != null) {

            durationPickerView = new DurationPickerView(this);

            new AlertDialog.Builder(this)
                    .setTitle(R.string.myst_dialog_title_duration)
                    .setView(durationPickerView)
                    .setPositiveButton(R.string.myst_dialog_button_positive_duration,
                            durationPositiveClick)
                    .setNegativeButton(R.string.myst_dialog_button_negative_duration,
                            negativeClick)
                    .show();
        }
    }

    @OnClick(R.id.myst_group_frequency)
    protected void frequencyClicked() {
        final String[] names = SamplingRate.getNames(this);

        frequencyPicker = new NumberPicker(this);
        frequencyPicker.setMinValue(0);
        frequencyPicker.setMaxValue(names.length - 1);
        frequencyPicker.setDisplayedValues(names);
        frequencyPicker.setValue(communicator.getSamplingRate().ordinal());
        frequencyPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        new AlertDialog.Builder(this)
                .setTitle(R.string.myst_dialog_title_frequency)
                .setView(frequencyPicker)
                .setPositiveButton(R.string.myst_dialog_button_positive_frequency,
                        frequencyPositiveClick)
                .setNegativeButton(R.string.myst_dialog_button_negative_frequency,
                        negativeClick)
                .show();
    }

    @Override
    public void onNameChange(String name) {
        deviceTextView.setText(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        communicator = ((CommunicatorServiceBinder) service).getCommunicator();

        if (communicator.setupSuccess()) {

            communicator.addListener(this);

            setupFileManager();

            setupBestFitManager();

            setupFragments();

            if (!isTablet()) {
                setupViewPager();
            }

            setupControlView();

            onDeviceConnection(communicator.getConnectedDevice());
        } else {
            communicator.showSetupProblemDialog(this);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // Nothing to do
    }

    private boolean isTablet() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
