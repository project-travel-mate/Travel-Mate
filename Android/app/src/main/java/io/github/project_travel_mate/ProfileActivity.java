package io.github.project_travel_mate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static utils.Constants.FIRST_NAME;
import static utils.Constants.USER_EMAIL;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.display_name)
    TextView displayName;
    @BindView(R.id.display_email)
    TextView emailId;
    @BindView(R.id.display_joining_date)
    TextView joiningDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString(USER_EMAIL, "--");
        emailId.setText(email);
        String name = preferences.getString(FIRST_NAME, "--");
        displayName.setText(name);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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