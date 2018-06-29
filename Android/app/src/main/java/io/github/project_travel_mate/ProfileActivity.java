package io.github.project_travel_mate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;
import static utils.DateUtils.getDate;
import static utils.DateUtils.rfc3339ToMills;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.display_image)
    ImageView displayImage;
    @BindView(R.id.display_name)
    TextView displayName;
    @BindView(R.id.display_email)
    TextView emailId;
    @BindView(R.id.display_joining_date)
    TextView joiningDate;
    private String mToken;
    private Handler mHandler;
    private MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = preferences.getString(USER_TOKEN, null);
        getUserDetails();
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

    private void getUserDetails() {

        mDialog = new MaterialDialog.Builder(ProfileActivity.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        // to fetch city names
        String uri = API_LINK_V2 + "get-user";
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {
                    try {
                        JSONObject object = new JSONObject(res);
                        String userName = object.getString("username");
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");
                        int id = object.getInt("id");
                        String imageURL = object.getString("image");
                        String dateJoined = object.getString("date_joined");
                        new User(userName, firstName, lastName, id, imageURL, dateJoined);

                        String fullName = firstName + " " + lastName;
                        displayName.setText(fullName);
                        emailId.setText(userName);
                        Long dateTime = rfc3339ToMills(dateJoined);
                        String date = getDate(dateTime);
                        joiningDate.setText(getString(R.string.text_joining_date) + " " + date);
                        Picasso.with(ProfileActivity.this).load(imageURL).placeholder(R.drawable.default_user_icon)
                                .error(R.drawable.default_user_icon).into(displayImage);

                        mDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR : ", "Message : " + e.getMessage());
                    }
                });
            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }
}