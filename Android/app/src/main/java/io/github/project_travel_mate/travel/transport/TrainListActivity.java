package io.github.project_travel_mate.travel.transport;

import android.content.Context;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK;
import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.SOURCE_CITY;

public class TrainListActivity extends AppCompatActivity implements
        com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

    private static final String DATEPICKER_TAG = "datepicker";
    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.music_list)
    ListView listView;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.seldate)
    TextView selectdate;
    private String mDate = "17-10";
    private String mSource;
    private String mDestination;
    private SharedPreferences mSharedPreferences;
    private Handler mHandler;
    private com.fourmob.datetimepicker.date.DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler = new Handler(Looper.getMainLooper());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSource = mSharedPreferences.getString(SOURCE_CITY, "delhi");
        mDestination = mSharedPreferences.getString(DESTINATION_CITY, "mumbai");
        String cityText = mSource + " to " + mDestination;
        city.setText(cityText);
        selectdate.setText(mDate);

        getTrainlist();

        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                isVibrate());

        progressBar.setVisibility(View.GONE);

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
        mDate = day + "-" + month;
        selectdate.setText(mDate);
        getTrainlist();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
    }

    /**
     * Calls API to get train list
     */
    private void getTrainlist() {

        progressBar.setVisibility(View.VISIBLE);
        String uri = API_LINK +
                "get-trains.php?src_city=" + mSource +
                "&dest_city=" + mDestination +
                "&date=" + mDate;

        Log.v("EXECUTING", uri);

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
                mHandler.post(() -> {
                    try {
                        JSONObject feed = new JSONObject(String.valueOf(res));
                        JSONArray feedItems = feed.getJSONArray("trains");

                        Log.v("RESPONSE", "Message : " + feedItems);
                        progressBar.setVisibility(View.GONE);
                        listView.setAdapter(new TrainAdapter(TrainListActivity.this, feedItems));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSource = mSharedPreferences.getString(SOURCE_CITY, "delhi");
        mDestination = mSharedPreferences.getString(DESTINATION_CITY, "mumbai");
        String cityText = mSource + " to " + mDestination;
        city.setText(cityText);

        getTrainlist();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city:
                //TODO :: show a dialog with list of cities
                break;
            case R.id.seldate:
                mDatePickerDialog.setVibrate(isVibrate());
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                mDatePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, TrainListActivity.class);
        return intent;
    }
}
