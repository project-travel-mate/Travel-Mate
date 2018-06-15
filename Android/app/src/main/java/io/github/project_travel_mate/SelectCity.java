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
    private String[] mCities;
    private SharedPreferences.Editor mEditor;
    private final List<String> mId = new ArrayList<>();
    private final List<String> mNames = new ArrayList<>();
    private final List<String> mLatitude = new ArrayList<>();
    private final List<String> mLongitude = new ArrayList<>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sharedPreferences.edit();
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
            mEditor.putString(DESTINATION_CITY_ID, mId.get(dposition));
            mEditor.putString(SOURCE_CITY_ID, mId.get(sposition));
            mEditor.putString(DESTINATION_CITY, mNames.get(dposition));
            mEditor.putString(SOURCE_CITY, mNames.get(sposition));
            mEditor.putString(DESTINATION_CITY_LAT, mLatitude.get(dposition));
            mEditor.putString(SOURCE_CITY_LAT, mLatitude.get(sposition));
            mEditor.putString(DESTINATION_CITY_LON, mLongitude.get(dposition));
            mEditor.putString(SOURCE_CITY_LON, mLongitude.get(sposition));
            SelectCity.this.startService(new Intent(SelectCity.this, LocationService.class));

            mEditor.apply();
            SelectCity.this.finish();
        }
    }

    private void getcitytask() {

        // to fetch city mNames
        String uri = API_LINK + "all-mCities.php";
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
                            JSONArray ar = ob.getJSONArray("mCities");
                            for (int i = 0; i < ar.length(); i++) {
                                mId.add(ar.getJSONObject(i).getString("mId"));
                                mNames.add(ar.getJSONObject(i).getString("name"));
                                mLatitude.add(ar.getJSONObject(i).getString("mLatitude"));
                                mLongitude.add(ar.getJSONObject(i).getString("lng"));
                            }
                            mCities = new String[mId.size()];
                            mCities = mNames.toArray(mCities);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    SelectCity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    mCities);
                            source.setAdapter(adapter);
                            dest.setAdapter(adapter);
                            pb.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", "Message : " + e.getMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
