package io.github.project_travel_mate.destinations.description;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import objects.City;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.EXTRA_MESSAGE_TYPE;
import static utils.Constants.USER_TOKEN;
import static utils.Utils.bitmapDescriptorFromVector;

public class PlacesOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.lv)
    RecyclerView recyclerView;
    @BindView(R.id.textViewNoItems)
    TextView textViewNoItems;
    @BindView(R.id.place_name)
    TextView selectedItemName;
    @BindView(R.id.place_address)
    TextView selectedItemAddress;
    @BindView(R.id.item_info)
    LinearLayout linearLayout;

    private ProgressDialog mProgressDialog;
    private String mMode;
    private int mIcon;
    private int mIndex;
    private GoogleMap mGoogleMap;
    private Handler mHandler;
    private String mToken;
    private City mCity;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONArray mFeedItems;
    private List<Marker> mMarkerList = new ArrayList<>();
    private Marker mPreviousMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_on_map);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        String type = intent.getStringExtra(EXTRA_MESSAGE_TYPE);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

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
                //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 14));

                MarkerOptions temp = new MarkerOptions();
                MarkerOptions markerOptions = temp
                        .title(locationName)
                        .position(coord)
                        .icon(bitmapDescriptorFromVector(PlacesOnMapActivity.this,
                                R.drawable.ic_radio_button_checked_orange_24dp));
                Marker marker = mGoogleMap.addMarker(markerOptions);
                mMarkerList.add(marker);
            }
        }
    }

    private void getPlaces() {

        mProgressDialog = new ProgressDialog(PlacesOnMapActivity.this);
        mProgressDialog.setMessage("Fetching data, Please wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        String uri = API_LINK_V2 + "get-places/" + mCity.getLatitude() + "/" + mCity.getLongitude()
                + "/" + mMode;

        Log.v("executing", uri);

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
                Log.v("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {
                    try {
                        mFeedItems = new JSONArray(res);
                        for (int i = 0; i < mFeedItems.length(); i++ ) {
                            Double latitude =
                                    mFeedItems.getJSONObject(i).getDouble("latitude");
                            Double longitude =
                                    mFeedItems.getJSONObject(i).getDouble("longitude");
                            String name = mFeedItems.getJSONObject(i).getString("title");

                            showMarker(latitude, longitude, name);
                        }
                        setupViewsAsNeeded(mFeedItems);

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
            recyclerView.setVisibility(View.GONE);
        } else {
            // Specify a layout for RecyclerView
            // Create a vertical RecyclerView
            mLayoutManager = new LinearLayoutManager(PlacesOnMapActivity.this,
                    LinearLayoutManager.VERTICAL,
                    false
            );
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(new PlacesOnMapAdapter(PlacesOnMapActivity.this, feedItems, mIcon));
            textViewNoItems.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Highlights the marker whose card is clicked
     * @param position position of the marker in
     *                 mMarkerList whose card is clicked
     */
    private void highlightMarker(int position) {
        if (mPreviousMarker != null) {
            mPreviousMarker.setIcon(bitmapDescriptorFromVector(PlacesOnMapActivity.this,
                    R.drawable.ic_radio_button_checked_orange_24dp));
            //hide info about previous marker
            mPreviousMarker.hideInfoWindow();
        }
        Marker currentMarker = mMarkerList.get(position);
        //show info about current marker
        currentMarker.showInfoWindow();
        currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mPreviousMarker = currentMarker;
    }

    /**
     * Zooms in towards the marker whose card is clicked
     * @param position position of the item in
     *                 mFeedItems whose card is clicked
     */
    private void zoomToMarker(int position) {
        Double latitude = null, longitude = null;
        try {
            latitude =
                    mFeedItems.getJSONObject(position).getDouble("latitude");
            longitude =
                    mFeedItems.getJSONObject(position).getDouble("longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng coordinate = new LatLng(latitude, longitude);
        CameraUpdate selectedPosition = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
        mGoogleMap.animateCamera(selectedPosition);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        LatLng coordinate = new LatLng(Double.parseDouble(mCity.getLatitude()),
                Double.parseDouble(mCity.getLongitude()));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
        map.animateCamera(yourLocation);

        map.setOnMarkerClickListener(marker -> {
            try {
                for (int i = 0; i < mFeedItems.length(); i++) {
                    //get index of the clicked marker
                    if (mFeedItems.getJSONObject(i).getString("title").equals(marker.getTitle())) {
                        mIndex = i;
                        break;
                    }
                }
                linearLayout.setVisibility(View.VISIBLE);
                highlightMarker(mIndex);
                //set info about clicked marker
                selectedItemName.setText(marker.getTitle());
                String[] address = mFeedItems.getJSONObject(mIndex).getString("address").split("<br/>");
                if (address.length > 1) {
                    selectedItemAddress.setText(address[0] + ", " +  address[1]);
                } else {
                    selectedItemAddress.setText(address[0]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlacesOnMapActivity.class);
        return intent;
    }

    /**
     * Adapter for horizontal recycler view for displaying each cityInfoItem
     */
    class PlacesOnMapAdapter extends RecyclerView.Adapter<PlacesOnMapAdapter.ViewHolder> {

        final Context mContext;
        final JSONArray mFeedItems;
        final int mRd;

        PlacesOnMapAdapter(Context context, JSONArray feedItems, int r) {
            this.mContext = context;
            this.mFeedItems = feedItems;
            mRd = r;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (position == 0) {
                try {
                    Double latitude =
                            mFeedItems.getJSONObject(position).getDouble("latitude");
                    Double longitude =
                            mFeedItems.getJSONObject(position).getDouble("longitude");
                    showMarker(latitude,
                            longitude,
                            mFeedItems.getJSONObject(position).getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                holder.title.setText(mFeedItems.getJSONObject(position).getString("title"));
                String description = mFeedItems.getJSONObject(position).getString("address");
                holder.description.setText(Html.fromHtml(description).toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", "Message : " + e.getMessage());
            }

            holder.imageView.setImageResource(mRd);

            holder.onMap.setOnClickListener(view12 -> {

                Intent browserIntent;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                            mFeedItems.getJSONObject(position).getString("title") +
                            "+(name)+@" +
                            mFeedItems.getJSONObject(position).getString("latitude") +
                            "," +
                            mFeedItems.getJSONObject(position).getString("longitude")
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

            holder.completeLayout.setOnClickListener(v -> {
                try {
                    highlightMarker(position);
                    String[] address = mFeedItems.getJSONObject(position).getString("address").split("<br/>");
                    if (address.length > 1) {
                        selectedItemAddress.setText(address[0] + ", " +  address[1]);
                    } else {
                        selectedItemAddress.setText(address[0]);
                    }
                    zoomToMarker(position);
                    linearLayout.setVisibility(View.VISIBLE);
                    selectedItemName.setText(mFeedItems.getJSONObject(position).getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.city_infoitem, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return  holder;
        }

        @Override
        public int getItemCount() {
            return mFeedItems.length();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.item_name)
            TextView title;
            @BindView(R.id.item_address)
            TextView description;
            @BindView(R.id.image)
            ImageView imageView;
            @BindView(R.id.map)
            LinearLayout onMap;
            @BindView(R.id.know_more_layout)
            LinearLayout linearLayout;
            @BindView(R.id.city_info_item_layout)
            CardView completeLayout;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

        }
    }
}
