package tie.hackathon.travelguide;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Constants;
import utils.GPSTracker;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Show markers on map around user's current location
 */
public class MapRealTimeActivity extends AppCompatActivity{

    @BindView(R.id.data) ScrollView sc;

    private com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap map;
    private SharedPreferences sharedPreferences;
    private int index = 0;
    private Handler mHandler;

    private String sorcelat;
    private String deslat;
    private String sorcelon;
    private String deslon;
    private String surce;
    private String dest;
    private String curlat;
    private String curlon;

    private List<String> name;
    private List<String> nums;
    private List<String> web;
    private List<String> addr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_realtime);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        this.mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();
        mHandler = new Handler(Looper.getMainLooper());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        sorcelat    = sharedPreferences.getString(Constants.SOURCE_CITY_LAT, Constants.DELHI_LAT);
        sorcelon    = sharedPreferences.getString(Constants.SOURCE_CITY_LON, Constants.DELHI_LON);
        deslat      = sharedPreferences.getString(Constants.DESTINATION_CITY_LAT, Constants.MUMBAI_LAT);
        deslon      = sharedPreferences.getString(Constants.DESTINATION_CITY_LON, Constants.MUMBAI_LON);
        surce       = sharedPreferences.getString(Constants.SOURCE_CITY, "Delhi");
        dest        = sharedPreferences.getString(Constants.DESTINATION_CITY, "Mumbai");

        sc.setVisibility(View.GONE);

        name    = new ArrayList<>();
        nums    = new ArrayList<>();
        web     = new ArrayList<>();
        addr    = new ArrayList<>();

        curlat = deslat;
        curlon = deslon;

        setTitle("Places");

        // Get user's current location
        GPSTracker tracker = new GPSTracker(this);
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
            Log.e("cdsknvdsl ", curlat + "dsbjvdks" + curlon);
        } else {
            curlat = Double.toString(tracker.getLatitude());
            curlon = Double.toString(tracker.getLongitude());
            Log.e("cdsknvdsl", tracker.getLatitude() + " " + curlat + "dsbjvdks" + curlon);
            if (curlat.equals("0.0")) {
                curlat = "28.5952242";
                curlon = "77.1656782";
            }
            getMarkers(0, R.drawable.ic_local_pizza_black_24dp);
        }

        // Zoom to current location
        LatLng coordinate = new LatLng(Double.parseDouble(curlat), Double.parseDouble(curlon));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
        map.animateCamera(yourLocation);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                sc.setVisibility(View.VISIBLE);
                for (int i = 0; i < name.size(); i++) {
                    if (name.get(i).equals(marker.getTitle())) {
                        index = i;
                        break;
                    }
                }

                TextView Title = (TextView) MapRealTimeActivity.this.findViewById(R.id.VideoTitle);
                TextView Description = (TextView) MapRealTimeActivity.this.findViewById(R.id.VideoDescription);
                final Button calls, book;
                calls = (Button) MapRealTimeActivity.this.findViewById(R.id.call);
                book = (Button) MapRealTimeActivity.this.findViewById(R.id.book);

                Title.setText(name.get(index));
                Description.setText(addr.get(index));
                calls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + nums.get(index)));
                        MapRealTimeActivity.this.startActivity(intent);

                    }
                });
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent;
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web.get(index)));
                        MapRealTimeActivity.this.startActivity(browserIntent);
                    }
                });
                return false;
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Calls API to get nearby places
     *
     * @param mo mode; type of places;
     * @param ic marker icon
     */
    private void getMarkers(int mo, final int ic) {

        String uri = Constants.apilink + "places-api.php?mode=" + mo + "&lat=" + curlat + "&lng=" + curlon;
        Log.e("executing", uri + " ");

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                final String res = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("YO", "Done");
                        try {
                            final JSONObject json = new JSONObject(res);
                            JSONArray routeArray = json.getJSONArray("results");

                            for (int i = 0; i < routeArray.length(); i++) {
                                name.add(routeArray.getJSONObject(i).getString("name"));
                                web.add(routeArray.getJSONObject(i).getString("website"));
                                nums.add(routeArray.getJSONObject(i).getString("phone"));
                                addr.add(routeArray.getJSONObject(i).getString("address"));
                                ShowMarker(Double.parseDouble(routeArray.getJSONObject(i).getString("lat")),
                                        Double.parseDouble(routeArray.getJSONObject(i).getString("lng")),
                                        routeArray.getJSONObject(i).getString("name"),
                                        ic);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage() + " ");
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        if (item.getItemId() == R.id.action_sort) {

            new MaterialDialog.Builder(this)
                    .title(R.string.title)
                    .items(R.array.items)
                    .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {

                            map.clear();
                            name.clear();
                            nums.clear();
                            web.clear();
                            addr.clear();

                            for (int i = 0; i < which.length; i++) {
                                Log.e("selected", which[i] + " " + text[i]);
                                Integer icon;
                                switch (which[0]) {
                                    case 0:
                                        icon = R.drawable.ic_local_pizza_black_24dp;
                                        break;
                                    case 1:
                                        icon = R.drawable.ic_local_bar_black_24dp;
                                        break;
                                    case 2:
                                        icon = R.drawable.ic_camera_alt_black_24dp;
                                        break;
                                    case 3:
                                        icon = R.drawable.ic_directions_bus_black_24dp;
                                        break;
                                    case 4:
                                        icon = R.drawable.ic_local_mall_black_24dp;
                                        break;
                                    case 5:
                                        icon = R.drawable.ic_local_gas_station_black_24dp;
                                        break;
                                    case 6:
                                        icon = R.drawable.ic_local_atm_black_24dp;
                                        break;
                                    case 7:
                                        icon = R.drawable.ic_local_hospital_black_24dp;
                                        break;
                                    default:
                                        icon = R.drawable.ic_attach_money_black_24dp;
                                        break;
                                }
                                MapRealTimeActivity.this.getMarkers(which[0], icon);

                            }
                            return true;
                        }
                    })
                    .positiveText(R.string.choose)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets marker at given location on map
     *
     * @param LocationLat  latitude
     * @param LocationLong longitude
     * @param LocationName name of location
     * @param LocationIcon icon
     */
    private void ShowMarker(Double LocationLat, Double LocationLong, String LocationName, Integer LocationIcon) {
        LatLng Coord = new LatLng(LocationLat, LocationLong);

        if (ContextCompat.checkSelfPermission(MapRealTimeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(Coord, 10));

                MarkerOptions abc = new MarkerOptions();
                MarkerOptions x = abc
                        .title(LocationName)
                        .position(Coord)
                        .icon(BitmapDescriptorFactory.fromResource(LocationIcon));
                map.addMarker(x);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }
}
