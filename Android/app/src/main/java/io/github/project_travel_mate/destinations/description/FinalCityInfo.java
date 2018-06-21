package io.github.project_travel_mate.destinations.description;

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

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.funfacts.FunFacts;
import objects.City;

import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.EXTRA_MESSAGE_TYPE;
import static utils.Constants.USER_TOKEN;

/**
 * Fetch city information for given city mId
 */
public class FinalCityInfo extends AppCompatActivity implements View.OnClickListener, FinalCityInfoView {

    private Typeface mCode;
    private Typeface mCodeBold;
    private MaterialDialog mDialog;
    private Handler mHandler;

    private City mCity;
    private String mToken;

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
        mHandler = new Handler(Looper.getMainLooper());

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        initUi();
        initPresenter();
    }

    private void initPresenter() {
        mFinalCityInfoPresenter.attachView(this);
        mFinalCityInfoPresenter.fetchCityInfo(mCity.getNickname(), mToken);
    }

    /**
     * Initialize view items with information
     * received from previous intent
     */
    private void initUi() {
        des.setText(mCity.getDescription());
        setTitle(mCity.getNickname());
        title.setTypeface(mCodeBold);
        title.setText(mCity.getNickname());
        Picasso.with(this).load(mCity.getAvatar()).into(iv);

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
                intent = new Intent(FinalCityInfo.this, FunFacts.class);
                intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, mCity);
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
                intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, mCity);
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
     * request to fetch city information comes back successfully
     * used to display the fetched information from backend on activity
     *
     * @param iconUrl     - mImage url
     * @param tempText        - current temperature of requested city
     * @param humidityText    - current humidity of requested city
     * @param weatherDescription - weather information of requested city
     */
    @Override
    public void parseResult(final String iconUrl,
                            final String tempText,
                            final String humidityText,
                            final String weatherDescription) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(FinalCityInfo.this).load(iconUrl).into(ico);
                temp.setText(tempText);
                humidity.setText(humidityText);
                weatherinfo.setText(weatherDescription);
            }
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
}
