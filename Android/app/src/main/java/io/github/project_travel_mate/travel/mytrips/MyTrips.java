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
import utils.Constants;

public class MyTrips extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView g;

    private MaterialDialog dialog;
    private List<Trip> trips = new ArrayList<>();
    private String userid;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        ButterKnife.bind(this);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);
        userid  = s.getString(Constants.USER_ID, "1");
        mHandler = new Handler(Looper.getMainLooper());

        trips.add(new Trip());

        mytrip();

        setTitle("My Trips");
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void mytrip() {

        dialog = new MaterialDialog.Builder(MyTrips.this)
                .title(R.string.app_name)
                .content("Fetching trips...")
                .progress(true, 0)
                .show();

        String uri = Constants.apilink + "trip/get-all.php?user=" + userid;
        Log.e("executing", uri + " ");


        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
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
                JSONArray arr;
                try {
                    arr = new JSONArray(res);

                    for (int i = 0; i < arr.length(); i++) {
                        String id = arr.getJSONObject(i).getString("id");
                        String start = arr.getJSONObject(i).getString("start_time");
                        String end = arr.getJSONObject(i).getString("end_time");
                        String name = arr.getJSONObject(i).getString("city");
                        String tname = arr.getJSONObject(i).getString("title");
                        String image = arr.getJSONObject(i).getString("image");
                        trips.add(new Trip(id, name, image, start, end, tname));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("erro", e.getMessage() + " ");
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        g.setAdapter(new MyTripsAdapter(MyTrips.this, trips));
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
