package io.github.project_travel_mate.destinations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import flipviewpager.utils.FlipSettings;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.description.FinalCityInfoActivity;
import io.github.project_travel_mate.utilities.AppConnectivity;
import objects.City;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class CityFragment extends Fragment {

    @BindView(R.id.cityname)
    AutoCompleteTextView cityname;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.music_list)
    ListView lv;

    private String mNameyet;
    private Activity mActivity;
    private Handler mHandler;
    private String mToken;

    public CityFragment() {
    }

    public static CityFragment newInstance() {
        CityFragment fragment = new CityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppConnectivity mConnectivity = new AppConnectivity(getContext());
        View view = inflater.inflate(R.layout.fragment_citylist, container, false);

        ButterKnife.bind(this, view);

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        mHandler = new Handler(Looper.getMainLooper());
        cityname.setThreshold(1);

        if (mConnectivity.isOnline()) {
            pb.setVisibility(View.VISIBLE);
            getCity();
        } else {
            final Snackbar snackbar = Snackbar.make(view, R.string.internet_connectivity, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        return view;
    }

    @OnTextChanged(R.id.cityname)
    void onTextChanged() {
        mNameyet = cityname.getText().toString();
        if (!mNameyet.contains(" ") && mNameyet.length() % 3 == 0) {
            cityAutoComplete();
        }
    }

    private void cityAutoComplete() {

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
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    JSONArray arr;
                    final ArrayList<City> cities;
                    final ArrayList<String> citynames;
                    try {
                        arr = new JSONArray(Objects.requireNonNull(response.body()).string());
                        Log.v("RESPONSE : ", arr.toString());

                        cities = new ArrayList<>();
                        citynames = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                cities.add(new City(arr.getJSONObject(i).getString("id"),
                                        arr.getJSONObject(i).getString("image"),
                                        arr.getJSONObject(i).getString("city_name"),
                                        arr.getJSONObject(i).getInt("facts_count"),
                                        "Know More", "View on Map", "Fun Facts", "City Trends"));
                                citynames.add(arr.getJSONObject(i).getString("city_name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> dataAdapter =
                                new ArrayAdapter<>(
                                        mActivity.getApplicationContext(), R.layout.spinner_layout, citynames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        cityname.setThreshold(1);
                        cityname.setAdapter(dataAdapter);
                        cityname.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                            Intent intent = FinalCityInfoActivity.getStartIntent(mActivity, cities.get(arg2));
                            startActivity(intent);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }

    private void getCity() {

        // to fetch 6 city names
        String uri = API_LINK_V2 + "get-all-cities/6";
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
                final int responseCode = response.code();
                mHandler.post(() -> {
                    try {
                        String res = null;
                        if (response.body() != null) {
                            res = response.body().string();
                        }
                        Log.v("RESULT", res);
                        JSONArray ar = new JSONArray(res);
                        pb.setVisibility(View.GONE);
                        FlipSettings settings = new FlipSettings.Builder().defaultPage().build();
                        List<City> cities = new ArrayList<>();
                        for (int i = 0; i < ar.length(); i++) {
                            cities.add(new City(
                                    ar.getJSONObject(i).getString("id"),
                                    ar.getJSONObject(i).optString("image"),
                                    ar.getJSONObject(i).getString("city_name"),
                                    ar.getJSONObject(i).getInt("facts_count"),
                                    "Know More", "View on Map", "Fun Facts", "City Trends"));
                        }

                        lv.setAdapter(new CityAdapter(mActivity, cities, settings));
                        lv.setOnItemClickListener((parent, view, position, id1) -> {
                            City city = (City) lv.getAdapter().getItem(position);
                            Toast.makeText(mActivity, city.getNickname(), Toast.LENGTH_SHORT).show();
                            Intent intent = FinalCityInfoActivity.getStartIntent(mActivity, city);
                            startActivity(intent);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

}
