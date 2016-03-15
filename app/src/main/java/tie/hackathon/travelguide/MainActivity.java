package tie.hackathon.travelguide;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import Util.Constants;
import Util.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BeaconManager beaconManager;
    private Region region;
    SharedPreferences s;
    Boolean discovered = false;
    SharedPreferences.Editor e;
    String beaconmajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new travel_fragment();
        fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();


        Intent i = getIntent();
        Boolean b = i.getBooleanExtra(Constants.IS_BEACON, false);


        if (b) {


            Intent i2 = new Intent(MainActivity.this, DetectedBeacon.class);
            i2.putExtra(Constants.CUR_UID, i.getStringExtra(Constants.CUR_UID));
            i2.putExtra(Constants.CUR_MAJOR, i.getStringExtra(Constants.CUR_MAJOR));
            i2.putExtra(Constants.CUR_MINOR, i.getStringExtra(Constants.CUR_MINOR));
            i2.putExtra(Constants.IS_BEACON, true);
            startActivity(i2);

        }


        beaconManager = new BeaconManager(this);
        region = new Region("Minion region", UUID.fromString(Constants.UID), null, null);


        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });


        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (discovered == false && list.size() > 0) {
                    Beacon nearestBeacon = list.get(0);
                    beaconmajor = Integer.toString(nearestBeacon.getMajor());
                    Log.e("Discovered", "Nearest places: " + nearestBeacon.getMajor());
                    discovered = true;
                    Intent i2 = new Intent(MainActivity.this, DetectedBeacon.class);
                    i2.putExtra(Constants.CUR_UID, " ");
                    i2.putExtra(Constants.CUR_MAJOR, beaconmajor);
                    i2.putExtra(Constants.CUR_MINOR, " ");
                    i2.putExtra(Constants.IS_BEACON, true);

                    startActivity(i2);


                }
            }


        });


        String isid = s.getString(Constants.UID, null);
        if (isid == null)
            new getloginid().execute();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (id == R.id.nav_travel) {

            fragment = new travel_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_enter) {

            fragment = new entertainment_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_city) {

            fragment = new city_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();


           /* Intent i = new Intent(MainActivity.this, FinalCityInfo.class);
            startActivity(i); */


        } else if (id == R.id.nav_utility) {

            fragment = new utilities_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();
        } else if (id == R.id.nav_changecity) {
            Intent i = new Intent(MainActivity.this, SelectCity.class);
            startActivity(i);

        } else if (id == R.id.nav_emergency) {
            fragment = new Emergency_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class getloginid extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String id = telephonyManager.getDeviceId();
                String uri = "http://csinsit.org/prabhakar/tie/login.php?device_id=" + id;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here", url + readStream + " ");
                return readStream;
            } catch (Exception e) {
                Log.e("here", e.getMessage() + " ");
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("result is", result + " ");

            if (result == null)
                return;

            try {
                JSONObject json = new JSONObject(result);
                String uid = json.getString("user_id");
                e.putString(Constants.UID, uid);
                e.commit();
                Log.e("here", "commitin" + s.getString(Constants.UID, null));
            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
        }

    }

}
