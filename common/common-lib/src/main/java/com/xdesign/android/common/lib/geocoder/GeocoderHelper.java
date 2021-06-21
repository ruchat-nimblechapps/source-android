package com.xdesign.android.common.lib.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;

import com.xdesign.android.common.lib.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A helper class for the {@link Geocoder} service which will perform requests
 * asynchronously and come back with the result on the calling thread.
 * <p>
 * Please note that {@link QueryListener} is allowed to be GC'ed, so if
 * the calling activity goes out of scope then a callback will not be returned.
 */
public final class GeocoderHelper {

    private static final String TAG = GeocoderHelper.class.getSimpleName();

    private static final int EXECUTOR_THREADS = 4;
    private static final ExecutorService EXECUTOR =
            Executors.newFixedThreadPool(EXECUTOR_THREADS);

    private static final int GEOCODER_RESULTS = 1;

    private final Geocoder geocoder;
    private final Handler handler; // on calling thread

    private final String delimiter;
    private final String error;

    public GeocoderHelper(Context context) {
        this.geocoder = new Geocoder(context.getApplicationContext());
        this.handler = new Handler(Looper.myLooper());

        final TypedValue delimiterValue = new TypedValue();
        context.getTheme().resolveAttribute(
                R.attr.xd_common_geocoderHelperAddressLineDelimiter,
                delimiterValue,
                true);
        this.delimiter = delimiterValue.string.toString();

        final TypedValue errorValue = new TypedValue();
        context.getTheme().resolveAttribute(
                R.attr.xd_common_geocoderHelperAddressLineDelimiter,
                errorValue,
                true);
        this.error = errorValue.string.toString();
    }

    public void submitQuery(
            final Location location,
            final ResultFormat format,
            QueryListener listener) {
        EXECUTOR.submit(new SubmitLocationRunnable(location, format, listener));
    }

    public void submitQuery(
            final String locationName,
            final ResultFormat format,
            QueryListener listener) {
        EXECUTOR.submit(new SubmitLocationNameRunnable(locationName, format, listener));
    }

    private void onProcessAddresses(
            final List<Address> addresses,
            final ResultFormat format,
            final WeakReference<QueryListener> listener) {

        if (addresses.size() > 0) {
            final Address address = addresses.get(0);
            final int addressLines = address.getMaxAddressLineIndex();
            final StringBuilder builder = new StringBuilder(addressLines);
            for (int i = 0; i < addressLines; i++) {
                builder.append(address.getAddressLine(i));

                if (format == ResultFormat.FIRST_LINE) {
                    break;
                }

                if (i != (addressLines - 1)) {
                    builder.append(delimiter);
                }
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    final QueryListener l = listener.get();
                    if (l != null) {
                        l.onQueryFinished(builder.toString());
                    }
                }
            });
        } else {
            Log.w(TAG, Constants.ERROR_NO_ADDRESSES);

            onFail(listener);
        }
    }

    private void onFail(
            final WeakReference<QueryListener> listener) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                final QueryListener queryListener = listener.get();
                if (queryListener != null) {
                    queryListener.onQueryError(error);
                }
            }
        });
    }

    private class SubmitLocationRunnable implements Runnable {
        final Location location;
        final ResultFormat resultFormat;
        final WeakReference<QueryListener> listenerWeakReference;

        public SubmitLocationRunnable(
                Location location,
                ResultFormat resultFormat,
                QueryListener listener) {
            this.location = location;
            this.resultFormat = resultFormat;
            this.listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void run() {
            if (Geocoder.isPresent()) {
                final List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            GEOCODER_RESULTS);
                } catch (IOException exception) {
                    Log.e(TAG, Constants.ERROR_EXCEPTION, exception);

                    onFail(listenerWeakReference);
                    return;
                }

                onProcessAddresses(addresses, resultFormat, listenerWeakReference);
            } else {
                Log.w(TAG, Constants.ERROR_ABSENT);

                onFail(listenerWeakReference);
            }
        }
    }

    private class SubmitLocationNameRunnable implements Runnable {
        final String locationName;
        final ResultFormat resultFormat;
        final WeakReference<QueryListener> listenerWeakReference;

        public SubmitLocationNameRunnable(
                String locationName,
                ResultFormat resultFormat,
                QueryListener listener) {
            this.locationName = locationName;
            this.resultFormat = resultFormat;
            this.listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void run() {
            if (Geocoder.isPresent()) {
                final List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(
                            locationName, GEOCODER_RESULTS);
                } catch (IOException exception) {
                    Log.e(TAG, Constants.ERROR_EXCEPTION, exception);

                    onFail(listenerWeakReference);
                    return;
                }

                onProcessAddresses(addresses, resultFormat, listenerWeakReference);
            } else {
                Log.w(TAG, Constants.ERROR_ABSENT);

                onFail(listenerWeakReference);
            }
        }
    }
}
