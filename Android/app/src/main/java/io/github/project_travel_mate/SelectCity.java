package io.github.project_travel_mate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK;
import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.DESTINATION_CITY_ID;
import static utils.Constants.DESTINATION_CITY_LAT;
import static utils.Constants.DESTINATION_CITY_LON;
import static utils.Constants.SOURCE_CITY;
import static utils.Constants.SOURCE_CITY_ID;
import static utils.Constants.SOURCE_CITY_LAT;
import static utils.Constants.SOURCE_CITY_LON;

@SuppressWarnings("WeakerAccess")
public class SelectCity extends AppCompatActivity {

    @BindView(R.id.source) Spinner source;
    @BindView(R.id.destination) Spinner dest;
    @BindView(R.id.pb) ProgressBar pb;
    private String[] cities;
    private SharedPreferences.Editor editor;
    private final List<String> id = new ArrayList<>();
    private final List<String> names = new ArrayList<>();
    private final List<String> lat = new ArrayList<>();
    private final List<String> lon = new ArrayList<>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        mHandler = new Handler(Looper.getMainLooper());

        getcitytask();

        Objects.requireNonNull(getSupportActionBar()).setTitle(" ");
    }

    @OnClick(R.id.ok)
    public void okClicked(View view) {

        int sposition = source.getSelectedItemPosition();
        int dposition = dest.getSelectedItemPosition();

        if (sposition == dposition) {
            Snackbar.make(view, "Source and destination cannot be same", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            editor.putString(DESTINATION_CITY_ID, id.get(dposition));
            editor.putString(SOURCE_CITY_ID, id.get(sposition));
            editor.putString(DESTINATION_CITY, names.get(dposition));
            editor.putString(SOURCE_CITY, names.get(sposition));
            editor.putString(DESTINATION_CITY_LAT, lat.get(dposition));
            editor.putString(SOURCE_CITY_LAT, lat.get(sposition));
            editor.putString(DESTINATION_CITY_LON, lon.get(dposition));
            editor.putString(SOURCE_CITY_LON, lon.get(sposition));
            SelectCity.this.startService(new Intent(SelectCity.this, LocationService.class));

            editor.apply();
            SelectCity.this.finish();
        }
    }

    private void getcitytask() {

        // to fetch city names
        String uri = API_LINK + "all-cities.php";
        Log.v("EXECUTING : ", uri);

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
            public void onResponse(Call call, final Response response) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(Objects.requireNonNull(response.body()).string());
                            JSONArray ar = ob.getJSONArray("cities");
                            for (int i = 0; i < ar.length(); i++) {
                                id.add(ar.getJSONObject(i).getString("id"));
                                names.add(ar.getJSONObject(i).getString("name"));
                                lat.add(ar.getJSONObject(i).getString("lat"));
                                lon.add(ar.getJSONObject(i).getString("lng"));
                            }
                            cities = new String[id.size()];
                            cities = names.toArray(cities);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    SelectCity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    cities);
                            source.setAdapter(adapter);
                            dest.setAdapter(adapter);
                            pb.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
