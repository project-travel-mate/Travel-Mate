package io.github.project_travel_mate;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import io.github.project_travel_mate.destinations.CityFragment;
import io.github.project_travel_mate.login.LoginActivity;
import io.github.project_travel_mate.travel.TravelFragment;
import io.github.project_travel_mate.travel.mytrips.MyTripsFragment;
import io.github.project_travel_mate.utilities.BugReportFragment;
import io.github.project_travel_mate.utilities.EmergencyFragment;
import io.github.project_travel_mate.utilities.UtilitiesFragment;

import static utils.Constants.USER_EMAIL;
import static utils.Constants.USER_TOKEN;

/**
 * Launcher Activity; Handles fragment changes;
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences mSharedPreferences;
    private int mPreviousMenuItemId;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreviousMenuItemId = R.id.nav_city; // This is default item

        //Initially city fragment
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = CityFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();

        // Get runtime permissions for Android M
        getRuntimePermissions();

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get reference to the navigation view header and email textview
        View navigationHeader = navigationView.getHeaderView(0);
        TextView emailTextView = navigationHeader.findViewById(R.id.email);
        // Fetch the user mail id from SharedPreferences
        String emailId = mSharedPreferences.getString(USER_EMAIL, getString(R.string.app_name));
        emailTextView.setText(emailId);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Change fragment on selecting naviagtion drawer item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == mPreviousMenuItemId) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        int id = item.getItemId();
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (id) {
            case R.id.nav_travel:
                fragment = TravelFragment.newInstance();
                break;

            case R.id.nav_mytrips:
                fragment = MyTripsFragment.newInstance();
                break;

            case R.id.nav_city:
                fragment = CityFragment.newInstance();
                break;

            case R.id.nav_utility:
                fragment = UtilitiesFragment.newInstance();
                break;

            case R.id.nav_emergency:
                fragment = EmergencyFragment.newInstance();
                break;

            case R.id.nav_signout: {

                //set AlertDialog before signout
                ContextThemeWrapper crt = new ContextThemeWrapper(this, R.style.AlertDialog);
                AlertDialog.Builder builder = new AlertDialog.Builder(crt);
                builder.setMessage(R.string.signout_message)
                        .setPositiveButton(R.string.positive_button,
                                (dialog, which) -> {
                                    mSharedPreferences
                                            .edit()
                                            .putString(USER_TOKEN, null)
                                            .apply();
                                    Intent i = LoginActivity.getStartIntent(MainActivity.this);
                                    startActivity(i);
                                    finish();
                                })
                        .setNegativeButton(android.R.string.cancel,
                                (dialog, which) -> {

                                });
                builder.create().show();
                break;

            }

            case R.id.nav_report_bug:
                fragment = BugReportFragment.newInstance();
                break;
        }

        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        mPreviousMenuItemId = item.getItemId();
        return true;
    }

    private void getRuntimePermissions() {
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
    }

    public void onClickProfile(View view) {
        Intent in = ProfileActivity.getStartIntent(MainActivity.this);
        startActivity(in);
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
