package io.github.project_travel_mate.travel.transport;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.DELHI_LAT;
import static utils.Constants.DELHI_LON;
import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.DESTINATION_CITY_LAT;
import static utils.Constants.DESTINATION_CITY_LON;
import static utils.Constants.MUMBAI_LAT;
import static utils.Constants.MUMBAI_LON;
import static utils.Constants.SOURCE_CITY;
import static utils.Constants.SOURCE_CITY_LAT;
import static utils.Constants.SOURCE_CITY_LON;
import static utils.Constants.maps_key;

/**
 * Show car directions between 2 cities
 */
public class CarDirectionsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final Double mFuelprice = 60.00;
    private final Double mMileageHatchback = 30.0;
    private final Double mMileageSedan = 18.0;
    private final Double mMileageSuv = 16.0;
    @BindView(R.id.travelcost1)
    TextView coste1;
    @BindView(R.id.travelcost2)
    TextView coste2;
    @BindView(R.id.travelcost3)
    TextView coste3;
    private String mSorcelat;
    private String mDeslat;
    private String mSorcelon;
    private String mDeslon;
    private String mSource;
    private String mDestination;
    private String mDistanceText;
    private Double mDistance;
    private Double mCost1;
    private Double mCost2;
    private Double mCost3;
    private ProgressDialog mProgressDialog;
    private GoogleMap mGoogleMap;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_directions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        mHandler = new Handler(Looper.getMainLooper());

        mSorcelat = s.getString(SOURCE_CITY_LAT, DELHI_LAT);
        mSorcelon = s.getString(SOURCE_CITY_LON, DELHI_LON);
        mDeslat = s.getString(DESTINATION_CITY_LAT, MUMBAI_LAT);
        mDeslon = s.getString(DESTINATION_CITY_LON, MUMBAI_LON);
        mSource = s.getString(SOURCE_CITY, "Delhi");
        mDestination = s.getString(DESTINATION_CITY, "MUmbai");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Car Directions");
        getDirections();

    }

    @OnClick(R.id.fab)
    void onClickFab() {
        String shareBody = "Lets plan a journey from " +
                mSource + " to " + mDestination + ". The distace between the two cities is "
                + mDistanceText;

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Travel Guide");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Choose"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private void showMarker(Double locationLat, Double locationLong, String locationName) {
        LatLng coord = new LatLng(locationLat, locationLong);

        if (mGoogleMap != null) {
            if (ContextCompat.checkSelfPermission(CarDirectionsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 5));
            }

            MarkerOptions abc = new MarkerOptions();
            MarkerOptions x = abc
                    .title(locationName)
                    .position(coord)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_drop_black_24dp));
            mGoogleMap.addMarker(x);

        }
    }


    /**
     * Calls API to get directions
     */
    private void getDirections() {

        // Show a dialog box
        mProgressDialog = new ProgressDialog(CarDirectionsActivity.this);
        mProgressDialog.setMessage("Fetching route, Please wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        String uri = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                mSorcelat + "," + mSorcelon + "&destination=" + mDeslat + "," + mDeslon +
                "&key=" +
                maps_key +
                "&mode=driving\n";

        Log.v("EXECUTING", uri);

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
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    Log.v("RESPONSE : ", "Done" + res);
                    try {
                        final JSONObject json = new JSONObject(res);
                        JSONArray routeArray = json.getJSONArray("routes");
                        JSONObject routes = routeArray.getJSONObject(0);
                        JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                        String encodedString = overviewPolylines.getString("points");
                        List<LatLng> list = decodePoly(encodedString);
                        Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(12)
                                .color(Color.parseColor("#05b1fb"))//Google maps blue color
                                .geodesic(true)
                        );
                        mDistance = routes.getJSONArray("legs")
                                .getJSONObject(0)
                                .getJSONObject("mDistance")
                                .getDouble("value");
                        mDistanceText = routes.getJSONArray("legs")
                                .getJSONObject(0)
                                .getJSONObject("mDistance")
                                .getString("text");


                        mCost1 = (mDistance / mMileageHatchback) * mFuelprice / 1000;
                        mCost2 = (mDistance / mMileageSedan) * mFuelprice / 1000;
                        mCost3 = (mDistance / mMileageSuv) * mFuelprice / 1000;

                        coste1.setText(mCost1.intValue());
                        coste2.setText(mCost2.intValue());
                        coste3.setText(mCost3.intValue());
                        for (int z = 0; z < list.size() - 1; z++) {
                            LatLng src = list.get(z);
                            LatLng dest1 = list.get(z + 1);
                            Polyline line2 = mGoogleMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(src.latitude, src.longitude),
                                            new LatLng(dest1.latitude, dest1.longitude))
                                    .width(2)
                                    .color(Color.BLUE).geodesic(true));
                        }
                        mProgressDialog.hide();
                        LatLng coordinate =
                                new LatLng(Double.parseDouble(mSorcelat), Double.parseDouble(mSorcelon));
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 5);
                        mGoogleMap.animateCamera(yourLocation);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                });
            }
        });
    }

    /**
     * Displays path on path
     *
     * @param encoded Encoded string that contains path
     * @return Points on map
     */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;

        showMarker(Double.parseDouble(mSorcelat), Double.parseDouble(mSorcelon), "SOURCE");
        showMarker(Double.parseDouble(mDeslat), Double.parseDouble(mDeslon), "DESTINATION");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CarDirectionsActivity.class);
        return intent;
    }
}
