package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;
import objects.CityHistoryListItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class CityHistoryActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.list)
    ListView listView;

    private static  City mCity;
    private String mToken;
    private List<CityHistoryListItem> mCityHistory = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_history);
        ButterKnife.bind(this);

        SharedPreferences mSharedPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPrefrences.getString(USER_TOKEN, "null");

        fetchHistory();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.title_activity_city_history);
    }

    /**
     * Fetches history of given city from server
     */
    public void fetchHistory() {

        animationView.playAnimation();
        Handler handler = new Handler(Looper.getMainLooper());
        String uri;
        uri = API_LINK_V2 + "get-city-information/" + mCity.getId();
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
                handler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                handler.post(() -> {
                    try {
                        JSONObject object = new JSONObject(res);
                        JSONArray keys = object.names();
                        for (int i = 0; i < keys.length(); i++) {
                            String heading = keys.getString(i);
                            String text = object.getString(heading);
                            if (!text.equals("")) {
                                CityHistoryListItem city = new CityHistoryListItem(heading, text);
                                mCityHistory.add(city);
                            }
                        }
                        listView.setAdapter(new CityHistoryAdapter(CityHistoryActivity.this,
                                mCityHistory));
                        animationView.setVisibility(GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR : ", "Message : " + e.getMessage());
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
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    public static Intent getStartIntent(Context context, City city) {
        Intent intent = new Intent(context, CityHistoryActivity.class);
        mCity = city;
        return intent;
    }
}
