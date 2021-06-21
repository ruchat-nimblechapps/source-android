package com.xdesign.android.mystrica.viewholders;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.xdesign.android.common.lib.viewholders.AbstractViewHolder;
import com.xdesign.android.mystrica.Mystrica;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.interfaces.Communicator;
import com.xdesign.android.mystrica.interfaces.DeviceChangeListener;
import com.xdesign.android.mystrica.interfaces.Device;
import com.xdesign.android.mystrica.views.RenameDeviceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author keithkirk
 */
public class DeviceViewHolder extends AbstractViewHolder<Device> implements DeviceChangeListener {

    @BindView(R.id.myst_text_view_device_name)
    protected TextView deviceTextView;

    @BindView(R.id.myst_image_view_edit)
    protected ImageView editImageView;

    private final Context context;
    private final Communicator communicator;

    private RenameDeviceView renameDeviceView;

    private DialogInterface.OnClickListener negativeListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Mystrica.dismissKeyboard(context, renameDeviceView);
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener positiveRenameListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            communicator.changeConnectedDeviceName(
                    renameDeviceView.getName(),
                    renameDeviceView.getPassword()
            );

            Mystrica.dismissKeyboard(context, renameDeviceView);
            dialog.dismiss();
        }
    };

    public DeviceViewHolder(Context context, Communicator communicator) {
        this.context = context;
        this.communicator = communicator;
    }

    public View createView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayout(), parent, false);
        ButterKnife.bind(this, view);
        view.setTag(this);
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.myst_view_device_list_item;
    }

    @Override
    public void setup(Device device) {
        device.addListener(this);

        deviceTextView.setText(device.getName());

        editImageView.setVisibility(device.isConnected() ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.myst_image_view_edit)
    protected void editClicked() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        renameDeviceView = new RenameDeviceView(builder.getContext());
        renameDeviceView.setNameHint(deviceTextView.getText().toString());

        builder
                .setTitle(R.string.myst_dialog_title_rename_device)
                .setMessage(R.string.myst_dialog_body_rename_device)
                .setView(renameDeviceView)
                .setPositiveButton(R.string.myst_dialog_button_positive_rename_device,
                        positiveRenameListener)
                .setNegativeButton(R.string.myst_dialog_button_negative_rename_device,
                        negativeListener)
                .show();

    }

    @Override
    public void onNameChange(String name) {
        deviceTextView.setText(name);
    }
}
