package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextClock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import adapters.TimezoneAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;

public class WorldClockActivity extends AppCompatActivity {

    private static final String DEFAULT_TIME_ZONE_KEY = "defaultTimeZone";

    @BindView(R.id.clock_analog)
    CustomAnalogClock mAnalogClock;

    @BindView(R.id.clock_digital)
    TextClock mTextClock;

    @BindView(R.id.actvTimezone)
    AutoCompleteTextView mAutoCompleteTextViewTimezone;

    private TimezoneAdapter mAdapter;
    private long mMiliSeconds;
    private Calendar mCurrent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_world_clock);
        ButterKnife.bind(this);
        setTitle(R.string.text_clock);

        mTextClock = findViewById(R.id.clock_digital);
        mAnalogClock = findViewById(R.id.clock_analog);

        String[] idArray = TimeZone.getAvailableIDs();
        this.mAdapter = new TimezoneAdapter(this, Arrays.asList(idArray));
        this.mAutoCompleteTextViewTimezone.setAdapter(this.mAdapter);

        /**
         * Analog Clock Initialization
         */
        mAnalogClock.init(WorldClockActivity.this, R.drawable.clock_face,
                R.drawable.hours_hand, R.drawable.minutes_hand,
                0, false, false);

        mAnalogClock.setScale(1f);
        // analog clock size

        getGMTTime();
        // get time from selected time zone

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }


    public static Intent getStartIntent(Context context) {
        return new Intent(context, WorldClockActivity.class);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function gets GMT time and also performs time calculation according to time zone.
     */
    private void getGMTTime() {
        mCurrent = Calendar.getInstance();
        mCurrent.add(Calendar.HOUR, -2);
        mMiliSeconds = mCurrent.getTimeInMillis();
        TimeZone tzCurrent = mCurrent.getTimeZone();
        int offset = tzCurrent.getRawOffset();
        if (tzCurrent.inDaylightTime(new Date())) {
            offset = offset + tzCurrent.getDSTSavings();
        }
        mMiliSeconds = mMiliSeconds - offset;
    }

    /**
     * This function saves the user selected time zone
     *
     * @param key
     * @param value
     */
    private void saveTimeZonePrefs(String key, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        settings.edit().putString(key, value).apply();
    }

    /**
     * This function gets the time from the time zone that user have chosen.
     *
     * @param timezone
     */
    private void setSelectedText(TimeZone timezone) {
        mMiliSeconds = mMiliSeconds + timezone.getRawOffset();
        mTextClock.setTimeZone(timezone.getID());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        mAnalogClock.setTime(calendar);
        mAnalogClock.setTimezone(timezone);
        saveTimeZonePrefs(DEFAULT_TIME_ZONE_KEY, timezone.getID());
        mTextClock.setFormat12Hour("hh:mm:ss a"); //for 12 hour format
        mTextClock.setFormat24Hour("k:mm:ss"); // for 24 hour format
        mMiliSeconds = 0;
    }

}
