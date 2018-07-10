package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.funfacts.FunFactsActivity;
import objects.City;

import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.EXTRA_MESSAGE_TYPE;
import static utils.Constants.USER_TOKEN;

/**
 * Fetch city information for given city mId
 */
public class FinalCityInfoActivity extends AppCompatActivity
        implements View.OnClickListener, FinalCityInfoView {

    @BindView(R.id.layout_content)
    LinearLayout content;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.humidit)
    TextView humidity;
    @BindView(R.id.weatherinfo)
    TextView weatherinfo;
    @BindView(R.id.head)
    TextView title;
    @BindView(R.id.image_slider)
    ViewPager imagesSliderView;
    @BindView(R.id.icon)
    ImageView ico;
    @BindView(R.id.expand_text_view)
    ExpandableTextView des;
    @BindView(R.id.funfact)
    LinearLayout funfact;
    @BindView(R.id.restau)
    LinearLayout restau;
    @BindView(R.id.hangout)
    LinearLayout hangout;
    @BindView(R.id.monu)
    LinearLayout monum;
    @BindView(R.id.shoppp)
    LinearLayout shopp;
    @BindView(R.id.trends)
    LinearLayout trend;
    @BindView(R.id.SliderDots)
    LinearLayout sliderDotspanel;
    private int mDotsCount;
    private ImageView[] mDots;
    private Handler mHandler;
    private City mCity;
    private String mToken;
    private FinalCityInfoPresenter mFinalCityInfoPresenter;
    int currentPage = 0;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_city_info);
        ButterKnife.bind(this);

        mFinalCityInfoPresenter = new FinalCityInfoPresenter();

        mHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        initUi();
        initPresenter();
    }

    private void initPresenter() {
        showProgress();
        mFinalCityInfoPresenter.attachView(this);
        mFinalCityInfoPresenter.fetchCityWeather(mCity.getId(), mToken);
        mFinalCityInfoPresenter.fetchCityInfo(mCity.getId(), mToken);
    }

    /**
     * Initialize view items with information
     * received from previous intent
     */
    private void initUi() {

        setTitle(mCity.getNickname());
        title.setText(mCity.getNickname());

        if (mCity.getFunFactsCount() < 1) {
            funfact.setVisibility(View.GONE);
        }

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setClickListeners();
    }

    private void setClickListeners() {
        funfact.setOnClickListener(this);
        restau.setOnClickListener(this);
        hangout.setOnClickListener(this);
        monum.setOnClickListener(this);
        shopp.setOnClickListener(this);
        trend.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.funfact:
                intent = FunFactsActivity.getStartIntent(FinalCityInfoActivity.this, mCity);
                startActivity(intent);
                break;
            case R.id.restau:
                fireIntent(PlacesOnMapActivity.getStartIntent(FinalCityInfoActivity.this), "restaurant");
                break;
            case R.id.hangout:
                fireIntent(PlacesOnMapActivity.getStartIntent(FinalCityInfoActivity.this), "hangout");
                break;
            case R.id.monu:
                fireIntent(PlacesOnMapActivity.getStartIntent(FinalCityInfoActivity.this), "monument");
                break;
            case R.id.shoppp:
                fireIntent(PlacesOnMapActivity.getStartIntent(FinalCityInfoActivity.this), "shopping");
                break;
            case R.id.trends:
                intent = TweetsActivity.getStartIntent(FinalCityInfoActivity.this, mCity);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void dismissProgress() {
    }

    /**
     * method called by FinalCityInfoPresenter when the network
     * request to fetch city weather information comes back successfully
     * used to display the fetched information from backend on activity
     *
     * @param iconUrl            - mImage url
     * @param tempText           - current temperature of requested city
     * @param humidityText       - current humidity of requested city
     * @param weatherDescription - weather information of requested city
     */
    @Override
    public void parseResult(final String iconUrl,
                            final String tempText,
                            final String humidityText,
                            final String weatherDescription) {
        mHandler.post(() -> {
            content.setVisibility(View.VISIBLE);
            Picasso.with(FinalCityInfoActivity.this).load(iconUrl).into(ico);
            temp.setText(tempText);
            humidity.setText(String.format(getString(R.string.humidity), humidityText));
            weatherinfo.setText(weatherDescription);
        });
    }

    /**
     * method called by FinalCityInfoPresenter when the network
     * request to fetch city information comes back successfully
     * used to display the fetched information from backend on activity
     *
     * @param description               city description
     * @param latitude                  city latitude
     * @param longitude                 city longitude
     * @param imagesArray               images array for the city
     */
    @Override
    public void parseInfoResult(final String description,
                                final String latitude,
                                final String longitude,
                                ArrayList<String> imagesArray) {
        mHandler.post(() -> {
            Log.e("description", description + " ");
            animationView.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            if (description != null && !description.equals("null"))
                des.setText(description);
            mCity.setDescription(description);
            mCity.setLatitude(latitude);
            mCity.setLongitude(longitude);
            slideImages(imagesArray);
        });
    }

    /**
     * auto slides images in the final city info
     * @param imagesArray array of images url
     */
    public void slideImages(ArrayList<String> imagesArray) {
        CityImageSliderAdapter adapter = new CityImageSliderAdapter(this, imagesArray);
        imagesSliderView.setAdapter(adapter);
        mDotsCount = adapter.getCount();
        mDots = new ImageView[mDotsCount];
        if (mDotsCount == 1) {
            sliderDotspanel.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < mDotsCount; i++) {
            mDots[i] = new ImageView(this);
            mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDotspanel.addView(mDots[i], params);
        }
        mDots[0].setImageDrawable(getResources().getDrawable(R.drawable.active_dot));

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == imagesArray.size()) {
                currentPage = 0;
            }
            for (int i = 0; i < mDotsCount; i++) {
                mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_active_dot));
            }
            mDots[currentPage].setImageDrawable(getResources().getDrawable(R.drawable.active_dot));
            imagesSliderView.setCurrentItem(currentPage++, true);
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 2000);
    }

    /**
     * Fires an Intent with given parameters
     *
     * @param intent Intent to be fires
     * @param type   the type to be passed as extra parameter
     */
    private void fireIntent(Intent intent, String type) {
        intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, mCity);
        intent.putExtra(EXTRA_MESSAGE_TYPE, type);
        startActivity(intent);
    }

    public static Intent getStartIntent(Context context, City city) {
        Intent intent = new Intent(context, FinalCityInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, city);
        return intent;
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }
}