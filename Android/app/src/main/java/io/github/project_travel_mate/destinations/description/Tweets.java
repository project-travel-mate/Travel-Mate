package io.github.project_travel_mate.destinations.description;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Tweet;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK;
import static utils.Constants.EXTRA_MESSAGE_ID;
import static utils.Constants.EXTRA_MESSAGE_IMAGE;
import static utils.Constants.EXTRA_MESSAGE_NAME;

public class Tweets extends AppCompatActivity {

    @BindView(R.id.list)
    ListView lv;

    private String id;
    private MaterialDialog dialog;
    private final List<Tweet> tweets = new ArrayList<>();
    private TweetsAdapter adapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        ButterKnife.bind(this);

        mHandler = new Handler(Looper.getMainLooper());

        Intent intent   = getIntent();
        String title    = intent.getStringExtra(EXTRA_MESSAGE_NAME);
        id              = intent.getStringExtra(EXTRA_MESSAGE_ID);
        String image    = intent.getStringExtra(EXTRA_MESSAGE_IMAGE);

        setTitle(title);
        getTweets();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void getTweets() {

        dialog = new MaterialDialog.Builder(Tweets.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

        // to fetch city names
        String uri = API_LINK + "city/trends/twitter.php?city=" + id;
        Log.v("executing", uri);


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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray ob = new JSONArray(res);
                            for (int i = 0; i < ob.length(); i++) {
                                String nam = ob.getJSONObject(i).getString("name");
                                String link = ob.getJSONObject(i).getString("url");
                                String count = ob.getJSONObject(i).getString("tweet_volume");
                                tweets.add(new Tweet(nam, link, count));
                            }
                            adapter = new TweetsAdapter(Tweets.this, tweets);
                            lv.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage());
                        }
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
}
