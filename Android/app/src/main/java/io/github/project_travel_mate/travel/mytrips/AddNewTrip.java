package io.github.project_travel_mate.travel.mytrips;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.github.project_travel_mate.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.STATUS_CODE_CREATED;
import static utils.Constants.USER_TOKEN;

/**
 * Activity to add new trip
 */
public class AddNewTrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

    private static final String DATEPICKER_TAG1 = "datepicker1";
    private static final String DATEPICKER_TAG2 = "datepicker2";

    private String mNameyet;
    private String mCityid = "2";
    private String mStartdate;
    private String mTripname;
    private String mToken;

    private MaterialDialog mDialog;
    private Handler mHandler;
    private DatePickerDialog mDatePickerDialog;

    @BindView(R.id.cityname)
    AutoCompleteTextView    cityname;
    @BindView(R.id.sdate)
    FlatButton              sdate;
    @BindView(R.id.edate)
    FlatButton              edate;
    @BindView(R.id.ok)
    FlatButton              ok;
    @BindView(R.id.tname)
    EditText                tname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mHandler = new Handler(Looper.getMainLooper());
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        cityname.setThreshold(1);

        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        isVibrate());

        sdate.setOnClickListener(this);
        edate.setOnClickListener(this);
        ok.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnTextChanged(R.id.cityname) void onTextChanged() {
        mNameyet = cityname.getText().toString();
        if (!mNameyet.contains(" ") && mNameyet.length() % 3 == 0) {
            cityNameAutoComplete();     // Calls API to autocomplete cityname
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (Objects.equals(datePickerDialog.getTag(), DATEPICKER_TAG1)) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            mStartdate = Long.toString(calendar.getTimeInMillis() / 1000);
        }
        if (Objects.equals(datePickerDialog.getTag(), DATEPICKER_TAG2)) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            String enddate = Long.toString(calendar.getTimeInMillis() / 1000);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) { }

    /**
     * Calls city name autocomplete API
     */
    private void cityNameAutoComplete() {

        // to fetch city names
        String uri = API_LINK_V2 + "get-city-by-name/" + mNameyet.trim();
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray arr;
                        final ArrayList<String> names, ids;
                        try {
                            arr = new JSONArray(res);
                            Log.v("RESPONSE : ", res);

                            names = new ArrayList<>();
                            ids = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                try {
                                    names.add(arr.getJSONObject(i).getString("city_name"));
                                    ids.add(arr.getJSONObject(i).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("ERROR", "Message : " + e.getMessage());
                                }
                            }
                            ArrayAdapter<String> dataAdapter =
                                    new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, names);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cityname.setThreshold(1);
                            cityname.setAdapter(dataAdapter);
                            cityname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    mCityid = ids.get(arg2);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("EXCEPTION : ", "Message : " + e.getMessage());
                        }
                    }
                });

            }
        });
    }

    /**
     * Calls API to add  new trip
     */
    private void addTrip() {

        // Show a mDialog box
        mDialog = new MaterialDialog.Builder(AddNewTrip.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        String uri = API_LINK_V2 + "add-trip";

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("trip_name", mTripname)
                .addFormDataPart("city_id", mCityid)
                .addFormDataPart("start_date_tx", mStartdate)
                .build();

        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .post(requestBody)
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
                try {
                    final String res = Objects.requireNonNull(response.body()).string();
                    final int responseCode = response.code();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (responseCode == STATUS_CODE_CREATED) {
                                Toast.makeText(AddNewTrip.this, R.string.trip_added, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddNewTrip.this, res, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDialog.dismiss();

            }
        });
    }

    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Set Start date
            case R.id.sdate :
                mDatePickerDialog.setVibrate(isVibrate());
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                mDatePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG1);
                break;
            // Set end date
            case R.id.edate :
                mDatePickerDialog.setVibrate(isVibrate());
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                mDatePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG2);
                break;
            // Add a new trip
            case R.id.ok :
                mTripname = tname.getText().toString();
                addTrip();
                break;
        }
    }
}
