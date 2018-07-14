package utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Objects;

/**
 * Created by swati garg on 01-06-2015.
 */
public class GPSTracker extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute
    private final Context mContext;
    private boolean mCanGetLocation = false;
    private Location mLocation; // mLocation
    private double mLatitude; // mLatitude
    private double mLongitude; // mLongitude
    private static final int REQUEST_LOCATION = 199;

    public GPSTracker() {
        this.mContext = null;
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    private Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = Objects.requireNonNull(locationManager)
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.mCanGetLocation = true;
                // First get mLocation from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    mLocation = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null) {
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                        Log.v("GPS(network)", mLatitude + " " + mLongitude);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (mLocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        mLocation = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                            Log.v("GPS(gps)", mLatitude + " " + mLongitude);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mLocation;

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }

        // return mLatitude
        return mLatitude;
    }

    /**
     * Function to get mLongitude
     */
    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }

        // return mLongitude
        return mLongitude;
    }


    public boolean canGetLocation() {
        return this.mCanGetLocation;
    }

    /**
     * Function to switch GPS on
     */
    public void displayLocationRequest(Context context) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000 / 2);
        LocationSettingsRequest.Builder mBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        mBuilder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> mPendingResult =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mBuilder.build());
        mPendingResult.setResultCallback(locationSettingsResult -> {
            final Status mStatus = locationSettingsResult.getStatus();
            switch (mStatus.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    //All location settings are satisfied
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    //Display dialog to update location settings
                    try {
                        mStatus.startResolutionForResult((Activity) context, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        //Unable to execute request
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    //Dialog cannot be displayed
                    break;
            }
        });
    }
}