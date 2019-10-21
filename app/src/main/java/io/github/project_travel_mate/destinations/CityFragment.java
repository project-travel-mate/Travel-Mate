package io.github.project_travel_mate.destinations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.CustomTarget;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import database.AppDataBase;
import flipviewpager.utils.FlipSettings;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.description.FinalCityInfoActivity;
import objects.City;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.AUTHORIZATION;
import static utils.Constants.LAST_CACHE_TIME;
import static utils.Constants.SPOTLIGHT_SHOW_COUNT;
import static utils.Constants.USER_TOKEN;

public class CityFragment extends Fragment implements TravelmateSnackbars {

    private static final String TAG = "CityFragment";

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.cities_list)
    ListView lv;

    private MaterialSearchView mMaterialSearchView;
    private final int[] mColors = {R.color.sienna, R.color.saffron, R.color.green, R.color.pink,
            R.color.orange, R.color.blue, R.color.grey, R.color.yellow, R.color.purple, R.color.peach};

    private String mNameyet;
    private Activity mActivity;
    private Handler mHandler;
    private String mToken;

    private int mSpotlightShownCount;
    private SharedPreferences mSharedPreferences;

    private AppDataBase mDatabase;
    private SimpleDateFormat mFormat;

    private CityAdapter mCityAdapter;
    private FlipSettings mSettings = new FlipSettings.Builder().defaultPage().build();
    private ArrayList<City> mCities = new ArrayList<>();
    private View mSpotView;
    private List<String> mInterests;

    public CityFragment() {
    }

    public static CityFragment newInstance() {
        CityFragment fragment = new CityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_citylist, container, false);

        ButterKnife.bind(this, view);

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        mSpotlightShownCount = mSharedPreferences.getInt(SPOTLIGHT_SHOW_COUNT, 0);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        mHandler = new Handler(Looper.getMainLooper());

        // make an target
        mSpotView = inflater.inflate(R.layout.spotlight_target, null);

        mMaterialSearchView = view.findViewById(R.id.search_view);
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.v("QUERY ITEM : ", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNameyet = newText;
                if (!mNameyet.contains(" ") && mNameyet.length() % 3 == 0) {
                    cityAutoComplete();
                }
                return true;
            }
        });

        mDatabase = AppDataBase.getAppDatabase(mActivity);

        mCityAdapter = new CityAdapter(mActivity, mCities, mSettings);

        lv.setAdapter(mCityAdapter);
        lv.setOnItemClickListener((parent, mView, position, id1) -> {
            City city = (City) lv.getAdapter().getItem(position);
            Intent intent = FinalCityInfoActivity.getStartIntent(mActivity, city);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCities.clear();
        mCities = new ArrayList<>(Arrays.asList(mDatabase.cityDao().loadAll()));
        mInterests = new ArrayList<>(Arrays.asList(
                mActivity.getString(R.string.interest_know_more),
                mActivity.getString(R.string.interest_weather),
                mActivity.getString(R.string.interest_fun_facts),
                mActivity.getString(R.string.interest_trends)
        ));

        if (checkCachedCities(mCities)) {
            fetchCitiesList();
        } else {
            animationView.setVisibility(View.GONE);

            for (City city : mCities)
                city.mInterests = mInterests;

            mCityAdapter.updateData(mCities);

            if (mSpotlightShownCount <= 3) {
                showSpotlightView(mSpotView);
            }
        }
    }

    /**
     * shows spotlight on city card for the first 3 times.
     *
     * @param spotView - the view to be highlighted
     */
    private void showSpotlightView(View spotView) {
        CustomTarget customTarget = new CustomTarget.Builder(getActivity())
                .setPoint(180f, 430f)
                .setShape(new Circle(200f))
                .setOverlay(spotView)
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<CustomTarget>() {
                    @Override
                    public void onStarted(CustomTarget target) {
                        // do something
                    }

                    @Override
                    public void onEnded(CustomTarget target) {
                        // do something
                    }
                })
                .build();


        Spotlight spotlight =
                Spotlight.with(mActivity)
                        .setOverlayColor(R.color.spotlight)
                        .setDuration(1000L)
                        .setAnimation(new DecelerateInterpolator(2f))
                        .setTargets(customTarget)
                        .setClosedOnTouchedOutside(false)
                        .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                            @Override
                            public void onStarted() {
                                //do something
                            }

                            @Override
                            public void onEnded() {
                                //do something
                            }
                        });
        spotlight.start();

        View.OnClickListener closeSpotlight = v -> {
            spotlight.closeSpotlight();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putInt(SPOTLIGHT_SHOW_COUNT, mSpotlightShownCount + 1);
            editor.apply();
        };

        spotView.findViewById(R.id.close_spotlight).setOnClickListener(closeSpotlight);
    }

    /**
     * Check cached cities with expiration time 24 hours
     *
     * @param mCities - list of cities object
     **/
    private boolean checkCachedCities(List<City> mCities) {
        return mCities.size() == 0 || is24Hours();
    }

    /**
     * Check time more than 24 hours
     **/
    private boolean is24Hours() {
        long mMillisPerDay = 24 * 60 * 60 * 1000L;
        boolean mMoreThanDay = false;
        Date mCurrentDate = new Date();
        try {
            String mDateString = mSharedPreferences.getString(LAST_CACHE_TIME, "");
            if (!mDateString.isEmpty()) {
                Date mExpiry = mFormat.parse(mDateString);
                mMoreThanDay = Math.abs(mCurrentDate.getTime() - mExpiry.getTime()) > mMillisPerDay;
                //Delete record cached before 24 hours
                if (mMoreThanDay) {
                    mDatabase.cityDao().deleteAll();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mMoreThanDay;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mActivity.getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mMaterialSearchView.setMenuItem(item);
    }

    private void cityAutoComplete() {

        if (mNameyet.trim().equals(""))
            return;

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
                        Log.v(TAG, "response= " + arr.toString());

                        cities = new ArrayList<>();
                        citynames = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                cities.add(new City(arr.getJSONObject(i).getString("id"),
                                        arr.getJSONObject(i).getString("image"),
                                        arr.getJSONObject(i).getString("city_name"),
                                        arr.getJSONObject(i).getInt("facts_count"),
                                        R.color.sienna,
                                        getString(R.string.interest_know_more), getString(R.string.interest_weather),
                                        getString(R.string.interest_fun_facts), getString(R.string.interest_trends)));
                                citynames.add(arr.getJSONObject(i).getString("city_name"));

                            } catch (JSONException e) {
                                Log.e(TAG, "error parsing JSON, ", e);
                            }
                        }
                        ArrayAdapter<String> dataAdapter =
                                new ArrayAdapter<>(
                                        mActivity.getApplicationContext(), R.layout.spinner_layout, citynames);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mMaterialSearchView.setAdapter(dataAdapter);
                        mMaterialSearchView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
                            Intent intent = FinalCityInfoActivity.getStartIntent(mActivity, cities.get(arg2));
                            startActivity(intent);
                        });
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing JSON, " + e.getMessage());
                    } catch (IOException e) {
                        Log.e(TAG, "Error, " + e);
                    }
                });
            }
        });
    }

    /**
     * Fetches the list of popular cities from server
     */
    private void fetchCitiesList() {

        // to fetch 6 city names
        String uri = API_LINK_V2 + "get-all-cities/10";
        Log.v(TAG, "url=" + uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .header(AUTHORIZATION, "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request Failed: " + e.getMessage());
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            String res = response.body().string();
                            Log.v(TAG, "result=" + res);

                            animationView.setVisibility(View.GONE);
                            JSONArray ar = new JSONArray(res);

                            for (int i = 0; i < ar.length(); i++) {
                                City city = new City(
                                        ar.getJSONObject(i).getString("id"),
                                        ar.getJSONObject(i).optString("image"),
                                        ar.getJSONObject(i).getString("city_name"),
                                        ar.getJSONObject(i).getInt("facts_count"),
                                        mColors[i], mInterests);
                                mCities.add(city);

                                //Inset into local DB
                                mDatabase.cityDao().insert(city);
                            }

                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(LAST_CACHE_TIME, mFormat.format(new Date()));
                            editor.apply();

                            Log.d(TAG, "get cities cities size = " + mCities.size());

                            mCityAdapter.updateData(mCities);

                            if (mSpotlightShownCount <= 3) {
                                showSpotlightView(mSpotView);
                            }

                        } catch (JSONException | IOException e) {
                            Log.e(TAG, "Error parsing mCities JSON : " + e.getMessage());
                            networkError();
                        } catch (SQLiteConstraintException exception) {
                            Log.e(TAG, "Error Sqlite : " + exception.getMessage());
                        }
                    } else {
                        networkError();
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

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }
}
