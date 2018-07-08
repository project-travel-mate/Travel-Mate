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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
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
import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.NUM_DAYS;
import static utils.Constants.USER_TOKEN;

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
    RecyclerView forecast_list;
    @BindView(R.id.empty_textview)
    TextView emptyView;

    private City mCity;
    private String mToken;
    private Handler mHandler;
    private ArrayList<Weather> mWeatherList = new ArrayList<>();

    public static Intent getStartIntent(Context context, City city) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, city);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        mHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        //get reference to current City
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        //get current temperature from FinalCityInfo
        String currentTemp = intent.getStringExtra(CURRENT_TEMP);

        //set text for empty view when no weather forecast data is returned
        emptyView.setText(String.format(getString(R.string.city_not_found), mCity.getNickname()));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(mCity.getNickname());

        fetchWeatherForecast(currentTemp);

    }

    private void fetchWeatherForecast(String currentTemp) {
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
                cancelAnimation();
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = Objects.requireNonNull(response.body()).string();
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
                                String dayOfWeek = getDayOfWeek(i);

                                //obtain the weather icon url and the weather condition code
                                String iconUrl = array.getJSONObject(i).getString("icon");
                                int code = array.getJSONObject(i).getInt("code");
                                //get the vector drawable resource id for the weather icon
                                int id = fetchDrawableFileResource(iconUrl, code);

                                if (i == 0) {
                                    //current day's weather stats to be displayed
                                    condition.setText(weatherCondition);
                                    temp.setText(currentTemp);
                                    maxTemp.setText(String.valueOf(maxTemperature));
                                    maxTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_upward,
                                            0, 0, 0);
                                    minTemp.setText(String.valueOf(minTemperature));
                                    minTemp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_downward,
                                            0, 0, 0);
                                    dayOfweek.setText(dayOfWeek);
                                    icon.setImageResource(id);
                                    DrawableCompat.setTint(icon.getDrawable(),
                                            ContextCompat.getColor(WeatherActivity.this, android.R.color.white));
                                    today.setText(R.string.today);

                                } else {
                                    //remaining days stats to be displayed in the horizontal RecyclerView
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                                    Calendar calendar = new GregorianCalendar();
                                    calendar.add(Calendar.DATE, i);
                                    String day = dateFormat.format(calendar.getTime());

                                    Weather weather = new Weather(id, maxTemperature,
                                            minTemperature, dayOfWeek.substring(0, 3), day);
                                    mWeatherList.add(weather);
                                    forecast_list.addItemDecoration(new DividerItemDecoration(WeatherActivity.this,
                                            DividerItemDecoration.HORIZONTAL));
                                    forecast_list.setLayoutManager(new LinearLayoutManager(WeatherActivity.this,
                                            LinearLayoutManager.HORIZONTAL, false));

                                    WeatherAdapter adapter = new WeatherAdapter(WeatherActivity.this, mWeatherList);
                                    forecast_list.setAdapter(adapter);
                                }
                            }
                            //cancel the loading animation once the data is fetched
                            cancelAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private int fetchDrawableFileResource(String iconUrl, int code) {
        //formulate the vector drawable file name from the local json file using the icon url & code from the API call
        String imageDrawable = "wi_";
        String time = iconUrl.substring(iconUrl.lastIndexOf("/") + 1);
        imageDrawable += time.contains("d") ? "day" : "night";
        String suffix = getSuffix(code);
        imageDrawable += "_" + suffix;

        //return the vector drawable resource id
        return getResources().getIdentifier(imageDrawable, "drawable", "io.github.project_travel_mate");
    }

    /**
     * parses the icons.json file which contains the weather condition codes and descriptions
     * required to fetch the right weather icon to display
     *
     * @param code weather condition code
     * @return weather condition description
     */
    private String getSuffix(int code) {
        String json;
        String cond = "";
        try {
            InputStream is = getAssets().open("icons.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(String.valueOf(code))) {
                JSONObject object = jsonObject.getJSONObject(String.valueOf(code));
                cond = object.getString("icon");
            }

        } catch (JSONException ex) {
            emptyListAnimation();
        } catch (IOException e) {
            networkError();
        }
        return cond;
    }

    /**
     * called to get the days of the week needed to display the forecast
     *
     * @param index day's index
     * @return current day of the week as a String
     */
    private String getDayOfWeek(int index) {
        String today = "";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int dayIndex = (day + index - 1) % 7 + 1;
        switch (dayIndex) {
            case Calendar.MONDAY:
                today = getString(R.string.dayOfWeek_monday);
                break;
            case Calendar.TUESDAY:
                today = getString(R.string.dayOfWeek_tuesday);
                break;
            case Calendar.WEDNESDAY:
                today = getString(R.string.dayOfWeek_wednesday);
                break;
            case Calendar.THURSDAY:
                today = getString(R.string.dayOfWeek_thursday);
                break;
            case Calendar.FRIDAY:
                today = getString(R.string.dayOfWeek_friday);
                break;
            case Calendar.SATURDAY:
                today = getString(R.string.dayOfWeek_saturday);
                break;
            case Calendar.SUNDAY:
                today = getString(R.string.dayOfWeek_sunday);
                break;
        }
        return today;
    }

    /**
     * provide back navigation on the action bar
     *
     * @return boolean stating Up Navigation has been handled
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
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
