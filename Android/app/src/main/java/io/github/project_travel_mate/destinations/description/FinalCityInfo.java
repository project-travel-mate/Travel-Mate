package io.github.project_travel_mate.destinations.description;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.funfacts.FunFacts;

import static utils.Constants.EXTRA_MESSAGE_ID;
import static utils.Constants.EXTRA_MESSAGE_IMAGE;
import static utils.Constants.EXTRA_MESSAGE_LATITUDE;
import static utils.Constants.EXTRA_MESSAGE_LONGITUDE;
import static utils.Constants.EXTRA_MESSAGE_NAME;
import static utils.Constants.EXTRA_MESSAGE_TYPE;

/**
 * Fetch city information for given city mId
 */
public class FinalCityInfo extends AppCompatActivity implements View.OnClickListener, FinalCityInfoView {

    private Typeface mCode;
    private Typeface mCodeBold;
    private MaterialDialog mDialog;
    private Handler mHandler;

    private String mId;
    private String mTitle;
    private String mImage;
    private String mLatitude;
    private String mLongitude;

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

    private FinalCityInfoPresenter mFinalCityInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_city_info);

        ButterKnife.bind(this);

        mFinalCityInfoPresenter = new FinalCityInfoPresenter();

        mCode = Typeface.createFromAsset(getAssets(), "fonts/whitney_book.ttf");
        mCodeBold = Typeface.createFromAsset(getAssets(), "fonts/CODE_Bold.otf");
        mHandler        = new Handler(Looper.getMainLooper());

        Intent intent   = getIntent();
        mTitle          = intent.getStringExtra(EXTRA_MESSAGE_NAME);
        mId = intent.getStringExtra(EXTRA_MESSAGE_ID);
        mImage = intent.getStringExtra(EXTRA_MESSAGE_IMAGE);

        initUi();
        initPresenter();
    }

    private void initPresenter() {
        mFinalCityInfoPresenter.attachView(this);
        mFinalCityInfoPresenter.fetchCityInfo(mId);
    }

    private void initUi() {
        des.setText(getString(R.string.sample_string));
        setTitle(mTitle);
        title.setTypeface(mCodeBold);
        title.setText(mTitle);
        // Load mImage into ImageView
        Picasso.with(this).load(mImage).into(iv);

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
        TextView mFunFactsTextView = findViewById(R.id.fftext);
        mFunFactsTextView.setTypeface(mCode);
        mFunFactsTextView = findViewById(R.id.hgtext);
        mFunFactsTextView.setTypeface(mCode);
        mFunFactsTextView = findViewById(R.id.shtext);
        mFunFactsTextView.setTypeface(mCode);
        mFunFactsTextView = findViewById(R.id.mntext);
        mFunFactsTextView.setTypeface(mCode);
        mFunFactsTextView = findViewById(R.id.rstext);
        mFunFactsTextView.setTypeface(mCode);
        mFunFactsTextView = findViewById(R.id.cttext);
        mFunFactsTextView.setTypeface(mCode);
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
                intent = new Intent(FinalCityInfo.this, FunFacts.class);
                intent.putExtra(EXTRA_MESSAGE_ID, mId);
                intent.putExtra(EXTRA_MESSAGE_NAME, mTitle);
                startActivity(intent);
                break;
            case R.id.restau:
                fireIntent(new Intent(FinalCityInfo.this, PlacesOnMap.class), "restaurant");
                break;
            case R.id.hangout:
                fireIntent(new Intent(FinalCityInfo.this, PlacesOnMap.class), "hangout");
                break;
            case R.id.monu:
                fireIntent(new Intent(FinalCityInfo.this, PlacesOnMap.class), "monument");
                break;
            case R.id.shoppp:
                fireIntent(new Intent(FinalCityInfo.this, PlacesOnMap.class), "shopping");
                break;
            case R.id.trends:
                intent = new Intent(FinalCityInfo.this, Tweets.class);
                intent.putExtra(EXTRA_MESSAGE_ID, mId);
                intent.putExtra(EXTRA_MESSAGE_NAME, mTitle);
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
        mDialog = new MaterialDialog.Builder(FinalCityInfo.this)
                .title(getString(R.string.app_name))
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissProgress() {
        mDialog.dismiss();
    }

    /**
     * method called by FinalCityInfoPresenter when the network
     * request to fetch city information comes back successfully
     * used to display the fetched information from backend on activity
     * @param description - description of city
     * @param iconUrl - mImage url
     * @param temp - current temperature of requested city
     * @param humidity - current humidity of requested city
     * @param weatherInfo - weather information of requested city
     * @param lat - latitude of requested city
     * @param lon - longitude of requested city
     */
    @Override
    public void parseResult(final String description,
                            final String iconUrl,
                            final String temp,
                            final String humidity,
                            final String weatherInfo,
                            final String lat,
                            final String lon) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                des.setText(description);
                Picasso.with(FinalCityInfo.this).load(iconUrl).into(ico);
                String temperatureText = temp + (char) 0x00B0 + " C ";
                FinalCityInfo.this.temp.setText(temperatureText);
                String humidityText = "Humidity : " + humidity;
                FinalCityInfo.this.humidity.setText(humidityText);
                weatherinfo.setText(weatherInfo);
                FinalCityInfo.this.mLatitude = lat;
                FinalCityInfo.this.mLongitude = lon;
            }
        });
    }

    /**
     * Fires an Intent with given parameters
     *
     * @param intent Intent to be fires
     * @param type the type to be passed as extra parameter
     */
    private void fireIntent(Intent intent, String type) {
        intent.putExtra(EXTRA_MESSAGE_ID, mId);
        intent.putExtra(EXTRA_MESSAGE_LATITUDE, mLatitude);
        intent.putExtra(EXTRA_MESSAGE_LONGITUDE, mLongitude);
        intent.putExtra(EXTRA_MESSAGE_NAME, mTitle);
        intent.putExtra(EXTRA_MESSAGE_TYPE, type);
        startActivity(intent);
    }
}
