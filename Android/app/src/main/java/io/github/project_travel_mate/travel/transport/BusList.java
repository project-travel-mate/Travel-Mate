package io.github.project_travel_mate.travel.transport;

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

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
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

import static utils.Constants.API_LINK;
import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.SOURCE_CITY;

/**
 * Displays a list of available buses
 */
public class BusList extends AppCompatActivity implements OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

    private static final String DATEPICKER_TAG = "datepicker";

    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.music_list)
    ListView    lv;
    @BindView(R.id.seldate)
    TextView    selectdate;
    @BindView(R.id.city)
    TextView    city;

    private String mSource;
    private String mDestination;
    private String mDate = "17-October-2015";

    private Handler mHandler;
    private SharedPreferences mSharedPreferences;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler(Looper.getMainLooper());

        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSource = mSharedPreferences.getString(SOURCE_CITY, "delhi");
        mDestination = mSharedPreferences.getString(DESTINATION_CITY, "mumbai");

        selectdate.setText(mDate);
        String cityText = mSource + " to " + mDestination;
        city.setText(cityText);

        getBuslist();

        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                isVibrate());

        setTitle("Buses");
        city.setOnClickListener(this);
        selectdate.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        // Set date in format 17-October-2016
        mDate = day + "-";

        String monthString;
        switch (month + 1) {
            case 1: monthString = "January"; break;
            case 2: monthString = "February"; break;
            case 3: monthString = "March"; break;
            case 4: monthString = "April"; break;
            case 5: monthString = "May"; break;
            case 6: monthString = "June"; break;
            case 7: monthString = "July"; break;
            case 8: monthString = "August"; break;
            case 9: monthString = "September"; break;
            case 10: monthString = "October"; break;
            case 11: monthString = "November"; break;
            case 12: monthString = "December"; break;
            default: monthString = "Invalid month"; break;
        }

        mDate = mDate + monthString;
        mDate = mDate + "-" + year;

        selectdate.setText(mDate);
        getBuslist(); //Update bus list
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
    }

    /**
     * Calls API to get bus list
     */
    private void getBuslist() {

        pb.setVisibility(View.VISIBLE);
        String uri = API_LINK + "bus-booking.php?src=" + mSource +
                "&mDestination=" + mDestination +
                "&date=" + mDate;

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
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
                            JSONObject feed = new JSONObject(String.valueOf(res));
                            JSONArray feedItems = feed.getJSONArray("results");
                            Log.v("response", "Response : " + feedItems);
                            pb.setVisibility(View.GONE);
                            lv.setAdapter(new BusAdapter(BusList.this, feedItems));
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
        mSource = mSharedPreferences.getString(SOURCE_CITY, "delhi");
        mDestination = mSharedPreferences.getString(DESTINATION_CITY, "mumbai");
        String cityText = mSource + " to " + mDestination;
        city.setText(cityText);
        getBuslist(); // Update Bus list
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city :
                Intent i = new Intent(BusList.this, SelectCity.class);
                startActivity(i);
                break;
            case R.id.seldate :
                mDatePickerDialog.setVibrate(isVibrate());
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                mDatePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
        }
    }
}
