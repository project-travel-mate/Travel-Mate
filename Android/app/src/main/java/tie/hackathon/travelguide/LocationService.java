package tie.hackathon.travelguide;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import utils.Constants;

/**
 * Created by swati on 17/10/15.
 * <p>
 * A Service that checks user's current latitude and longitude,
 * and displays a notification on a given one
 */

public class LocationService extends Service {
    private static final String BROADCAST_ACTION = "Hello World";
    private LocationManager locationManager;
    private MyLocationListener listener;
    private final Location previousBestLocation = null;

    private Intent intent;

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                runnable.run();

            }
        };
        t.start();
        return t;
    }

    /**
     * Returns distance between 2 latitudes and longitudes
     *
     * @param lat1 Location 1 latitude
     * @param lat2 Location 2 latitude
     * @param lon1 Location 1 longitude
     * @param lon2 Location 2 longitude
     * @return distance between 2 locations
     */
    private static double distance(double lat1, double lat2, double lon1,
                                   double lon2) {

        final int R = 6371; // Radius of the earth
        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(LocationService.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isBetterLocation(Location location, Location currentBestLocation) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocationService.this);
        Double m = distance(location.getLatitude(),
                Double.parseDouble(sharedPreferences.getString(Constants.DESTINATION_CITY_LAT, "0.0")),
                location.getLongitude(),
                Double.parseDouble(sharedPreferences.getString(Constants.DESTINATION_CITY_LON, "0.0")));
        return m < 5000;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        if (ContextCompat.checkSelfPermission(LocationService.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(listener);
        }
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {
            if (isBetterLocation(loc, previousBestLocation)) {
                loc.getLatitude();
                loc.getLongitude();

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(LocationService.this)
                                .setSmallIcon(R.drawable.google_travel_logo)
                                .setContentTitle("Destination almost reached")
                                .setContentText("Wake Up! Get Ready!")
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                int notificationId = 12345;

                Intent targetIntent = new Intent(LocationService.this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(LocationService.this,
                        0,
                        targetIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);
                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nManager.notify(notificationId, builder.build());

                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
                stopSelf();
            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
