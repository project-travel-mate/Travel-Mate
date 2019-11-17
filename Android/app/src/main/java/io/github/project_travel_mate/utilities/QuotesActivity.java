package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.github.project_travel_mate.R;

public class QuotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        DailyQuotesFragment dailyQuotesFragment = new DailyQuotesFragment();

        // Get the FragmentManager and start a transaction.

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Add the SimpleFragment.
        fragmentTransaction.add(R.id.quote_framelayout,
                dailyQuotesFragment).commit();

    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, QuotesActivity.class);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
