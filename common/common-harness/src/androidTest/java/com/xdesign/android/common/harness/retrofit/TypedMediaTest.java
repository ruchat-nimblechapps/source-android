package com.xdesign.android.common.harness.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.xdesign.android.common.lib.retrofit.TypedMedia;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * We cannot test easily for the following cases:
 * 1) That the scaled bitmap is truly representative of the original in pixel terms
 * 2) That the quality has been applied correctly
 * <p>
 * Likewise, the following tests are only partially correct:
 * 1) Rotate 90/270
 * 2) Rotate 180, flip horizontal/vertical, transpose/transverse
 */
@RunWith(AndroidJUnit4.class)
public final class TypedMediaTest {

    private static final String TAG = TypedMediaTest.class.getSimpleName();

    private List<File> created;

    @Before
    public void before() {
        created = new ArrayList<>();
    }

    @After
    public void after() {
        for (final File file : created) {
            if (!file.delete()) {
                Log.w(TAG, "Failed to delete " + file);
            }
        }
        created = null;
    }

    @Test
    public void invalidBounds() throws IOException {
        for (final int bound : new int[] {-1, 0, 1, 3}) {
            try {
                new TypedMedia("mime", Uri.fromFile(createFile()), bound, 1);

                fail("Exception not thrown");
            } catch (IllegalArgumentException e) {
                // pass
            }
        }
    }

    @Test
    public void invalidQuality() throws IOException {
        for (final int quality : new int[] {-1, 101}) {
            try {
                new TypedMedia("mime", Uri.fromFile(createFile()), 2, quality);

                fail("Exception not thrown");
            } catch (IllegalArgumentException e) {
                // pass
            }
        }
    }

    @Test
    public void rotationUndefined() throws IOException {
        run(2, 1, 2, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(1, on.getHeight());
            }
        });
    }

    @Test
    public void rotationNormal() throws IOException {
        run(2, 1, 2, ExifInterface.ORIENTATION_NORMAL, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(1, on.getHeight());
            }
        });
    }

    @Test
    public void rotation90And270() throws IOException {
        run(2, 1, 2, ExifInterface.ORIENTATION_ROTATE_90, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(1, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });

        run(2, 1, 2, ExifInterface.ORIENTATION_ROTATE_270, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(1, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });
    }

    @Test
    public void rotation180AndFlipAndTrans() throws IOException {
        run(2, 1, 2, ExifInterface.ORIENTATION_FLIP_HORIZONTAL, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(1, on.getHeight());
            }
        });

        run(2, 1, 2, ExifInterface.ORIENTATION_FLIP_VERTICAL, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(1, on.getHeight());
            }
        });

        run(2, 1, 2, ExifInterface.ORIENTATION_TRANSPOSE, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(1, on.getHeight());
            }
        });

        run(2, 1, 2, ExifInterface.ORIENTATION_TRANSVERSE, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(1, on.getHeight());
            }
        });
    }

    @Test
    public void noScalingWhenBelowBounds() throws IOException {
        run(4, 4, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });

        run(4, 2, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });

        run(2, 4, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });

        run(2, 2, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });
    }

    @Test
    public void scalesWhenAboveBounds() throws IOException {
        run(8, 8, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });

        run(8, 4, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });

        run(4, 8, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });
    }

    @Test
    public void scalesToEvenDimensions() throws IOException {
        run(8, 5, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });

        run(8, 3, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(2, on.getHeight());
            }
        });

        run(5, 8, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });

        run(3, 8, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(2, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });
    }

    /**
     * 32 down to 4 should give us a sample of 4 before downscaling through
     * a matrix.
     */
    @Test
    public void scalesBySampling() throws IOException {
        run(32, 32, 4, new WithBitmap() {
            @Override
            public void perform(Bitmap on) {
                assertEquals(4, on.getWidth());
                assertEquals(4, on.getHeight());
            }
        });
    }

    private void run(int width, int height, int bound, WithBitmap action)
            throws IOException {

        run(width, height, bound, ExifInterface.ORIENTATION_UNDEFINED, action);
    }

    private void run(int width, int height, int bound, int orientation, WithBitmap action)
            throws IOException {

        final File file = createFile();
        new TypedMedia(
                "mime", Uri.fromFile(writeBitmap(width, height, orientation)), bound, 1)
                .writeTo(new FileOutputStream(file));

        action.perform(BitmapFactory.decodeFile((file.getPath())));
    }

    private File writeBitmap(int width, int height, int orientation) throws IOException {
        final File file = createFile();

        final int[] colors = new int[width * height];
        Arrays.fill(colors, Color.BLACK);

        Bitmap.createBitmap(
                colors, width, height, Bitmap.Config.RGB_565)
                .compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(file));

        if (orientation != ExifInterface.ORIENTATION_UNDEFINED) {
            final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
            exif.saveAttributes();
        }

        return file;
    }

    private File createFile() throws IOException {
        final File file = File.createTempFile(
                UUID.randomUUID().toString(),
                null,
                InstrumentationRegistry.getInstrumentation()
                        .getTargetContext().getExternalCacheDir());

        created.add(file);

        return file;
    }

    private interface WithBitmap {

        void perform(Bitmap on);
    }
}
