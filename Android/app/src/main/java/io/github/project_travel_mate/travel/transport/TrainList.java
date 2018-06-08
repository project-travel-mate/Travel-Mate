package io.github.project_travel_mate.travel.transport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.SelectCity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.Constants;

import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.SOURCE_CITY;

public class TrainList extends AppCompatActivity implements
        com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

    @BindView(R.id.pb)
    ProgressBar     pb;
    @BindView(R.id.music_list)
    ListView        lv;
    @BindView(R.id.city)
    TextView        city;
    @BindView(R.id.seldate)
    TextView        selectdate;

    private String dates = "17-10";
    private String source;
    private String dest;

    private static final String DATEPICKER_TAG = "datepicker";
    private SharedPreferences sharedPreferences;
    private Handler mHandler;
    private com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler                    = new Handler(Looper.getMainLooper());
        sharedPreferences           = PreferenceManager.getDefaultSharedPreferences(this);
        source                      = sharedPreferences.getString(SOURCE_CITY, "delhi");
        dest                        = sharedPreferences.getString(DESTINATION_CITY, "mumbai");
        String cityText             = source + " to " + dest;
        city.setText(cityText);
        selectdate.setText(dates);

        getTrainlist();

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                isVibrate());

        pb.setVisibility(View.GONE);

        setTitle("Trains");

        city.setOnClickListener(this);
        selectdate.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog,
                          int year, int month, int day) {
        month++;
        dates = day + "-" + month;
        selectdate.setText(dates);
        getTrainlist();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) { }

    /**
     * Calls API to get train list
     */
    private void getTrainlist() {

        pb.setVisibility(View.VISIBLE);
        String uri = Constants.API_LINK +
                "get-trains.php?src_city=" + source +
                "&dest_city=" + dest +
                "&date=" + dates;

        Log.e("CALLING : ", uri);

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
                        Log.e("RESPONSE : ", "Done");
                        try {
                            JSONObject feed = new JSONObject(String.valueOf(res));
                            JSONArray feedItems = feed.getJSONArray("trains");

                            Log.e("response", feedItems + " ");
                            pb.setVisibility(View.GONE);
                            lv.setAdapter(new TrainAdapter(TrainList.this, feedItems));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        source                  = sharedPreferences.getString(SOURCE_CITY, "delhi");
        dest                    = sharedPreferences.getString(DESTINATION_CITY, "mumbai");
        String cityText         = source + " to " + dest;
        city.setText(cityText);

        getTrainlist();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city :
                Intent i = new Intent(TrainList.this, SelectCity.class);
                startActivity(i);
                break;
            case R.id.seldate :
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
        }
    }
}
