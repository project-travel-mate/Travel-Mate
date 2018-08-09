package io.github.project_travel_mate.travel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.MapItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.GPSTracker;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.HERE_API_MODES;
import static utils.Constants.USER_TOKEN;
import static utils.Utils.bitmapDescriptorFromVector;

/**
 * Show markers on map around user's current location
 */
public class MapViewRealTimeActivity extends AppCompatActivity implements OnMapReadyCallback, TravelmateSnackbars {

    private final List<MapItem> mMapItems = new ArrayList<>();
    @BindView(R.id.data)
    ScrollView scrollView;
    @BindView(R.id.animation)
    LottieAnimationView animationView;
    @BindView(R.id.layout)
    LinearLayout layout;
    private int mIndex = 0;
    private Handler mHandler;
    private String mToken;
    private String mCurlat;
    private String mCurlon;
    private GoogleMap mGoogleMap;
    private static final int REQUEST_LOCATION = 199;
    GPSTracker tracker;
    public ArrayList<Integer> mSelectedIndices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_realtime);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mHandler = new Handler(Looper.getMainLooper());

        scrollView.setVisibility(View.GONE);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        setTitle("Map View");

        // Get user's current location
        tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.displayLocationRequest(this);
        } else {
            mCurlat = Double.toString(tracker.getLatitude());
            mCurlon = Double.toString(tracker.getLongitude());
            showDialogToSelectitems();
        }
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Calls API to get nearby places
     *
     * @param mode mode; type of places;
     * @param icon marker icon
     */
    private void getMarkers(String mode, final int icon) {

        String uri = API_LINK_V2 + "get-places/" + mCurlat + "/" + mCurlon
                + "/" + mode;

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    try {
                        JSONArray routeArray = new JSONArray(res);

                        for (int i = 0; i < routeArray.length(); i++) {
                            String name = routeArray.getJSONObject(i).getString("title");
                            String web = routeArray.getJSONObject(i).getString("icon");
                            String nums = routeArray.getJSONObject(i).getString("distance");
                            String addr = routeArray.getJSONObject(i).getString("address");

                            Double latitude =
                                    routeArray.getJSONObject(i).getDouble("latitude");
                            Double longitude =
                                    routeArray.getJSONObject(i).getDouble("longitude");

                            showMarker(latitude, longitude, name, icon);
                            mMapItems.add(new MapItem(name, nums, web, addr));
                        }
                        animationView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkError();
                        Log.e("ERROR : ", e.getMessage() + " ");
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_sort:
                showDialogToSelectitems();
                return true;
            case R.id.action_list_view :
                finish();
                Intent intent = ListViewRealTimeActivity.getStartIntent(MapViewRealTimeActivity.this);
                startActivity(intent);
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Shows a dialog to allow user to select items to be displayed
     * on map
     */
    private void showDialogToSelectitems() {
        Integer[] selectedItems = new Integer[mSelectedIndices.size()];

        for (int i = 0; i < mSelectedIndices.size(); i++) {
            selectedItems[i] = mSelectedIndices.get(i);
        }
        mSelectedIndices.clear();
        new MaterialDialog.Builder(this)
                .title(R.string.title)
                .items(R.array.items)
                .itemsCallbackMultiChoice(selectedItems, (dialog, which, text) -> {

                    mGoogleMap.clear();
                    mMapItems.clear();

                    for (int i = 0; i < which.length; i++) {
                        Log.v("selected", which[i] + " " + text[i]);
                        mSelectedIndices.add(which[i]);
                        Integer icon;
                        switch (which[i]) {
                            case 0:
                                icon = R.drawable.ic_local_pizza_black;
                                break;
                            case 1:
                                icon = R.drawable.ic_local_bar_black;
                                break;
                            case 2:
                                icon = R.drawable.ic_camera_alt_black;
                                break;
                            case 3:
                                icon = R.drawable.ic_directions_bus_black;
                                break;
                            case 4:
                                icon = R.drawable.ic_local_mall_black;
                                break;
                            case 5:
                                icon = R.drawable.ic_local_gas_station_black;
                                break;
                            case 6:
                                icon = R.drawable.ic_local_atm_black;
                                break;
                            case 7:
                                icon = R.drawable.ic_local_hospital_black;
                                break;
                            default:
                                icon = R.drawable.ic_attach_money_black;
                                break;
                        }
                        MapViewRealTimeActivity.this.getMarkers(HERE_API_MODES.get(which[i]), icon);
                    }
                    return true;
                })
                .positiveText(R.string.choose)
                .show();
    }

    /**
     * Sets marker at given location on map
     *
     * @param locationLat  latitude
     * @param locationLong longitude
     * @param locationName name of location
     * @param locationIcon icon
     */
    private void showMarker(Double locationLat, Double locationLong, String locationName, Integer locationIcon) {
        LatLng coord = new LatLng(locationLat, locationLong);

        if (ContextCompat.checkSelfPermission(MapViewRealTimeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 10));

                MarkerOptions abc = new MarkerOptions();
                MarkerOptions x = abc
                        .title(locationName)
                        .position(coord)
                        .icon(bitmapDescriptorFromVector(MapViewRealTimeActivity.this, locationIcon));
                mGoogleMap.addMarker(x);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        mGoogleMap = map;

        if (mCurlat == null || mCurlon == null)
            return;

        // Zoom to current location
        LatLng coordinate = new LatLng(Double.parseDouble(mCurlat), Double.parseDouble(mCurlon));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
        map.animateCamera(yourLocation);

        map.setOnMarkerClickListener(marker -> {
            scrollView.setVisibility(View.VISIBLE);
            for (int i = 0; i < mMapItems.size(); i++) {
                if (mMapItems.get(i).getName().equals(marker.getTitle())) {
                    mIndex = i;
                    break;
                }
            }

            TextView title = MapViewRealTimeActivity.this.findViewById(R.id.item_title);
            TextView description = MapViewRealTimeActivity.this.findViewById(R.id.item_description);
            final Button calls, book;
            calls = MapViewRealTimeActivity.this.findViewById(R.id.call);
            book = MapViewRealTimeActivity.this.findViewById(R.id.book);

            title.setText(mMapItems.get(mIndex).getName());

            description.setText(android.text.Html.fromHtml(mMapItems.get(mIndex).getAddress()).toString());
            calls.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mMapItems.get(mIndex).getNumber()));
                MapViewRealTimeActivity.this.startActivity(intent);

            });
            book.setOnClickListener(view -> {
                Intent browserIntent;
                try {
                    browserIntent = new Intent(
                            Intent.ACTION_VIEW, Uri.parse(mMapItems.get(mIndex).getAddress()));
                    MapViewRealTimeActivity.this.startActivity(browserIntent);
                } catch (Exception e) {
                    TravelmateSnackbars.createSnackBar(findViewById(R.id.map_real_time),
                            R.string.no_activity_for_browser, Snackbar.LENGTH_LONG).show();
                }
            });
            return false;
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MapViewRealTimeActivity.class);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //User agreed to make required location settings changes
                        //startLocationUpdates();
                        TravelmateSnackbars.createSnackBar(findViewById(R.id.map_real_time),
                                R.string.location_enabled, Snackbar.LENGTH_LONG).show();
                        mCurlat = Double.toString(tracker.getLatitude());
                        mCurlon = Double.toString(tracker.getLongitude());
                        getMarkers("eat-drink", R.drawable.ic_local_pizza_black);

                        break;
                    case Activity.RESULT_CANCELED:
                        //User chose not to make required location settings changes
                        TravelmateSnackbars.createSnackBar(findViewById(R.id.map_real_time),
                                R.string.location_not_enabled, Snackbar.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        layout.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }
}