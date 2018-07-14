package io.github.project_travel_mate.destinations.description;

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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.EXTRA_MESSAGE_TYPE;
import static utils.Constants.HERE_API_APP_CODE;
import static utils.Constants.HERE_API_APP_ID;
import static utils.Constants.HERE_API_LINK;
import static utils.Utils.bitmapDescriptorFromVector;

public class PlacesOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.lv)
    TwoWayView twoWayView;
    @BindView(R.id.textViewNoItems)
    TextView textViewNoItems;

    private ProgressDialog mProgressDialog;
    private String mMode;
    private int mIcon;
    private GoogleMap mGoogleMap;
    private Handler mHandler;
    private City mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_on_map);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        String type = intent.getStringExtra(EXTRA_MESSAGE_TYPE);
        mHandler = new Handler(Looper.getMainLooper());

        setTitle(mCity.getNickname());

        switch (type) {
            case "restaurant":
                mMode = "eat-drink";
                mIcon = R.drawable.restaurant;
                break;
            case "hangout":
                mMode = "going-out,leisure-outdoor";
                mIcon = R.drawable.hangout;
                break;
            case "monument":
                mMode = "sights-museums";
                mIcon = R.drawable.monuments;
                break;
            default:
                mMode = "shopping";
                mIcon = R.drawable.shopping_icon;
                break;
        }

        getPlaces();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle("Places");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void showMarker(Double locationLat, Double locationLong, String locationName) {
        LatLng coord = new LatLng(locationLat, locationLong);
        if (ContextCompat.checkSelfPermission(PlacesOnMapActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 14));

                MarkerOptions temp = new MarkerOptions();
                MarkerOptions markerOptions = temp
                        .title(locationName)
                        .position(coord)
                        .icon(bitmapDescriptorFromVector(PlacesOnMapActivity.this, R.drawable.ic_pin_drop_black));
                mGoogleMap.addMarker(markerOptions);
            }
        }
    }

    private void getPlaces() {

        mProgressDialog = new ProgressDialog(PlacesOnMapActivity.this);
        mProgressDialog.setMessage("Fetching data, Please wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        // to fetch city names
        String uri = HERE_API_LINK + "?at=" + mCity.getLatitude() + "," + mCity.getLongitude() + "&mode=" + mMode
                + "&app_id=" + HERE_API_APP_ID + "&app_code=" + HERE_API_APP_CODE;
        Log.v("executing", "URI : " + uri);

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
                Log.v("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {
                    try {
                        JSONObject feed = new JSONObject(res);
                        feed = feed.getJSONObject("results");

                        JSONArray feedItems = feed.getJSONArray("items");
                        Log.v("response", feedItems.toString());

                        setupViewsAsNeeded(feedItems);

                        mProgressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR : ", "Message : " + e.getMessage());
                    }
                });

            }
        });
    }

    private void setupViewsAsNeeded(JSONArray feedItems) {
        if (feedItems.length() == 0) {
            textViewNoItems.setVisibility(View.VISIBLE);
            twoWayView.setVisibility(View.GONE);
        } else {
            twoWayView.setAdapter(new PlacesOnMapAdapter(PlacesOnMapActivity.this, feedItems, mIcon));
            textViewNoItems.setVisibility(View.GONE);
            twoWayView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
    }

    class PlacesOnMapAdapter extends BaseAdapter {

        final Context mContext;
        final JSONArray mFeedItems;
        final int mRd;
        private final LayoutInflater mInflater;

        PlacesOnMapAdapter(Context context, JSONArray feedItems, int r) {
            this.mContext = context;
            this.mFeedItems = feedItems;
            mRd = r;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mFeedItems.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return mFeedItems.getJSONObject(position);
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
            ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.city_infoitem, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();

            if (position == 0) {
                try {
                    Double latitude = Double.parseDouble(
                            mFeedItems.getJSONObject(position).getJSONArray("position").get(0).toString());
                    Double longitude = Double.parseDouble(
                            mFeedItems.getJSONObject(position).getJSONArray("position").get(1).toString());
                    showMarker(latitude,
                            longitude,
                            mFeedItems.getJSONObject(position).getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                holder.title.setText(mFeedItems.getJSONObject(position).getString("title"));
                String description = mFeedItems.getJSONObject(position).getString("vicinity");
                holder.description.setText(Html.fromHtml(description).toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", "Message : " + e.getMessage());
            }

            holder.iv.setImageResource(mRd);

            holder.onmap.setOnClickListener(view12 -> {

                Intent browserIntent;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                            mFeedItems.getJSONObject(position).getString("name") +
                            "+(name)+@" +
                            mFeedItems.getJSONObject(position).getString("lat") +
                            "," +
                            mFeedItems.getJSONObject(position).getString("lng")
                    ));
                    mContext.startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

            holder.linearLayout.setOnClickListener(view1 -> {
                Intent browserIntent;
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/"));
                mContext.startActivity(browserIntent);
            });

            view.setOnClickListener(v -> {
                mGoogleMap.clear();
                try {
                    Double latitude = Double.parseDouble(
                            mFeedItems.getJSONObject(position).getJSONArray("position").get(0).toString());
                    Double longitude = Double.parseDouble(
                            mFeedItems.getJSONObject(position).getJSONArray("position").get(1).toString());
                    showMarker(latitude,
                            longitude,
                            mFeedItems.getJSONObject(position).getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.item_name)
            TextView title;
            @BindView(R.id.item_address)
            TextView description;
            @BindView(R.id.image)
            ImageView iv;
            @BindView(R.id.map)
            LinearLayout onmap;
            @BindView(R.id.b2)
            LinearLayout linearLayout;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlacesOnMapActivity.class);
        return intent;
    }
}
