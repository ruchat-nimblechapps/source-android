package com.xdesign.android.common.lib.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import retrofit.mime.TypedFile;

public final class TypedMedia extends TypedFile {

    private static final String TAG = TypedMedia.class.getSimpleName();

    private final int bound;
    private final int quality;

    /**
     * @param mimeType  MIME type to be attached to the file
     * @param uri       {@link Uri} of the file to be send
     * @param bound     bounding dimension of the media to be uploaded
     * @param quality   JPEG quality level in the range [0..100]
     */
    public TypedMedia(
            String mimeType,
            Uri uri,
            int bound,
            int quality) {

        super(mimeType, new File(uri.getPath()));

        if (bound < 1) {
            throw new IllegalArgumentException("Bound needs to be more than 0");
        }
        if (bound % 2 != 0) {
            throw new IllegalArgumentException("Bound needs to be an even number");
        }
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("Quality needs to be between 0 and 100");
        }

        this.bound = bound;
        this.quality = quality;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        final Matrix matrix = new Matrix();

        final ExifInterface exif = new ExifInterface(file().getAbsolutePath());
        switch (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.postTranslate(-1, 1);
                break;

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.postTranslate(1, -1);
                break;

            case ExifInterface.ORIENTATION_TRANSPOSE:
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.postTranslate(-1, -1);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            case ExifInterface.ORIENTATION_UNDEFINED:
            default:
                // NOP
        }

        final BitmapFactory.Options sourceOptions = new BitmapFactory.Options();
        sourceOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file().getAbsolutePath(), sourceOptions);

        final int sourceWidth = sourceOptions.outWidth;
        final int sourceHeight = sourceOptions.outHeight;

        // http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
        int inSampleSize = 1;
        if (Math.max(sourceWidth, sourceHeight) > bound) {
            while (Math.max(
                    sourceWidth / inSampleSize,
                    sourceHeight / inSampleSize) > bound) {

                inSampleSize *= 2;
            }

            // go back one step since we don't want to be upscaling
            inSampleSize /= 2;

            // preserve the aspect ratio
            final int destWidth;
            final int destHeight;
            if (sourceWidth > sourceHeight) {
                destWidth = bound;
                destHeight = Math.round(
                        sourceHeight * ((float) destWidth / sourceWidth) / 2) * 2;
            } else {
                destHeight = bound;
                destWidth = Math.round(
                        sourceWidth * ((float) destHeight / sourceHeight) / 2) * 2;
            }

            matrix.preScale(
                    destWidth / ((float) sourceWidth / inSampleSize),
                    destHeight / ((float) sourceHeight / inSampleSize));

            Log.d(TAG, String.format(
                    Locale.ENGLISH,
                    "Resizing %1$s from %2$dx%3$d to %4$dx%5$d",
                    file().getPath(),
                    sourceWidth,
                    sourceHeight,
                    destWidth,
                    destHeight));
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // we'll risk cutting a few pixels off than adding blank ones
        final Bitmap newBitmap = Bitmap.createBitmap(
                BitmapFactory.decodeFile(file().getAbsolutePath(), options),
                0,
                0,
                (int) Math.floor((float) sourceWidth / inSampleSize),
                (int) Math.floor((float) sourceHeight / inSampleSize),
                matrix,
                true);

        final boolean compressed = newBitmap.compress(
                Bitmap.CompressFormat.JPEG,
                quality,
                outputStream);

        if (compressed) {
            Log.d(TAG, "Successfully compressed " + file().getPath());
        } else {
            Log.w(TAG, "Failed to compress " + file().getPath());

            super.writeTo(outputStream);
        }
    }
}
