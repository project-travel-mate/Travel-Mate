package io.github.project_travel_mate.mytrips;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.dd.processbutton.FlatButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.searchcitydialog.CitySearchDialogCompat;
import io.github.project_travel_mate.searchcitydialog.CitySearchModel;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.TravelmateSnackbars;
import utils.Utils;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

/**
 * Activity to add new trip
 */
public class AddNewTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        View.OnClickListener, TravelmateSnackbars {

    private static final String DATEPICKER_TAG1 = "datepicker1";
    @BindView(R.id.select_city_name)
    TextView cityName;
    @BindView(R.id.sdate)
    TextView tripStartDate;
    @BindView(R.id.ok)
    FlatButton ok;
    @BindView(R.id.tname)
    EditText tripName;
    @BindView(R.id.linear_layout)
    LinearLayout mLinearLayout;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private String mCityid;
    private String mStartdate;
    private String mTripname;
    private String mToken;
    private MaterialDialog mDialog;
    private Handler mHandler;
    private DatePickerDialog mDatePickerDialog;
    private ArrayList<CitySearchModel> mSearchCities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mHandler = new Handler(Looper.getMainLooper());
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        final Calendar calendar = Calendar.getInstance();

        mDatePickerDialog = new DatePickerDialog(
                AddNewTripActivity.this,
                AddNewTripActivity.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        fetchCitiesList();

        tripStartDate.setOnClickListener(this);
        ok.setOnClickListener(this);
        cityName.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
          
    /**
     * Calls API to add  new trip
     */
    private void addTrip() {

        // Show a mDialog box
        mDialog = new MaterialDialog.Builder(AddNewTripActivity.this)
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
                mHandler.post(() -> {
                    mDialog.dismiss();
                    networkError();
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String res = Objects.requireNonNull(response.body()).string();
                    final int responseCode = response.code();
                    mHandler.post(() -> {
                        if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                            TravelmateSnackbars.createSnackBar(findViewById(R.id.activityAddNewTrip),
                                    R.string.trip_added, Snackbar.LENGTH_LONG).show();
                            //Call back to MytripsFragment
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK , returnIntent);
                            finish();

                        } else {
                            TravelmateSnackbars.createSnackBar(findViewById(R.id.activityAddNewTrip),
                                    res, Snackbar.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    networkError();
                    mDialog.dismiss();
                }
                mDialog.dismiss();

            }
        });
    }

    /**
     * Fetches the list cities from server
     */
    private void fetchCitiesList() {

        String uri = API_LINK_V2 + "get-all-cities/10";
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
            public void onResponse(Call call, final Response response) {
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();
                            Log.v("RESULT", res);
                            JSONArray ar = new JSONArray(res);
                            for (int i = 0; i < ar.length(); i++) {
                                mSearchCities.add(new CitySearchModel(
                                        ar.getJSONObject(i).getString("city_name"),
                                        ar.getJSONObject(i).optString("image"),
                                        ar.getJSONObject(i).getString("id")));
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                        }
                    } else {
                        Log.e("ERROR", "Network error");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Set Start date
            case R.id.sdate:
                mDatePickerDialog.show();
                break;
            // Add a new trip
            case R.id.ok:
                Utils.hideKeyboard(this);
                mTripname = tripName.getText().toString();

                if (mTripname.trim().equals("")) {
                    TravelmateSnackbars.createSnackBar(findViewById(R.id.activityAddNewTrip),
                            R.string.trip_name_blank, Snackbar.LENGTH_LONG).show();
                } else if (tripStartDate == null || tripStartDate.getText().toString().equals("")) {
                    TravelmateSnackbars.createSnackBar(findViewById(R.id.activityAddNewTrip),
                            R.string.trip_date_blank, Snackbar.LENGTH_LONG).show();
                } else if (mCityid == null) {
                    TravelmateSnackbars.createSnackBar(findViewById(R.id.activityAddNewTrip),
                            R.string.trip_city_blank, Snackbar.LENGTH_LONG).show();
                } else
                    addTrip();
            
                break;
            case R.id.select_city_name :
                new CitySearchDialogCompat(AddNewTripActivity.this, getString(R.string.search_title),
                        getString(R.string.search_hint), null, mSearchCities,
                        (SearchResultListener<CitySearchModel>) (dialog, item, position) -> {
                            mCityid = item.getId();
                            cityName.setText(item.getTitle());
                            dialog.dismiss();
                        }).show();
                break;

        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AddNewTripActivity.class);
        return intent;
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        mLinearLayout.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        Log.d("Month", String.valueOf(month));
        mStartdate = Long.toString(calendar.getTimeInMillis() / 1000);
        tripStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }
}