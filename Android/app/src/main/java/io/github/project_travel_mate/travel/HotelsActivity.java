package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.searchcitydialog.CitySearchDialogCompat;
import io.github.project_travel_mate.searchcitydialog.CitySearchModel;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

/**
 * Display list of hotels in destination city
 */
public class HotelsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.hotel_list)
    GridView gridView;
    @BindView(R.id.select_city)
    TextView selectCity;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.layout)
    LinearLayout layout;

    private Handler mHandler;
    private String mToken;

    private ArrayList<CitySearchModel> mSearchCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        fetchCitiesList();

        setTitle("Hotels");

        selectCity.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Calls API to get hotel list
     */
    private void getHotelList(String latitude, String longitude) {

        String uri = API_LINK_V2 + "get-places/" + latitude + "/" + longitude + "/accommodation";

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
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                Log.v("RESPONSE", res + " ");
                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray feedItems = new JSONArray(res);
                            Log.v("response", feedItems + " ");
                            layout.setVisibility(View.VISIBLE);
                            animationView.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            if (feedItems.length() > 0) {
                                gridView.setAdapter(new HotelsAdapter(HotelsActivity.this, feedItems));
                            } else {
                                noResults();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }

    /**
     * Fetches the list cities from server
     */
    private void fetchCitiesList() {

        String uri = API_LINK_V2 + "get-all-cities/10";
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
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
            public void onResponse(Call call, final Response response) {
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();
                            Log.v("RESULT", res);
                            JSONArray ar = new JSONArray(res);
                            for (int i = 0; i < ar.length(); i++) {
                                mSearchCities.add(new CitySearchModel(
                                        ar.getJSONObject(i).getString("city_name"),
                                        ar.getJSONObject(i).optString("image"),
                                        ar.getJSONObject(i).getString("id")));
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            networkError();
                            Log.e("ERROR", "Message : " + e.getMessage());
                        }
                    } else {
                        Log.e("ERROR", "Network error");
                        networkError();
                    }
                });
            }
        });

    }

    /**
     * Calls the API & fetch details of city with given id
     *
     * @param cityId the city id
     */
    public void getCityInfo(String cityId) {

        animationView.setVisibility(View.VISIBLE);

        String uri = API_LINK_V2 + "get-city/" + cityId;
        Log.v("EXECUTING", uri);
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> networkError());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {

                    final String res = Objects.requireNonNull(response.body()).string();
                    try {
                        Log.v("Response", res);
                        JSONObject responseObject = new JSONObject(res);
                        String latitude = responseObject.getString("latitude");
                        String longitude = responseObject.getString("longitude");
                        getHotelList(latitude, longitude);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkError();
                    }
                } else {
                    networkError();
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_city:
                new CitySearchDialogCompat(HotelsActivity.this, getString(R.string.search_title),
                        getString(R.string.search_hint), null, mSearchCities,
                        (SearchResultListener<CitySearchModel>) (dialog, item, position) -> {
                            String selectedCity = item.getId();
                            selectCity.setText(String.format(getString(R.string.showing_hotels), item.getName()));
                            dialog.dismiss();
                            getCityInfo(selectedCity);
                        }).show();
                gridView.setAdapter(null);
                break;
        }
    }

    // TODO :: Move adapter to a new class
    class HotelsAdapter extends BaseAdapter {

        final Context mContext;
        final JSONArray mFeedItems;
        private final LayoutInflater mInflater;

        HotelsAdapter(Context context, JSONArray feedItems) {
            this.mContext = context;
            this.mFeedItems = feedItems;

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
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.hotel_listitem, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            try {
                holder.title.setText(mFeedItems.getJSONObject(position).getString("title"));
                holder.description.setText(android.text.Html.fromHtml(mFeedItems
                        .getJSONObject(position).getString("address")).toString());
                holder.distance.setText(new DecimalFormat("##.##").format((float) mFeedItems
                        .getJSONObject(position).getInt("distance") / 1000));
                holder.call.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    try {
                        intent.setData(Uri.parse("tel:" +
                                mFeedItems.getJSONObject(position).optString("phone", "000")));
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkError();
                    }
                });
                holder.map.setOnClickListener(view -> {
                    Intent browserIntent;
                    try {
                        Double latitude =
                                mFeedItems.getJSONObject(position).getDouble("latitude");
                        Double longitude =
                                mFeedItems.getJSONObject(position).getDouble("longitude");

                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                                mFeedItems.getJSONObject(position).getString("title") +
                                "+(name)+@" + latitude +
                                "," + longitude));

                        mContext.startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkError();
                    }

                });
                holder.book.setOnClickListener(view -> {
                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(mFeedItems.getJSONObject(position).getString("href")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        networkError();
                    }
                    mContext.startActivity(browserIntent);

                });
                holder.expandText.setOnClickListener((View view) -> {
                    if (holder.detailsLayout.getVisibility() == View.GONE) {
                        holder.detailsLayout.setVisibility(View.VISIBLE);
                        holder.expandCollapse.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    } else {
                        holder.detailsLayout.setVisibility(View.GONE);
                        holder.expandCollapse.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }
                });
                holder.expandCollapse.setOnClickListener((View view) -> {
                    if (holder.detailsLayout.getVisibility() == View.GONE) {
                        holder.detailsLayout.setVisibility(View.VISIBLE);
                        holder.expandCollapse.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    } else {
                        holder.detailsLayout.setVisibility(View.GONE);
                        holder.expandCollapse.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR : ", "Message : " + e.getMessage());
            }
            return convertView;
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

    /**
     * Plays the no results animation in the view
     */
    private void noResults() {
        layout.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        Toast.makeText(HotelsActivity.this, R.string.no_trips, Toast.LENGTH_LONG).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    class ViewHolder {
        @BindView(R.id.hotel_name)
        TextView title;
        @BindView(R.id.hotel_address)
        TextView description;
        @BindView(R.id.call)
        RelativeLayout call;
        @BindView(R.id.map)
        RelativeLayout map;
        @BindView(R.id.book)
        RelativeLayout book;
        @BindView(R.id.more_details)
        LinearLayout detailsLayout;
        @BindView(R.id.expand_text_view)
        TextView expandText;
        @BindView(R.id.expand_collapse)
        ImageView expandCollapse;
        @BindView(R.id.distance)
        TextView distance;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, HotelsActivity.class);
        return intent;
    }
}
