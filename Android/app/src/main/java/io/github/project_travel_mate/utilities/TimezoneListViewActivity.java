package io.github.project_travel_mate.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.github.project_travel_mate.R;
import objects.ZoneName;

public class TimezoneListViewActivity extends Activity implements TextWatcher {

    @BindView(R.id.listView)
    RecyclerView mListview;
    @BindView(R.id.timezoneSearch)
    EditText mTimezoneSearch;
    CurrencyConverterAdapter mAdaptorListView;

    public static ArrayList<ZoneName> timezone_names;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timezone_listview);

        timezone_names = new ArrayList<>();
        mListview = findViewById(R.id.listView);
        mTimezoneSearch = findViewById(R.id.timezoneSearch);

        mContext = this;
        addTimezones();

        mTimezoneSearch.addTextChangedListener(this);
    }

    /**
     * add times to list adapter
     */
    public void addTimezones() {
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                for (String id : android.icu.util.TimeZone.getAvailableIDs(countryCode)) {
                    // Add timezone to result map
                    timezone_names.add(new ZoneName(displayTimeZone(TimeZone.getTimeZone(id)), getFlagId(countryCode)));
                }
            }

        }

        Collections.sort(timezone_names, (n1, n2) -> n1.shortName.compareTo(n2.shortName));

        mAdaptorListView = new CurrencyConverterAdapter(TimezoneListViewActivity.this, timezone_names);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        mListview.setLayoutManager(mLayoutManager);
        mListview.setAdapter(mAdaptorListView);
    }

    /**
     * @param tz - timexome to be displayed
     * @return - formatted timezone value
     */
    private static String displayTimeZone(TimeZone tz) {
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);
        // avoid -4:-30 issue
        minutes = Math.abs(minutes);

        String result;
        if (hours > 0) {
            result = tz.getID() + " " + String.format("(GMT+%d:%02d)", hours, minutes);
        } else {
            result = tz.getID() + " " + String.format("(GMT%d:%02d)", hours, minutes);
        }

        return result;
    }

    /**
     * @param countryCode - country code to get symbol for
     * @return - country symbol
     */
    private String getFlagId(String countryCode) {
        String currencySymbol = "";
        Locale locale;
        Currency currency = null;
        try {
            locale = new Locale("", countryCode);
            currency = Currency.getInstance(locale);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (currency != null) {
            currencySymbol = currency.getCurrencyCode();
        }

        return currencySymbol;
    }

    @Override
    public void beforeTextChanged(CharSequence searchItem, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence searchItem, int start, int before, int count) {
        TimezoneListViewActivity.this.mAdaptorListView.getFilter().filter(searchItem);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

