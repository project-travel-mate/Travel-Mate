package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

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
import objects.Tweet;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.USER_TOKEN;

public class TweetsActivity extends AppCompatActivity {

    @BindView(R.id.list)
    ListView lv;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private final List<Tweet> mTweets = new ArrayList<>();
    private TweetsAdapter mAdapter;
    private Handler mHandler;
    private String mToken;

    private City mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        ButterKnife.bind(this);

        mHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        setTitle(mCity.getNickname());
        fetchTweets();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /***
     * Fetch tweets of a given city from server
     */
    private void fetchTweets() {

        // to fetch city names
        String uri = API_LINK_V2 + "get-city-trends/" + mCity.getId();
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
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {
                                String nam = array.getJSONObject(i).getString("name");
                                String link = array.getJSONObject(i).getString("url");
                                String count = array.getJSONObject(i).getString("tweet_volume");
                                mTweets.add(new Tweet(nam, link, count));
                            }
                            mAdapter = new TweetsAdapter(TweetsActivity.this, mTweets);
                            lv.setAdapter(mAdapter);
                            animationView.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", "Message : " + e.getMessage());
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    public static Intent getStartIntent(Context context, City city) {
        Intent intent = new Intent(context, TweetsActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, city);
        return intent;
    }
}
