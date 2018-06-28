package io.github.project_travel_mate.destinations.description;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

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
public class FinalCityInfoActivity extends AppCompatActivity implements View.OnClickListener, FinalCityInfoView {

    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.humidit)
    TextView humidity;
    @BindView(R.id.weatherinfo)
    TextView weatherinfo;
    @BindView(R.id.head)
    TextView title;
    @BindView(R.id.image)
    ImageView iv;
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
    private Typeface mCode;
    private Typeface mCodeBold;
    private MaterialDialog mDialog;
    private Handler mHandler;
    private City mCity;
    private String mToken;
    private FinalCityInfoPresenter mFinalCityInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_city_info);

        ButterKnife.bind(this);

        mFinalCityInfoPresenter = new FinalCityInfoPresenter();

        mCode = Typeface.createFromAsset(getAssets(), "fonts/whitney_book.ttf");
        mCodeBold = Typeface.createFromAsset(getAssets(), "fonts/CODE_Bold.otf");
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
        mFinalCityInfoPresenter.fetchCityWeather(mCity.getNickname(), mToken);
        mFinalCityInfoPresenter.fetchCityInfo(mCity.getId(), mToken);
    }

    /**
     * Initialize view items with information
     * received from previous intent
     */
    private void initUi() {
        setTitle(mCity.getNickname());
        title.setTypeface(mCodeBold);
        title.setText(mCity.getNickname());
        Picasso.with(this).load(mCity.getAvatar()).into(iv);

        if (mCity.getFunFactsCount() < 1) {
            funfact.setVisibility(View.GONE);
        }

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTypeFaces();
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

    private void setTypeFaces() {
        Integer[] textViewids = new Integer[]{R.id.fftext, R.id.hgtext,
                R.id.shtext, R.id.mntext, R.id.rstext, R.id.cttext};
        TextView funFactsTextView;
        for (Integer id : textViewids) {
            funFactsTextView = findViewById(id);
            funFactsTextView.setTypeface(mCode);
        }
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
        mDialog = new MaterialDialog.Builder(FinalCityInfoActivity.this)
                .title(getString(R.string.app_name))
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissProgress() {
        mDialog.dismiss();
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
            Picasso.with(FinalCityInfoActivity.this).load(iconUrl).into(ico);
            temp.setText(tempText);
            humidity.setText(humidityText);
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
            des.setText(description);
            mCity.setDescription(description);
            mCity.setLatitude(latitude);
            mCity.setLongitude(longitude);
        });
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
}
