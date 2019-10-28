package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextClock;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import adapters.TimezoneAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.utilities.helper.KeyboardHelper;

public class WorldClockActivity extends AppCompatActivity {

    @BindView(R.id.clock_analog)
    CustomAnalogClock mAnalogClock;

    @BindView(R.id.clock_digital)
    TextClock mTextClock;

    @BindView(R.id.actvTimezone)
    AutoCompleteTextView mAutoCompleteTextViewTimezone;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, WorldClockActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_world_clock);
        ButterKnife.bind(this);
        setTitle(R.string.text_clock);

        // AutoCompleteTextView initialization
        TimezoneAdapter mAdapter = new TimezoneAdapter(this, Arrays.asList(TimeZone.getAvailableIDs()));
        this.mAutoCompleteTextViewTimezone.setAdapter(mAdapter);
        this.mAutoCompleteTextViewTimezone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorldClockActivity.this.onItemAutocompleteItemSelected();
            }
        });

        // Analog Clock Initialization
        mAnalogClock.init(WorldClockActivity.this, R.drawable.clock_face,
                R.drawable.hours_hand, R.drawable.minutes_hand,
                0, false, false);

        mAnalogClock.setScale(1f);
        // analog clock size

        // Initialize data with default timezone
        this.setSelectedText(TimeZone.getDefault());


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function gets the time from the time zone that user have chosen.
     *
     * @param timezone
     */
    private void setSelectedText(TimeZone timezone) {
        this.mTextClock.setTimeZone(timezone.getID());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -2);
        this.mAnalogClock.setTime(calendar);
        this.mAnalogClock.setTimezone(timezone);
    }

    /**
     * Method triggered when the user selects
     * a timezone from the list
     */
    private void onItemAutocompleteItemSelected() {
        KeyboardHelper.Companion.hideKeyboard(this);
        this.setSelectedText(TimeZone.getTimeZone(this.mAutoCompleteTextViewTimezone.getText().toString()));
    }

}
