package io.github.project_travel_mate.travel.mytrips;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

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
import objects.Trip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class MyTrips extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView gridView;

    private MaterialDialog mDialog;
    private final List<Trip> mTrips = new ArrayList<>();
    private String mToken;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());

        mTrips.add(new Trip());

        mytrip();

        setTitle(getResources().getString(R.string.text_my_trips));
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void mytrip() {

        mDialog = new MaterialDialog.Builder(MyTrips.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        String uri = API_LINK_V2 + "get-all-trips";

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
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful() && response.body() != null) {
                    final String res = response.body().string();
                    JSONArray arr;
                    try {
                        arr = new JSONArray(res);

                        for (int i = 0; i < arr.length(); i++) {
                            String id = arr.getJSONObject(i).getString("id");
                            String start = arr.getJSONObject(i).getString("start_date_tx");
                            String end = arr.getJSONObject(i).optString("end_date", null);
                            String name = arr.getJSONObject(i).getJSONObject("city").getString("city_name");
                            String tname = arr.getJSONObject(i).getString("trip_name");
                            String image = arr.getJSONObject(i).getJSONObject("city").getString("image");
                            mTrips.add(new Trip(id, name, image, start, end, tname));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    }
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        gridView.setAdapter(new MyTripsAdapter(MyTrips.this, mTrips));
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
