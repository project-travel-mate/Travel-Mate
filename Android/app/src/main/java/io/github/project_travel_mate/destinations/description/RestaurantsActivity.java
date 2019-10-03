package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import adapters.RestaurantsCardViewAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;
import objects.RestaurantDetails;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.RestaurantItemEntity;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.AUTHORIZATION;
import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.USER_TOKEN;


public class RestaurantsActivity extends AppCompatActivity implements RestaurantsCardViewAdapter.OnItemClickListener {

    private static final String TAG = "RestaurantsActivity";

    @BindView(R.id.restaurants_recycler_view)
    RecyclerView mRestaurantsOptionsRecycleView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private City mCity;
    private Handler mHandler;
    private String mToken;
    private SharedPreferences mSharedPreferences;
    public List<RestaurantItemEntity> restaurantItemEntities = new ArrayList<>();
    private RestaurantsCardViewAdapter mRestaurantsCardViewAdapter;

    private final Gson mGson = new Gson();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, RestaurantsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        mHandler = new Handler(Looper.getMainLooper());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mCity.getNickname());

        mRestaurantsCardViewAdapter = new RestaurantsCardViewAdapter(this,
                restaurantItemEntities);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(RestaurantsActivity.this);
        mRestaurantsOptionsRecycleView.setLayoutManager(mLayoutManager);
        mRestaurantsOptionsRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRestaurantsOptionsRecycleView.setAdapter(mRestaurantsCardViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getRestaurantItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!restaurantItemEntities.isEmpty())
            getMenuInflater().inflate(R.menu.restaurants_sort_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                sortRestaurants(item.getItemId());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortRestaurants(int sortType) {
        Comparator<RestaurantItemEntity> comparator = null;
        switch (sortType) {
            case R.id.restaurantSortPricesLow:
                comparator = (r1, r2) -> r1.getAvgCost() - r2.getAvgCost();
                break;
            case R.id.restaurantSortPricesHigh:
                comparator = (r1, r2) -> r2.getAvgCost() - r1.getAvgCost();
                break;
            case R.id.restaurantSortRating:
                comparator = (r1, r2) -> Float.compare(r2.getRatings(), r1.getRatings());
                break;
            case R.id.restaurantSortVotes:
                comparator = (r1, r2) -> r2.getVotes() - r1.getVotes();
                break;
            case R.id.restaurantSortAlphabet:
                comparator = (r1, r2) -> r1.getName().compareTo(r2.getName());
                break;
        }

        if (comparator != null) {
            Collections.sort(restaurantItemEntities, comparator);
            if (mRestaurantsCardViewAdapter != null) {
                mRestaurantsCardViewAdapter.notifyDataSetChanged();
            }
        }

    }


    @Override
    public void onItemClick(RestaurantItemEntity item) {

        //get-restaurant/<int:restaurant_id>

        String requestUrl = API_LINK_V2 + "get-restaurant/" + item.getId();
        Log.w(TAG, "URL =" + requestUrl);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header(AUTHORIZATION, "Token " + mToken)
                .url(requestUrl)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> networkError());
                Log.v(TAG, "Request Failed, message = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String res = Objects.requireNonNull(response.body()).string();

                Log.v(TAG, "Response = " + res );

                RestaurantDetails details = mGson.fromJson(res, RestaurantDetails.class);

                Log.d(TAG, "details " + details.getName());

                mHandler.post(() -> RestaurantDetailsActivity.newInstance(RestaurantsActivity.this, details));
            }
        });
    }

    private void getRestaurantItems() {

        String uri = API_LINK_V2 + "get-all-restaurants/" + mCity.getLatitude() + "/" + mCity.getLongitude();
        Log.v(TAG, "URI : " + uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header(AUTHORIZATION, "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> networkError());
                Log.v(TAG, "Request Failed, message = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {

                    if (res.equals("\"Not Found\"") || res.contains("Not Found")) {
                        notFoundError();
                        return;
                    }

                    try {
                        JSONArray array = new JSONArray(res);
                        Log.v(TAG, "Response = " + res );
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String id = object.getString("restaurant_id");
                            String imageUrl = object.getString("restaurant_image");
                            String name = object.getString("restaurant_name");
                            String address = object.getString("address");
                            float ratings = (float) object.getDouble("aggregate_rating");
                            int votes = object.getInt("votes");
                            String restaurantURL = object.getString("restaurant_url");
                            int avgCost = object.getInt("avg_cost_2");

                            restaurantItemEntities.add(
                                    new RestaurantItemEntity(id, imageUrl, name, address, ratings,
                                            votes, avgCost, restaurantURL));
                        }

                        animationView.setVisibility(View.GONE);
                        mRestaurantsCardViewAdapter.notifyDataSetChanged();

                        invalidateOptionsMenu();
                    } catch (JSONException e) {
                        networkError();
                        e.printStackTrace();
                        Log.e("ERROR : ", "Message : " + e.getMessage());
                    }
                });

            }
        });
    }

    /**
     * Plays the Not Found animation in the view
     */
    private void notFoundError() {
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
        animationView.setOnClickListener(v -> getRestaurantItems());
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
        animationView.setOnClickListener(v -> getRestaurantItems());
    }

}
