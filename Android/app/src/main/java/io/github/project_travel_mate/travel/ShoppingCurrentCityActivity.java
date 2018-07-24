package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.Utils;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class ShoppingCurrentCityActivity extends AppCompatActivity {

    @BindView(R.id.shopping_list)
    ListView lv;
    @BindView(R.id.query)
    EditText q;
    @BindView(R.id.go)
    Button ok;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.show_city_name)
    TextView showCityName;

    private MaterialSearchView mSearchView;
    private String mToken;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_currentcity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setTitle(getResources().getString(R.string.text_shopping));

        mSearchView = findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Log.v("QUERY ITEM : ", query);
                try {
                    getShoppingItems(query);
                } catch (Exception e) {
                    networkError();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });


        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @OnClick(R.id.go)
    void onClick() {
        try {
            String item = q.getText().toString();
            getShoppingItems(item);
        } catch (Exception e) {
            networkError();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void getShoppingItems(final String item) {
        showCityName.setText(String.format(getString(R.string.showing_shopping_item), item));
        showCityName.setVisibility(View.VISIBLE);
        String uri = API_LINK_V2 + "get-shopping-info/" + item;
        uri = uri.replace(" ", "+");

        //Set up client
        OkHttpClient client = new OkHttpClient();

        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        Log.v("EXECUTING : ", uri);

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
                            final String res = Objects.requireNonNull(response.body()).string();
                            JSONArray feedItems = new JSONArray(res);
                            Log.v("response", feedItems + " ");

                            if (feedItems.length() == 0) {
                                Utils.hideKeyboard(ShoppingCurrentCityActivity.this);
                            }
                            animationView.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            lv.setAdapter(new ShoppingAdapter(ShoppingCurrentCityActivity.this, feedItems));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            networkError();
                        }
                    } else {
                        noResults();
                    }
                });
            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ShoppingCurrentCityActivity.class);
        return intent;
    }
    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        showCityName.setVisibility(View.GONE);
        animationView.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }
    /**
     * Plays the no results animation in the view
     */
    private void noResults() {
        animationView.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        Toast.makeText(ShoppingCurrentCityActivity.this,  R.string.no_results, Toast.LENGTH_LONG).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

}
