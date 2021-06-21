package com.xdesign.android.common.lib.app.delegates;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.xdesign.android.common.lib.R;
import com.xdesign.android.common.lib.utils.ExceptionLogger;
import com.xdesign.android.common.lib.utils.IntentUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

// TODO copy selected files if they're not local
/**
 * {@link Delegate} which takes care of taking a picture using the camera or selecting a picture
 * from an app (ie Gallery). Takes care of creating any necessary temp files, saving/restoring an
 * instance, and cleaning up temp files (unless disabled).
 * <p>
 * Should be built through {@link Builder}.
 * <p>
 * Should be invoked (ie on a button press) through {@link #invoke()}.
 * <p>
 * The result, whether a success or a failure, will be notified through {@link WithSelected}.
 *
 * @see Delegate
 */
public final class TakeOrPickPictureDelegate extends Delegate {

    private static final String INSTANCE_FILES =
            TakeOrPickPictureDelegate.class.getSimpleName() + "_files";
    private static final String INSTANCE_URI =
            TakeOrPickPictureDelegate.class.getSimpleName() + "_uri";

    /**
     * Some older Sony devices appear to use this action for a {@link Intent} coming back
     * from the camera app.
     */
    private static final String INTENT_ACTION_CAMERA_SONY = "inline-data";

    private final ArrayList<File> files = new ArrayList<>();

    private final Activity activity;
    private final int requestCode;

    @StringRes
    private final int pickerTitle;

    private final File directory;
    private final String prefix;
    private final String extension;

    private final WithSelected action;

    private final boolean cleanUp;

    @Nullable
    private Uri uri;

    private TakeOrPickPictureDelegate(
            Activity activity,
            int requestCode,
            @StringRes int pickerTitle,
            File directory,
            String prefix,
            String extension,
            WithSelected action,
            boolean cleanUp) {

        this.activity = activity;
        this.requestCode = requestCode;

        this.pickerTitle = pickerTitle;

        this.directory = directory;
        this.prefix = prefix;
        this.extension = extension;

        this.action = action;

        this.cleanUp = cleanUp;
    }

    @Override
    public void onDestroy() {
        if (cleanUp) {
            for (final File file : files) {
                if (file.delete()) {
                    Log.d(getTag(), "Deleted temp file " + file);
                } else {
                    Log.w(getTag(), "Failed to delete temp file " + file);
                }
            }
        }
    }

    @Override
    public void onSaveState(Bundle outState) {
        if (!files.isEmpty()) {
            outState.putSerializable(INSTANCE_FILES, files);
        }

        if (uri != null) {
            outState.putParcelable(INSTANCE_URI, uri);
        }
    }

    @Override
    public void onRestoreState(Bundle inState) {
        if (inState.containsKey(INSTANCE_FILES)) {
            files.addAll((ArrayList<File>) inState.getSerializable(INSTANCE_FILES));
        }

        if (inState.containsKey(INSTANCE_URI)) {
            uri = inState.getParcelable(INSTANCE_URI);
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == this.requestCode) {
            final boolean fromCamera;
            if (data == null || data.getData() == null) {
                fromCamera = true;
            } else {
                final String action = data.getAction();
                fromCamera = (action != null)
                        && (action.equals(MediaStore.ACTION_IMAGE_CAPTURE)
                        || action.equals(INTENT_ACTION_CAMERA_SONY));
            }

            final Uri selectedUri;
            if (fromCamera) {
                selectedUri = uri;
            } else {
                selectedUri = data.getData();
            }

            if (selectedUri != null) {
                action.perform(selectedUri);
            } else {
                Log.w(getTag(), "Resolved uri was null");
                action.failed();
            }

            return true;
        }

        return super.onActivityResult(requestCode, resultCode, data);
    }

    public void invoke() {
        final File tempFile;
        try {
            tempFile = File.createTempFile(
                    prefix + UUID.randomUUID().toString(),
                    extension,
                    directory);

            Log.d(getTag(), "Created temp file " + tempFile);
        } catch (IOException e) {
            ExceptionLogger.log(e, getTag(), "Failed to create temp file");

            action.failed();
            return;
        }

        files.add(tempFile);
        uri = Uri.fromFile(tempFile);

        activity.startActivityForResult(
                IntentUtils.getCameraAndGalleryChooserIntent(
                        activity.getPackageManager(),
                        uri,
                        activity.getString(pickerTitle)),
                requestCode);
    }

    public static abstract class WithSelected {

        /**
         * Will be called when the invocation operation succeeds.
         *
         * @param uri the image selected by the user as a {@link Uri}
         */
        public abstract void perform(Uri uri);

        /**
         * Will be called when the invocation operation fails.
         */
        public void failed() {
            // NOP
        }
    }

    public final static class Builder {

        private final Activity activity;
        private final int requestCode;
        private final WithSelected action;

        private File directory;

        @StringRes
        private int pickerTitle = R.string.xd_common_title_pick_image;
        private String prefix = "";
        private String extension = "";
        private boolean cleanUp = true;

        public Builder(
                Activity activity,
                int requestCode,
                WithSelected action) {

            this.activity = activity;
            this.requestCode = requestCode;
            this.action = action;

            directory = activity.getExternalCacheDir();
        }

        public Builder directory(File value) {
            directory = value;
            return this;
        }

        public Builder prefix(String value) {
            prefix = value;
            return this;
        }

        public Builder extension(String value) {
            extension = value;
            return this;
        }

        public Builder pickerTitle(@StringRes int value) {
            pickerTitle = value;
            return this;
        }

        public Builder cleanUp(boolean value) {
            cleanUp = value;
            return this;
        }

        public TakeOrPickPictureDelegate build() {
            validate();

            return new TakeOrPickPictureDelegate(
                    activity,
                    requestCode,
                    pickerTitle,
                    directory,
                    prefix,
                    extension,
                    action,
                    cleanUp);
        }

        private void validate() {
            if (requestCode <= 0) {
                throw new IllegalArgumentException("requestCode must be non-zero and positive");
            }
            if (action == null) {
                throw new IllegalArgumentException("action cannot be null");
            }

            if (directory == null) {
                throw new IllegalArgumentException("directory cannot be null");
            }
            if (prefix == null) {
                throw new IllegalArgumentException("prefix cannot be null");
            }
            if (extension == null) {
                throw new IllegalArgumentException("extension cannot be null");
            }
        }
    }
}
