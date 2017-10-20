package tie.hackathon.travelguide;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;

import utils.Constants;
import utils.GPSTracker;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlacesOnMap extends AppCompatActivity {

    @BindView(R.id.lv) TwoWayView lv;

    private String id;
    private String name;
    private Intent i;
    private String deslon;
    private String deslat;
    private String curlat;
    private String curlon;
    private String type;

    private com.google.android.gms.maps.MapFragment mapFragment;
    private ProgressDialog progressDialog;
    private int mode;
    private int icon;
    private GoogleMap map;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_on_map);

        ButterKnife.bind(this);

        i           = getIntent();
        name        = i.getStringExtra("name_");
        id          = i.getStringExtra("id_");
        type        = i.getStringExtra("type_");
        mHandler    = new Handler(Looper.getMainLooper());

        setTitle(name);
        switch (type) {
            case "restaurant":
                mode = 0;
                icon = R.drawable.restaurant;
                break;
            case "hangout":
                mode = 1;
                icon = R.drawable.hangout;
                break;
            case "monument":
                mode = 2;
                icon = R.drawable.monuments;
                break;
            default:
                mode = 4;
                icon = R.drawable.shopping;
                break;
        }

        deslat = i.getStringExtra("lat_");
        deslon = i.getStringExtra("lng_");


        getPlaces();

        this.mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

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
            LatLng coordinate = new LatLng(Double.parseDouble(curlat), Double.parseDouble(curlon));
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
            map.animateCamera(yourLocation);

        }

        setTitle("Places");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private void ShowMarker(Double LocationLat, Double LocationLong, String LocationName) {
        LatLng Coord = new LatLng(LocationLat, LocationLong);
        if (ContextCompat.checkSelfPermission(PlacesOnMap.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(Coord, 14));

                MarkerOptions abc = new MarkerOptions();
                MarkerOptions x = abc
                        .title(LocationName)
                        .position(Coord)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_drop_black_24dp));
                map.addMarker(x);

            }
        }
    }

    private void getPlaces() {

        progressDialog = new ProgressDialog(PlacesOnMap.this);
        progressDialog.setMessage("Fetching data, Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        // to fetch city names
        String uri = Constants.apilink + "places-api.php?lat=" + deslat + "&lng=" + deslon + "&mode=" + mode;
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
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = response.body().string();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject YTFeed = new JSONObject(res);

                            JSONArray YTFeedItems = YTFeed.getJSONArray("results");
                            Log.e("response", YTFeedItems + " ");


                            lv.setAdapter(new City_info_adapter(PlacesOnMap.this, YTFeedItems, icon));

                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage() + " ");
                        }
                    }
                });

            }
        });
    }

    public class City_info_adapter extends BaseAdapter {

        final Context context;
        final JSONArray FeedItems;
        final int rd;
        LinearLayout b2;
        private LayoutInflater inflater = null;

        public City_info_adapter(Context context, JSONArray FeedItems, int r) {
            this.context = context;
            this.FeedItems = FeedItems;
            rd = r;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return FeedItems.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            try {
                return FeedItems.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.city_infoitem, null);

            TextView Title = (TextView) vi.findViewById(R.id.item_name);
            TextView Description = (TextView) vi.findViewById(R.id.item_address);
            LinearLayout onmap = (LinearLayout) vi.findViewById(R.id.map);
            b2 = (LinearLayout) vi.findViewById(R.id.b2);


            try {
                Title.setText(FeedItems.getJSONObject(position).getString("name"));
                Description.setText(FeedItems.getJSONObject(position).getString("address"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("eroro", e.getMessage() + " ");
            }

            ImageView iv = (ImageView) vi.findViewById(R.id.image);
            iv.setImageResource(rd);

            onmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent browserIntent;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                                FeedItems.getJSONObject(position).getString("name") +
                                "+(name)+@" +
                                FeedItems.getJSONObject(position).getString("lat") +
                                "," +
                                FeedItems.getJSONObject(position).getString("lng")
                        ));
                        context.startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent;
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/"));
                    context.startActivity(browserIntent);
                }
            });

            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map.clear();
                    try {
                                ShowMarker(Double.parseDouble(FeedItems.getJSONObject(position).getString("lat")),
                                Double.parseDouble(FeedItems.getJSONObject(position).getString("lng")),
                                FeedItems.getJSONObject(position).getString("name")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return vi;
        }
    }
}
