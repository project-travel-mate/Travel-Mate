package tie.hackathon.travelguide;

/**
 * Created by swati on 12/2/16.
 */

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import Util.Constants;

public class MyApplication extends Application {

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                showNotification(
                        getResources().getString(R.string.app_name),
                        "Hello there! Visit this great monument :D", list.get(0).getMajor(), list.get(0).getMinor(), list.get(0).getProximityUUID().toString());
            }

            @Override
            public void onExitedRegion(Region region) {
                showNotification(
                        "Good Bye!", "Hope to see you again :)", region.getMajor(), region.getMinor(), region.getProximityUUID().toString());
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString(Constants.UID),
                        Constants.major, Constants.minor));
            }

        });
    }

    /**
     * Show notification
     *
     * @param title   title
     * @param message Message
     * @param major   Beacon major
     * @param minor   Beacon minor
     * @param uid     user id
     */
    public void showNotification(String title, String message, int major, int minor, String uid) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.putExtra(Constants.CUR_UID, uid);
        notifyIntent.putExtra(Constants.CUR_MAJOR, major);
        notifyIntent.putExtra(Constants.CUR_MINOR, minor);
        notifyIntent.putExtra(Constants.IS_BEACON, true);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

}