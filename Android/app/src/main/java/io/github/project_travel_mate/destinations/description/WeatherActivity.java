package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;
import objects.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.CURRENT_TEMP;
import static utils.Constants.EXTRA_MESSAGE_CALLED_FROM_UTILITIES;
import static utils.Constants.EXTRA_MESSAGE_CITY_ID;
import static utils.Constants.EXTRA_MESSAGE_CITY_NAME;
import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.NUM_DAYS;
import static utils.Constants.USER_TOKEN;
import static utils.WeatherUtils.fetchDrawableFileResource;
import static utils.WeatherUtils.getDayOfWeek;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.weather_condition)
    TextView condition;
    @BindView(R.id.weather_icon)
    ImageView icon;
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.min_temp)
    TextView maxTemp;
    @BindView(R.id.max_temp)
    TextView minTemp;
    @BindView(R.id.today)
    TextView today;
    @BindView(R.id.day_of_week)
    TextView dayOfweek;
    @BindView(R.id.forecast_list)
    RecyclerView forecastList;
    @BindView(R.id.empty_textview)
    TextView emptyView;

    private City mCity;
    private String mToken;
    private String mCurrentTemp;
    private Handler mHandler;
    private ArrayList<Weather> mWeatherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        mHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();

        //check if it's called after searching for a city in utilities
        boolean isCalledFromUtilities = intent.getBooleanExtra(EXTRA_MESSAGE_CALLED_FROM_UTILITIES,
                false);
        if (isCalledFromUtilities) {
            //create a new city object by getting values from received intent
            String cityNickname = intent.getStringExtra(EXTRA_MESSAGE_CITY_NAME);
            String cityID = intent.getStringExtra(EXTRA_MESSAGE_CITY_ID);
            mCity = new City(cityNickname, cityID);
            fetchCurrentTemp();
        } else {
            //get reference to current City
            mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
            //get current temperature from FinalCityInfo
            mCurrentTemp = intent.getStringExtra(CURRENT_TEMP);
            if (mCurrentTemp != null) {
                //if called from within a FinalCityInfo activity
                //directly fetch weather info
                fetchWeatherForecast();
            } else {
                //if called directly from cities list then
                //first fetch current temp here
                fetchCurrentTemp();
            }

        }
        //set text for empty view when no weather forecast data is returned
        emptyView.setText(String.format(getString(R.string.city_not_found), mCity.getNickname()));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(mCity.getNickname());
    }

    /**
     * called to fetch the weather forecast for the current city
     * for a given number of days
     */
    private void fetchWeatherForecast() {
        // to fetch weather forecast by city name
        String uri = API_LINK_V2 + "get-multiple-days-weather/" + NUM_DAYS + "/" + mCity.getNickname();
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
                mHandler.post(() -> {
                    Log.e("Request Failed", "Message : " + e.getMessage());
                    cancelAnimation();
                    networkError();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonResponse = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONArray array = new JSONArray(jsonResponse);
                            for (int i = 0; i < array.length(); i++) {

                                //parse the json response to obtain the required values
                                String weatherCondition = array.getJSONObject(i).getString("condensed");

                                double maxT = array.getJSONObject(i).getDouble("max_temp");
                                double minT = array.getJSONObject(i).getDouble("min_temp");
                                //rounding off the min/max temperature values to the nearest integer
                                int maxTemperature = (int) Math.rint(maxT);
                                int minTemperature = (int) Math.rint(minT);

                                //get the current day of the week for each day
                                String dayOfWeek = getDayOfWeek(i, "EEEE");

                                //obtain the weather icon url and the weather condition code
                                String iconUrl = array.getJSONObject(i).getString("icon");
                                int code = array.getJSONObject(i).getInt("code");
                                //get the vector drawable resource id for the weather icon
                                int id = fetchDrawableFileResource(WeatherActivity.this, iconUrl, code);

                                if (i == 0) {
                                    //current day's weather stats to be displayed
                                    condition.setText(weatherCondition);
                                    temp.setText(mCurrentTemp);

                                    //set the temperatures, add vectors icons for min/max textviews
                                    maxTemp.setText(String.valueOf(maxTemperature));
                                    maxTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward,
                                            0, 0, 0);
                                    minTemp.setText(String.valueOf(minTemperature));
                                    minTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward,
                                            0, 0, 0);

                                    dayOfweek.setText(dayOfWeek);

                                    icon.setImageResource(id);
                                    //change the color of weather icon vector from default black to white
                                    DrawableCompat.setTint(icon.getDrawable(),
                                            ContextCompat.getColor(WeatherActivity.this, android.R.color.white));
                                    today.setText(R.string.today);

                                } else {
                                    //remaining days stats to be displayed in the horizontal RecyclerView
                                    String day = getDayOfWeek(i, "dd MMM");
                                    Weather weather = new Weather(id, maxTemperature,
                                            minTemperature, dayOfWeek.substring(0, 3), day);
                                    mWeatherList.add(weather);

                                    //add divider between individual items
                                    forecastList.addItemDecoration(new DividerItemDecoration(WeatherActivity.this,
                                            DividerItemDecoration.HORIZONTAL));
                                    forecastList.setLayoutManager(new LinearLayoutManager(WeatherActivity.this,
                                            LinearLayoutManager.HORIZONTAL, false));

                                    //set the adapter for the recycler view displaying the forecast
                                    WeatherAdapter adapter = new WeatherAdapter(WeatherActivity.this, mWeatherList);
                                    forecastList.setAdapter(adapter);
                                }
                            }
                            //cancel the loading animation once the data is fetched
                            cancelAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //display no data error animation
                            emptyListAnimation();
                        } catch (IOException e) {
                            //display no internet connection error
                            networkError();
                        }
                    } else {
                        //display animation for no data returned from the API call
                        emptyListAnimation();
                    }
                });
            }

        });
    }

    /**
     * Fetches current temp of city
     */
    private void fetchCurrentTemp() {

        String uri = API_LINK_V2 + "get-city-weather/" + mCity.getId();
        uri = uri.replaceAll(" ", "%20");

        //Set up client
        OkHttpClient client = new OkHttpClient();

        Log.v("EXECUTING", uri);

        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {

                    final String res = Objects.requireNonNull(response.body()).string();
                    try {
                        JSONObject responseObject = new JSONObject(res);
                        mCurrentTemp = responseObject.getString("temp") +
                                        (char) 0x00B0 + responseObject.getString("temp_units");
                        fetchWeatherForecast();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mHandler.post(() -> networkError());
                    }
                } else {
                    mHandler.post(() -> emptyListAnimation());
                }
            }
        });
    }

    /**
     * provide back navigation on the action bar
     * @return boolean stating Up Navigation has been handled
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * called to start the WeatherActivity via an intent
     * @param context context to access application resources
     * @param city City object containing the details of the current city
     * @param currentTemp current temperature of the current city
     * @return reference to intent object to start the activity
     */
    public static Intent getStartIntent(Context context, City city, String currentTemp) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, city);
        intent.putExtra(CURRENT_TEMP, currentTemp);
        return intent;
    }

    /**
     * called to start WeatherActivity from Utilities after user
     * has searched for a city
     * @param context context to access application resources
     * @param cityName name of he city for which weather is to be displayed
     * @param cityId id of the city for which weather is to be displayed
     * @param calledFromUtilities to check if it's called from Utilities or not
     * @return intent object
     */
    public static Intent getStartIntent(Context context, String cityName,
                                        String cityId, boolean calledFromUtilities) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CITY_NAME, cityName);
        intent.putExtra(EXTRA_MESSAGE_CITY_ID, cityId);
        intent.putExtra(EXTRA_MESSAGE_CALLED_FROM_UTILITIES, calledFromUtilities);
        return intent;
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    /**
     * Plays the no data found animation in the view
     */
    private void emptyListAnimation() {
        emptyView.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    /**
     * Cancel animation after receiving response from the API
     */
    private void cancelAnimation() {
        if (animationView != null) {
            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
        }
    }
}
