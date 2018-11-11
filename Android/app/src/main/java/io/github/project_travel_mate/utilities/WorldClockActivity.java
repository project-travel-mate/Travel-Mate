package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import utils.CurrencyConverterGlobal;

public class WorldClockActivity extends AppCompatActivity {

    @BindView(R.id.timezone_country_flag)
    ImageView from_image;
    @BindView(R.id.timezone_country_name)
    TextView timezone_name;
    Boolean flag_check_first_item = false;
    private Context mContext;
    String timezone_short = "Asia/Kolkata";
    private AnalogClock mAnalogClock;
    private String mFormat;
    private final String mHours12 = "hh:mm:ss a";
    private final String mHours24 = "k:mm:ss";

    //private DigitalClock mDigitalClock;

    private static TextClock mDigitalClock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_world_clock);
        ButterKnife.bind(this);
        setTitle(R.string.text_clock);


        mAnalogClock = findViewById(R.id.clock_analog);

        mDigitalClock = findViewById(R.id.clock_digital);

        mContext = this;

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag_check_first_item) {
            if (CurrencyConverterGlobal.global_image_id != 0 &&
                    !CurrencyConverterGlobal.global_country_name.isEmpty()) {
                from_image.setImageResource(CurrencyConverterGlobal.global_image_id);
                timezone_name.setText(CurrencyConverterGlobal.global_country_name);
                timezone_short = CurrencyConverterGlobal.country_id;

                setClock();
                Log.d("Key", timezone_short);
            }
        }
    }

    /**
     * set clock with timezone
     */
    private void setClock() {
        String timezone = CurrencyConverterGlobal.global_country_name.split(" ", 2)[0];

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        long gmtTime = calendar.getTime().getTime();

        long timezoneAlteredTime = gmtTime + TimeZone.getTimeZone(timezone).getRawOffset();
        Calendar clockCalender = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        clockCalender.setTimeInMillis(timezoneAlteredTime);

        //customization
        mAnalogClock.setCalendar(clockCalender)
                .setDiameterInDp(200.0f)
                .setOpacity(1.0f)
                .setColor(Color.BLACK);

        mDigitalClock.setTimeZone(timezone);
        setFormat();
    }

    @OnClick(R.id.zone_field)
    void fromSelected() {
        flag_check_first_item = true;
        Intent intent = new Intent(mContext, TimezoneListViewActivity.class);
        startActivity(intent);
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, WorldClockActivity.class);
        return intent;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return DateFormat.is24HourFormat(mContext);
    }

    private void setFormat() {
        if (get24HourMode()) {
            mDigitalClock.setFormat24Hour(mHours24);
        } else {
            mDigitalClock.setTimeZone(mHours12);
        }
    }
}
