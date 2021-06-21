package com.xdesign.android.common.lib.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public final class IntentUtils {

    private static final String TYPE_IMAGE = "image/*";

    private static final Intent INTENT_IMAGE_CAPTURE = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    private static final Intent INTENT_GALLERY = new Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            .setType(TYPE_IMAGE);

    public static Intent getCameraChooserIntent(
            Uri outputUri,
            String chooserTitle) {

        final Intent intent = new Intent(INTENT_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        return Intent.createChooser(intent, chooserTitle);
    }

    public static Intent getGalleryChooserIntent(String chooserTitle) {
        return Intent.createChooser(INTENT_GALLERY, chooserTitle);
    }

    /**
     * Returns the chooser {@link Intent} containing camera and gallery
     * applications.
     */
    public static Intent getCameraAndGalleryChooserIntent(
            PackageManager packageManager,
            Uri outputUri,
            String chooserTitle) {

        final List<Intent> cameraIntents = new ArrayList<>();
        final List<ResolveInfo> resolveInfoList =
                packageManager.queryIntentActivities(INTENT_IMAGE_CAPTURE, 0);

        for (final ResolveInfo info : resolveInfoList) {
            final Intent intent = new Intent(INTENT_IMAGE_CAPTURE);

            intent.setComponent(new ComponentName(
                    info.activityInfo.packageName, info.activityInfo.name));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            cameraIntents.add(intent);
        }

        final Intent chooserIntent = Intent.createChooser(INTENT_GALLERY, chooserTitle);
        chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        return chooserIntent;
    }

    private IntentUtils() {}
}
