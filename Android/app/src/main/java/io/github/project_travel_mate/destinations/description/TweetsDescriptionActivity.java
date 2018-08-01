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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import objects.TweetDescription;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_HASHTAG_NAME;
import static utils.Constants.USER_TOKEN;

public class TweetsDescriptionActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.tweets_recycler_view)
    RecyclerView recyclerView;

    private List<TweetDescription> mTweets = new ArrayList<>();
    private String mToken;
    private String mHashtagName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_description);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String hashtag = intent.getStringExtra(EXTRA_MESSAGE_HASHTAG_NAME);

        //server doesnt respond to query when it contains #
        //in the begining, so remove it
        if (hashtag.contains("#"))
            mHashtagName = hashtag.replaceFirst("#", "");
        else
            mHashtagName = hashtag;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        fetchTweets();

        setTitle(String.format(getString(R.string.hashtag_title), mHashtagName));
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Gets a list of all the tweets of given hashtag
     */
    private void fetchTweets() {
        // to fetch tweets
        Handler handler = new Handler(Looper.getMainLooper());
        String uri = API_LINK_V2 + "get-search-tweets/" + mHashtagName;
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
                handler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONArray array = new JSONArray(res);
                            for (int i = 0; i < array.length(); i++) {
                                String username = array.getJSONObject(i).getString("username");
                                String favCount = array.getJSONObject(i).getString("favorite_count");
                                String text = array.getJSONObject(i).getString("text");
                                String retweetsCount  = array.getJSONObject(i).getString("retweet_count");
                                String createdAt = array.getJSONObject(i).getString("created_at");
                                String avatar = array.getJSONObject(i).getString("user_profile_image");
                                String userScreenName = array.getJSONObject(i).getString("user_screen_name");
                                mTweets.add(new TweetDescription(username, userScreenName,
                                        createdAt, avatar, favCount, retweetsCount, text));
                            }
                            RecyclerView.LayoutManager layoutManager = new
                                    LinearLayoutManager(TweetsDescriptionActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            TweetsDescriptionAdapter adapter = new
                                    TweetsDescriptionAdapter(TweetsDescriptionActivity.this, mTweets);
                            recyclerView.setAdapter(adapter);
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
    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    public static Intent getStartIntent(Context context, String hashtag) {
        Intent intent = new Intent(context, TweetsDescriptionActivity.class);
        intent.putExtra(EXTRA_MESSAGE_HASHTAG_NAME, hashtag);
        return intent;
    }
}
