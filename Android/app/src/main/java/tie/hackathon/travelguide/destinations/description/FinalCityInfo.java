package tie.hackathon.travelguide.destinations.description;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import tie.hackathon.travelguide.FunFacts;
import tie.hackathon.travelguide.PlacesOnMap;
import tie.hackathon.travelguide.R;
import tie.hackathon.travelguide.Tweets;

/**
 * Fetch city information for given city id
 */
public class FinalCityInfo extends AppCompatActivity implements View.OnClickListener, FinalCityInfoView {

    private Intent intent;
    private TextView fftext;
    private Typeface code;
    private Typeface tex;
    private Typeface codeb;
    private MaterialDialog dialog;
    private Handler mHandler;

    private String id;
    private String tit;
    private String image;
    private String description;
    private String lat;
    private String lon;

    @BindView(R.id.temp) TextView temp;
    @BindView(R.id.humidit) TextView humidity;
    @BindView(R.id.weatherinfo) TextView weatherinfo;
    @BindView(R.id.head) TextView title;
    @BindView(R.id.image) ImageView iv;
    @BindView(R.id.icon) ImageView ico;
    @BindView(R.id.expand_text_view) ExpandableTextView des;
    @BindView(R.id.funfact) LinearLayout funfact;
    @BindView(R.id.restau) LinearLayout restau;
    @BindView(R.id.hangout) LinearLayout hangout;
    @BindView(R.id.monu) LinearLayout monum;
    @BindView(R.id.shoppp) LinearLayout shopp;
    @BindView(R.id.trends) LinearLayout trend;

    FinalCityInfoPresenter mFinalCityInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_city_info);

        ButterKnife.bind(this);

        mFinalCityInfoPresenter = new FinalCityInfoPresenter();

        code        = Typeface.createFromAsset(getAssets(), "fonts/whitney_book.ttf");
        codeb       = Typeface.createFromAsset(getAssets(), "fonts/CODE_Bold.otf");
        tex         = Typeface.createFromAsset(getAssets(), "fonts/texgyreadventor-regular.otf");
        mHandler    = new Handler(Looper.getMainLooper());

        intent = getIntent();
        tit = intent.getStringExtra("name_");
        id = intent.getStringExtra("id_");
        image = intent.getStringExtra("image_");

        initUi();
        initPresenter();
    }

    private void initPresenter() {
        mFinalCityInfoPresenter.attachView(this);
        mFinalCityInfoPresenter.fetchCityInfo(id);
    }

    private void initUi() {
        des.setText(getString(R.string.sample_string));
        setTitle(tit);
        title.setTypeface(codeb);
        title.setText(tit);
        // Load image into ImageView
        Picasso.with(this).load(image).into(iv);

        getSupportActionBar().setHomeButtonEnabled(true);
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
        fftext = (TextView) findViewById(R.id.fftext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.hgtext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.shtext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.mntext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.rstext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.cttext);
        fftext.setTypeface(code);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.funfact:
                i = new Intent(FinalCityInfo.this, FunFacts.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                startActivity(i);
                break;
            case R.id.restau:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("lat_", lat);
                i.putExtra("lng_", lon);
                i.putExtra("name_", tit);
                i.putExtra("type_", "restaurant");
                startActivity(i);
                break;
            case R.id.hangout:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("lat_", lat);
                i.putExtra("lng_", lon);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "hangout");
                startActivity(i);
                break;
            case R.id.monu:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("lat_", lat);
                i.putExtra("lng_", lon);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "monument");
                startActivity(i);
                break;
            case R.id.shoppp:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("lat_", lat);
                i.putExtra("lng_", lon);
                i.putExtra("type_", "shopping");
                startActivity(i);
                break;
            case R.id.trends:
                i = new Intent(FinalCityInfo.this, Tweets.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                startActivity(i);
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
        dialog = new MaterialDialog.Builder(FinalCityInfo.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissProgress() {
        dialog.dismiss();
    }

    @Override
    public void parseResult(String description, String iconUrl, String temp, String humidity, String weatherInfo,
                            String lat, String lon) {
        mHandler.post(() -> {
            des.setText(description);
            Picasso.with(FinalCityInfo.this).load(iconUrl).into(ico);
            this.temp.setText(temp + (char) 0x00B0 + " C ");
            this.humidity.setText("Humidity : " + humidity);
            weatherinfo.setText(weatherInfo);
            this.lat = lat;
            this.lon = lon;
        });
    }
}
