package tie.hackathon.travelguide;

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

import utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Activity to add new trip
 */
public class AddNewTrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,View.OnClickListener {

    private static final String DATEPICKER_TAG1 = "datepicker1";
    private static final String DATEPICKER_TAG2 = "datepicker2";

    private String nameyet;
    private String cityid = "2";
    private String startdate;
    private String enddate;
    private String tripname;
    private String userid;

    private MaterialDialog      dialog;
    private Handler             mHandler;
    private SharedPreferences   sharedPreferences;
    private DatePickerDialog    datePickerDialog;

    @BindView(R.id.cityname)    AutoCompleteTextView    cityname;
    @BindView(R.id.sdate)       FlatButton              sdate;
    @BindView(R.id.edate)       FlatButton              edate;
    @BindView(R.id.ok)          FlatButton              ok;
    @BindView(R.id.tname)       EditText                tname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);

        ButterKnife.bind(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = sharedPreferences.getString(Constants.USER_ID, "1");
        mHandler = new Handler(Looper.getMainLooper());

        cityname.setThreshold(1);

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        isVibrate());

        sdate.setOnClickListener(this);
        edate.setOnClickListener(this);
        ok.setOnClickListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnTextChanged(R.id.cityname) void onTextChanged(){
        nameyet = cityname.getText().toString();
        if (!nameyet.contains(" ")) {
            Log.e("name", nameyet + " ");
            tripAutoComplete();     // Calls API to autocomplete cityname
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (Objects.equals(datePickerDialog.getTag(), DATEPICKER_TAG1)) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            startdate = Long.toString(calendar.getTimeInMillis() / 1000);
        }
        if (Objects.equals(datePickerDialog.getTag(), DATEPICKER_TAG2)) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            enddate = Long.toString(calendar.getTimeInMillis() / 1000);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {}


    /**
     * Calls city name autocomplete API
     */
    private void tripAutoComplete() {

        // to fetch city names
        String uri = Constants.apilink + "city/autocomplete.php?search=" + nameyet.trim();
        Log.e("executing", uri + " ");

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

                final String res = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray arr;
                        final ArrayList names, ids;
                        try {
                            arr = new JSONArray(res);
                            Log.e("RESPONSE : ", arr + " ");

                            names = new ArrayList<>();
                            ids = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                try {
                                    names.add(arr.getJSONObject(i).getString("name"));
                                    ids.add(arr.getJSONObject(i).getString("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("error ", " " + e.getMessage());
                                }
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                                    (getApplicationContext(), R.layout.spinner_layout, names);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cityname.setThreshold(1);
                            cityname.setAdapter(dataAdapter);
                            cityname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    cityid = ids.get(arg2).toString();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("EXCEPTION : ", e.getMessage() + " ");
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

        // Show a dialog box
        dialog = new MaterialDialog.Builder(AddNewTrip.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

        String uri = Constants.apilink + "trip/add-trip.php?user=" + userid +
                "&title=" + tripname +
                "&start_time=" + startdate +
                "&city=" + cityid;

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("RESPONSE : ", "Done");
                        Toast.makeText(AddNewTrip.this, "Trip added", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
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
        switch (view.getId()){
            // Set Start date
            case R.id.sdate :
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG1);
                break;
            // Set end date
            case R.id.edate :
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG2);
                break;
            // Add a new trip
            case R.id.ok :
                tripname = tname.getText().toString();
                addTrip();
                break;
        }
    }
}
