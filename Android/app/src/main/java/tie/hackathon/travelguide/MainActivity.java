package tie.hackathon.travelguide;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import utils.Constants;
import tie.hackathon.travelguide.login.LoginActivity;

/**
 * Launcher Activity; Handles fragment changes;
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences   sharedPreferences;
    private Boolean             discovered = false;
    private String              beaconmajor;
    private BeaconManager       beaconManager;
    private Region              region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Initially city fragment
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new CityFragment();
        fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        // If beacon detected, open activity
        final Intent intent = getIntent();
        if (intent.getBooleanExtra(Constants.IS_BEACON, false)) {
            Intent intent1 = new Intent(MainActivity.this, DetectedBeacon.class);
            intent1.putExtra(Constants.CUR_UID, intent.getStringExtra(Constants.CUR_UID));
            intent1.putExtra(Constants.CUR_MAJOR, intent.getStringExtra(Constants.CUR_MAJOR));
            intent1.putExtra(Constants.CUR_MINOR, intent.getStringExtra(Constants.CUR_MINOR));
            intent1.putExtra(Constants.IS_BEACON, true);
            startActivity(intent1);
        }


        /*// Start beacon ranging
        beaconManager = new BeaconManager(this);
        region = new Region("Minion region", UUID.fromString(Constants.UID), null, null);

        beaconManager.connect(() -> beaconManager.startRanging(region));

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region1, List<Beacon> list) {
                if (!discovered && list.size() > 0) {
                    Beacon nearestBeacon = list.get(0);
                    beaconmajor = Integer.toString(nearestBeacon.getMajor());
                    Log.e("Discovered", "Nearest places: " + nearestBeacon.getMajor());
                    discovered = true;
                    Intent intent1 = new Intent(MainActivity.this, DetectedBeacon.class);
                    intent1.putExtra(Constants.CUR_UID, " ");
                    intent1.putExtra(Constants.CUR_MAJOR, beaconmajor);
                    intent1.putExtra(Constants.CUR_MINOR, " ");
                    intent1.putExtra(Constants.IS_BEACON, true);
                    MainActivity.this.startActivity(intent1);
                }
            }

        });*/

        // Get runtime permissions for Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.VIBRATE,

                }, 0);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Change fragment on selecting naviagtion drawer item
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_travel) {

            fragment = new TravelFragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_city) {

            fragment = new CityFragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_utility) {

            fragment = new UtilitiesFragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_changecity) {

            Intent i = new Intent(MainActivity.this, SelectCity.class);
            startActivity(i);

        } else if (id == R.id.nav_emergency) {

            fragment = new EmergencyFragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_signout) {

            sharedPreferences
                    .edit()
                    .putString(Constants.USER_ID, null)
                    .apply();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
