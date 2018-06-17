package io.github.project_travel_mate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

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

import static utils.Constants.API_LINK_V2;
import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.DESTINATION_CITY_ID;
import static utils.Constants.DESTINATION_CITY_LAT;
import static utils.Constants.DESTINATION_CITY_LON;
import static utils.Constants.SOURCE_CITY;
import static utils.Constants.SOURCE_CITY_ID;
import static utils.Constants.SOURCE_CITY_LAT;
import static utils.Constants.SOURCE_CITY_LON;
import static utils.Constants.USER_TOKEN;

public class SelectCityFragment extends Fragment {

    private Activity mActivity;

    @BindView(R.id.source) Spinner sourceSpinner;
    @BindView(R.id.destination) Spinner destinationSpinner;
    @BindView(R.id.pb) ProgressBar progressBar;

    private String[] mCities;
    private String mToken;
    private SharedPreferences mSharedPreferences;
    // TODO :: Remove these array lists & replace with City object
    private final List<String> mId = new ArrayList<>();
    private final List<String> mNames = new ArrayList<>();
    private final List<String> mLatitude = new ArrayList<>();
    private final List<String> mLongitude = new ArrayList<>();
    private Handler mHandler;

    public SelectCityFragment() {}

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_select_city, container, false);
        ButterKnife.bind(this, view);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mHandler = new Handler(Looper.getMainLooper());
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        getcitytask();

        return view;
    }

    @OnClick(R.id.ok)
    public void okClicked(View view) {

        int sposition = sourceSpinner.getSelectedItemPosition();
        int dposition = destinationSpinner.getSelectedItemPosition();

        if (sposition == dposition) {
            Snackbar.make(view, R.string.source_dest_cant_be_same, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(DESTINATION_CITY_ID, mId.get(dposition));
            editor.putString(SOURCE_CITY_ID, mId.get(sposition));
            editor.putString(DESTINATION_CITY, mNames.get(dposition));
            editor.putString(SOURCE_CITY, mNames.get(sposition));
            editor.putString(DESTINATION_CITY_LAT, mLatitude.get(dposition));
            editor.putString(SOURCE_CITY_LAT, mLatitude.get(sposition));
            editor.putString(DESTINATION_CITY_LON, mLongitude.get(dposition));
            editor.putString(SOURCE_CITY_LON, mLongitude.get(sposition));
            //mActivity.startService(new Intent(mActivity, LocationService.class));
            editor.apply();
            Snackbar.make(view, R.string.source_dest_set, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void getcitytask() {

        // to fetch city mNames
        String uri = API_LINK_V2 + "get-all-cities";

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
            public void onResponse(Call call, final Response response) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONArray ar = new JSONArray(Objects.requireNonNull(response.body()).string());
                            for (int i = 0; i < ar.length(); i++) {
                                mId.add(ar.getJSONObject(i).getString("id"));
                                mNames.add(ar.getJSONObject(i).getString("city_name"));
                                mLatitude.add(ar.getJSONObject(i).getString("latitude"));
                                mLongitude.add(ar.getJSONObject(i).getString("longitude"));
                            }
                            mCities = new String[mId.size()];
                            mCities = mNames.toArray(mCities);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    mActivity,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    mCities);
                            sourceSpinner.setAdapter(adapter);
                            destinationSpinner.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", "Message : " + e.getMessage());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
    }
}
