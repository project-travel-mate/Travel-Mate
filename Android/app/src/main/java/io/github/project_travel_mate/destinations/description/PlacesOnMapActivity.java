package io.github.project_travel_mate.destinations.description;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

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

public class PlacesOnMapActivity extends AppCompatActivity implements
        Marker.OnMarkerClickListener, TextWatcher {
    @BindView(R.id.editTextSearch)
    EditText editTextSearch;
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
    PlacesOnMapAdapter PlacesMapAdapter;
    private ProgressDialog mProgressDialog;
    private String mMode;
    private int mIcon;
    private int mIndex;
    private Handler mHandler;
    private String mToken;
    private City mCity;
    private JSONArray mFeedItems;
    private List<Marker> mMarkerList = new ArrayList<>();
    private Marker mPreviousMarker = null;
    BottomSheetBehavior sheetBehavior;
    @BindView(R.id.bottom_sheet)
    LinearLayout layoutBottomSheet;
    private MapView mMap;
    private IMapController mController;
    private Drawable mMarker, mDefaultMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_on_map);
        ButterKnife.bind(this);
        editTextSearch.addTextChangedListener(this);
        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        String type = intent.getStringExtra(EXTRA_MESSAGE_TYPE);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        mMap = findViewById(R.id.map);
        setTitle(mCity.getNickname());
        mMarker = this.getDrawable(R.drawable.ic_radio_button_checked_orange_24dp);
        mDefaultMarker = this.getDrawable(R.drawable.marker_default);
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
        initMap();
        setTitle("Places");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * On open street map initialize
     */
    private void initMap() {
        mMap.setBuiltInZoomControls(false);
        mMap.setMultiTouchControls(true);
        mMap.setTilesScaledToDpi(true);
        mController = mMap.getController();
        GeoPoint cityLocation = new GeoPoint(Double.parseDouble(mCity.getLatitude()),
                Double.parseDouble(mCity.getLongitude()));
        mController.setZoom(14.0);
        mController.setCenter(cityLocation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * show marker
     *
     * @param locationLat  latitude
     * @param locationLong longitude
     * @param locationName name of location
     */
    private void showMarker(Double locationLat, Double locationLong, String locationName) {
        GeoPoint coord = new GeoPoint(locationLat, locationLong);
        Marker marker = new Marker(mMap);
        if (ContextCompat.checkSelfPermission(PlacesOnMapActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            marker.setPosition(coord);
            marker.setIcon(mMarker);
            marker.setTitle(locationName);
            marker.setOnMarkerClickListener(this);
            mMap.getOverlays().add(marker);
            mMap.invalidate();
            mMarkerList.add(marker);
        }
    }

    /**
     * move to center marker
     *
     * @param marker    marker
     * @param latitude  latitude
     * @param longitude longitude
     */
    private void moveMakerToCenter(Marker marker, Double latitude, Double longitude) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        mMap.setBottom(height);
        mMap.setRight(width);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mController.setZoom(15.0);
        GeoPoint center = new GeoPoint(latitude, longitude);
        mController.animateTo(center);
    }

    /**
     * on marker selected
     *
     * @param marker marker
     */
    private void onPlaceSelected(Marker marker) {
        try {
            moveMakerToCenter(marker, marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
            linearLayout.setVisibility(View.VISIBLE);
            selectedItemName.setText(marker.getTitle());
            String[] address = mFeedItems.getJSONObject(mIndex).getString("address").split("<br/>");
            if (address.length > 1) {
                selectedItemAddress.setText(address[0] + ", " + address[1]);
            } else {
                selectedItemAddress.setText(address[0]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * get places
     */
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
                        for (int i = 0; i < mFeedItems.length(); i++) {
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
            RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            PlacesMapAdapter = new PlacesOnMapAdapter(PlacesOnMapActivity.this, feedItems, mIcon);
            recyclerView.setAdapter(PlacesMapAdapter);
            textViewNoItems.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Highlights the marker whose card is clicked
     *
     * @param title this is the title of the marker
     */
    private void highlightMarker(String title) {
        int index = 0;
        Marker currentMarker = null;
        for (Marker m : mMarkerList) {
            if (m.getTitle().equals(title)) {
                currentMarker = mMarkerList.get(index);
                break;
            }
            index++;
        }
        if (mPreviousMarker != null) {
            mPreviousMarker.setIcon(mMarker);
            //hide info about previous marker
            mPreviousMarker.closeInfoWindow();
        }
        mMap.getOverlays().remove(currentMarker);
        mMap.invalidate();
        currentMarker.setIcon(mDefaultMarker);
        mMap.getOverlays().add(currentMarker);
        mMap.invalidate();
        //show info about current marker
        currentMarker.showInfoWindow();
        mPreviousMarker = currentMarker;
        zoomToMarker(currentMarker.getPosition().getLatitude(), currentMarker.getPosition().getLongitude());
    }

    /**
     * Zooms in towards the marker whose card is clicked
     *
     * @param latitude  latitude from the selected marker
     * @param longitude longitude from the selected marker
     */
    private void zoomToMarker(double latitude, double longitude) {
        mController.setZoom(16.0);
        GeoPoint center = new GeoPoint(latitude, longitude);
        mController.animateTo(center);
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, PlacesOnMapActivity.class);
        return intent;
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        onPlaceSelected(marker);
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {

        filter(editable.toString());
        
    }

    /**
     * Adapter for horizontal recycler view for displaying each cityInfoItem
     */
    class PlacesOnMapAdapter extends RecyclerView.Adapter<PlacesOnMapAdapter.ViewHolder> {
        final Context mContext;
        JSONArray mFeedItems;
        final int mRd;

        PlacesOnMapAdapter(Context context, JSONArray feedItems, int r) {
            this.mContext = context;
            this.mFeedItems = feedItems;
            mRd = r;
        }

        public void filterList(JSONArray filterdNames) {
            this.mFeedItems = filterdNames;
            notifyDataSetChanged();
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
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" +
                            mFeedItems.getJSONObject(position).getString("title")
                    ));
                    mContext.startActivity(browserIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            holder.completeLayout.setOnClickListener(v -> {
                try {
                    highlightMarker(mFeedItems.getJSONObject(position).getString("title"));
                    String[] address = mFeedItems.getJSONObject(position).getString("address").split("<br/>");
                    if (address.length > 1) {
                        selectedItemAddress.setText(address[0] + ", " + address[1]);
                    } else {
                        selectedItemAddress.setText(address[0]);
                    }
                    linearLayout.setVisibility(View.VISIBLE);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
            return holder;
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

    /**
     * compares the filtered string with the exsiting array and creates a new one.
     *
     * @param searchtxt the text user enters into the edittext field
     */
    private void filter(String searchtxt) {
        final JSONArray filteredFeedItems = new JSONArray();
        for (int i = 0; i <= mFeedItems.length() - 1; i++) {
            try {
                if (mFeedItems.getJSONObject(i).getString("title").toLowerCase().contains(searchtxt.toLowerCase())) {
                    filteredFeedItems.put(mFeedItems.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        PlacesMapAdapter.filterList(filteredFeedItems);
    }
}