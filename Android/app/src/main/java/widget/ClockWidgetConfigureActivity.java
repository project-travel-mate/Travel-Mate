package widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.TimeZone;

import io.github.project_travel_mate.R;

/**
 * The configuration screen for the {@link ClockWidget ClockWidget} AppWidget.
 */
public class ClockWidgetConfigureActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String PREFS_NAME = "widget.ClockWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static String TIMEZONE_CODE = "America/Denver";
    private Context mContext;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    Spinner mTimeZoneSpinner;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = ClockWidgetConfigureActivity.this;
            // When the button is clicked, store the string locally
            mTimeZoneSpinner = (Spinner) findViewById(R.id.spinnerTimeZoneIDs);
            TIMEZONE_CODE = mTimeZoneSpinner.getSelectedItem().toString();
            saveTitlePref(context, mAppWidgetId, TIMEZONE_CODE);
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ClockWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public ClockWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return TIMEZONE_CODE;
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContext = this;
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.clock_widget_configure);
        //mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        //Setup Spinner for Timezone IDs
        String[] timezoneIDs = TimeZone.getAvailableIDs();
        mTimeZoneSpinner = (Spinner) findViewById(R.id.spinnerTimeZoneIDs);
        ArrayAdapter<String> mTZAdpter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timezoneIDs);
        mTZAdpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeZoneSpinner.setAdapter(mTZAdpter);
        mTimeZoneSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String mTZoneID = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), mTZoneID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

