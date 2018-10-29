package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import io.github.project_travel_mate.R;
import io.github.project_travel_mate.utilities.DailyQuotesFragment;

public class DailyQuotesManager {

    private DailyQuotesManager() {
        // not called
    }

    public static void checkDailyQuote(Context mContext) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (prefs.getBoolean(Constants.QUOTES_SHOW_DAILY, true) != true) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launchCount = prefs.getLong(Constants.QUOTES_LAUNCH_COUNT, 0) + 1;
        editor.putLong(Constants.QUOTES_LAUNCH_COUNT, launchCount);

        // Get date of first launch
        Long dateFirstLaunched = prefs.getLong(Constants.QUOTES_FIRST_LAUNCH_DATE, 0);
        if (dateFirstLaunched == 0) {
            dateFirstLaunched = System.currentTimeMillis();
            editor.putLong(Constants.QUOTES_FIRST_LAUNCH_DATE, dateFirstLaunched);

            if (Constants.QUOTES_SHOW_ON_FIRST_LOAD) {
                showDailyQuote(mContext);
            }
        }

        // Wait at least n days before opening
        if (launchCount >= Constants.DAYS_UNTIL_SHOW_QUOTE) {
            if (System.currentTimeMillis() >= dateFirstLaunched +
                    (Constants.DAYS_UNTIL_SHOW_QUOTE * 24 * 60 * 60 * 1000)) {
                showDailyQuote(mContext);
            }
        }

        editor.apply();
    }

    private static void showDailyQuote(final Context mContext) {
        DailyQuotesFragment dailyQuotesFragment = new DailyQuotesFragment();

        // Get the FragmentManager and start a transaction.

        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.quote_framelayout,
                dailyQuotesFragment).addToBackStack(null).commit();
    }

    public static void dontShowQuotes(Context mContext) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.QUOTES_SHOW_DAILY, false);
        editor.apply();
    }
}
