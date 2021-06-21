package com.xdesign.android.common.lib.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public final class UriUtils {

    private static final String TAG = UriUtils.class.getSimpleName();

    private static final String SCHEME_SEPARATOR = "://";

    private static final String SCHEME_FILE = "file";
    private static final String SCHEME_HTTP = "http";
    private static final String SCHEME_HTTPS = "https";

    private static final String FILE_IMAGE_PREFIX_TEMP = "temp-";
    private static final String FILE_IMAGE_EXTENSION = ".jpg";

    private static final String URI_GOOGLE_PHOTOS_PREFIX =
            "content://com.google.android.apps.photos.content";

    private static final int TEMP_QUALITY = 90;
    private static final Bitmap.CompressFormat TEMP_FORMAT = Bitmap.CompressFormat.JPEG;

    public static Uri getFileUri(Context context, Uri uri) {
        return Uri.parse(SCHEME_FILE + SCHEME_SEPARATOR + getFilePath(context, uri));
    }

    public static String getFilePath(Context context, Uri uri) {
        String filePath = null;

        if (uri.toString().startsWith(URI_GOOGLE_PHOTOS_PREFIX)) {
            /*
             * This is an annoying workaround for when the Photos app doesn't
             * give us a Uri to the file through the content resolver, but
             * instead we get access to the input stream of the file.
             */

            try {
                final InputStream inputStream = context.getContentResolver().openInputStream(uri);
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                final File tempFile = File.createTempFile(
                        FILE_IMAGE_PREFIX_TEMP + UUID.randomUUID().toString(),
                        FILE_IMAGE_EXTENSION,
                        context.getExternalCacheDir());
                final FileOutputStream fos = new FileOutputStream(tempFile);

                bitmap.compress(TEMP_FORMAT, TEMP_QUALITY, fos);

                filePath = tempFile.getPath();
            } catch (IOException e) {
                ExceptionLogger.log(e, TAG);
            }
        } else {
            String[] projection = { MediaStore.Images.Media.DATA };
            final Cursor cursor = context.getContentResolver().query(
                    uri,
                    projection,
                    null,
                    null,
                    null);

            if (cursor == null) {
                filePath = uri.getPath();
            } else {
                cursor.moveToFirst();

                filePath = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));

                cursor.close();
            }
        }

        return filePath;
    }

    public static String getFilename(Context context, Uri uri) {
        final String path = getFilePath(context, uri);

        return path.substring(path.lastIndexOf(File.separator) + 1);
    }

    public static String getExtension(Context context, Uri uri) {
        final String path = getFilePath(context, uri);

        return path.substring(path.lastIndexOf('.') + 1);
    }

    public static boolean isLocal(Uri uri) {
        final String scheme = uri.getScheme();

        return (scheme != null && scheme.equals(SCHEME_FILE));
    }

    public static boolean isRemote(Uri uri) {
        final String scheme = uri.getScheme();

        return (scheme != null && (scheme.equals(SCHEME_HTTP) || scheme.equals(SCHEME_HTTPS)));
    }

    private UriUtils() {}
}
