package tie.hackathon.travelguide;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import Util.Constants;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BeaconManager beaconManager;
    private Region region;
    SharedPreferences s ;
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
        FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager
        fragment = new plan_journey_fragment();
        fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        TextView name,email;
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

//        name.setText(s.getString(Constants.USER_NAME,"Swati Garg"));
  //x      email.setText(s.getString(Constants.USER_EMAIL,"swati4star@gmail.com"));




        Intent i = getIntent();
        Boolean b = i.getBooleanExtra(Constants.IS_BEACON,false);

        Log.e("fdbvfdbfd",b+"   ");

        if(b){


            Intent i2 = new Intent(MainActivity.this,DetectedBeacon.class);
            i2.putExtra(Constants.CUR_UID,i.getStringExtra(Constants.CUR_UID));
            i2.putExtra(Constants.CUR_MAJOR,i.getStringExtra(Constants.CUR_MAJOR));
            i2.putExtra(Constants.CUR_MINOR,i.getStringExtra(Constants.CUR_MINOR));
           i2.putExtra(Constants.IS_BEACON,true);

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
                if (discovered == false && list.size()>0) {
                    Beacon nearestBeacon = list.get(0);
                    beaconmajor = Integer.toString(nearestBeacon.getMajor());
                    Log.e("Discovered", "Nearest places: " + nearestBeacon.getMajor());
                    discovered = true;
                    Intent i2 = new Intent(MainActivity.this,DetectedBeacon.class);
                    i2.putExtra(Constants.CUR_UID," ");
                    i2.putExtra(Constants.CUR_MAJOR,beaconmajor);
                    i2.putExtra(Constants.CUR_MINOR," ");
                    i2.putExtra(Constants.IS_BEACON,true);

                    startActivity(i2);


                }
            }


        });




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
        FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager

        if (id == R.id.nav_start) {

            fragment = new start_journey_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_plan) {

            fragment = new plan_journey_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_city) {

            fragment = new city_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_checklist) {
            fragment = new CheckList_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        } else if (id == R.id.nav_changecity) {
            Intent i = new Intent(MainActivity.this,SelectCity.class);
            startActivity(i);

        }else if (id == R.id.nav_signout) {

            e.putBoolean(Constants.FIRST_TIME, true);
            e.commit();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();


        }else if (id == R.id.nav_emergency ) {
            fragment = new Emergency_fragment();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
