package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.Objects;

import butterknife.ButterKnife;
import io.github.project_travel_mate.R;

import static utils.Constants.USER_ID;

public class ChecklistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userId = sharedPreferences.getString(USER_ID, null);

        Log.d(String.valueOf(Log.INFO), userId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = ChecklistFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.checklist_root_layout, fragment).commit();

        setTitle(getResources().getString(R.string.text_checklist));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChecklistActivity.class);
        return intent;
    }
}
