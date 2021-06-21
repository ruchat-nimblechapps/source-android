package com.xdesign.android.mystrica.viewholders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.xdesign.android.common.lib.viewholders.AbstractViewHolder;
import com.xdesign.android.mystrica.Mystrica;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.util.FileUtils;
import com.xdesign.android.mystrica.views.RenameFileView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author keithkirk
 */
public class SessionViewHolder extends AbstractViewHolder<File> {

    private static final String MODIFIED_FORMAT = "dd-MM-yyyy hh:mm";

    @BindView(R.id.myst_text_view_filename)
    protected TextView filenameTextView;

    @BindView(R.id.myst_text_view_modfied)
    protected TextView modifiedTextView;

    private final Context context;
    private final Listener listener;
    private final boolean writeEnabled;

    private RenameFileView renameFileView;
    private File file;

    private DialogInterface.OnClickListener renameFilePositive
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final File newFile = FileUtils.renameFile(file, renameFileView.getName());

            listener.onFileRenamed(file, newFile);

            file = newFile;

            setDisplayedDetails();

            Mystrica.dismissKeyboard(context, renameFileView);
            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener deleteFilePositive
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            FileUtils.deleteFile(file);
            listener.onFileDeleted(file);

            dialog.dismiss();
        }
    };

    private DialogInterface.OnClickListener negativeListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            if (renameFileView != null) {
                Mystrica.dismissKeyboard(context, renameFileView);
            }

            dialog.dismiss();
        }
    };

    public SessionViewHolder(Context context, Listener listener, boolean writeEnabled) {
        this.context = context;
        this.listener = listener;
        this.writeEnabled = writeEnabled;
    }

    public View createView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayout(), parent, false);
        ButterKnife.bind(this, view);
        view.setTag(this);
        return view;
    }

    @Override
    public int getLayout() {
        return R.layout.myst_view_file_list_item;
    }

    @Override
    public void setup(File file) {
        this.file = file;

        setDisplayedDetails();
    }

    @OnClick(R.id.myst_button_edit)
    protected void editClicked() {
        if (writeEnabled) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            renameFileView = new RenameFileView(builder.getContext());
            renameFileView.setNameHint(FileUtils.getFileNameWithoutExtension(file));

            builder
                    .setTitle(R.string.myst_dialog_title_edit_file)
                    .setMessage(R.string.myst_dialog_body_edit_file)
                    .setView(renameFileView)
                    .setPositiveButton(R.string.myst_dialog_button_positive_edit_file,
                            renameFilePositive)
                    .setNegativeButton(R.string.myst_dialog_button_negative_edit_file,
                            negativeListener)
                    .show();
        } else {
            Toast.makeText(context, R.string.myst_toast_file_rename_permission_fail, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @OnClick(R.id.myst_button_email)
    protected void emailClicked() {
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, FileUtils.getUrisForFile(file));
        intent.setType("text/html");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.myst_toast_email_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.myst_button_delete)
    protected void deleteClicked() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.myst_dialog_title_delete_file)
                .setMessage(R.string.myst_dialog_body_delete_file)
                .setPositiveButton(R.string.myst_dialog_button_positive_delete_file,
                        deleteFilePositive)
                .setNegativeButton(R.string.myst_dialog_button_negative_delete_file,
                        negativeListener)
                .show();
    }

    private void setDisplayedDetails() {
        final SimpleDateFormat format = new SimpleDateFormat(MODIFIED_FORMAT);
        final String modified = format.format(new Date(file.lastModified()));

        filenameTextView.setText(FileUtils.getFileNameWithoutExtension(file));
        modifiedTextView.setText(context.getString(R.string.myst_format_modified, modified));
    }

    public interface Listener {

        void onFileRenamed(File oldFile, File newFile);

        void onFileDeleted(File file);
    }
}
