package io.github.project_travel_mate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.OTHER_USER_ID;
import static utils.Constants.SHARE_PROFILE_URI;
import static utils.Constants.SHARE_PROFILE_USER_ID_QUERY;
import static utils.Constants.STATUS_CODE_OK;
import static utils.Constants.USER_DATE_JOINED;
import static utils.Constants.USER_EMAIL;
import static utils.Constants.USER_ID;
import static utils.Constants.USER_IMAGE;
import static utils.Constants.USER_NAME;
import static utils.Constants.USER_TOKEN;
import static utils.DateUtils.getDate;
import static utils.DateUtils.rfc3339ToMills;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.display_image)
    ImageView displayImage;
    @BindView(R.id.display_name)
    EditText displayName;
    @BindView(R.id.display_email)
    TextView emailId;
    @BindView(R.id.display_joining_date)
    TextView joiningDate;
    @BindView(R.id.ib_edit_display_name)
    ImageButton editDisplayName;
    private String mToken;
    private Handler mHandler;
    private MaterialDialog mDialog;
    // Flag for checking the current drawable of the ImageButton
    private boolean mFlagForDrawable = true;
    private SharedPreferences mSharedPreferences;
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        Intent intent = getIntent();
        String id = intent.getStringExtra(OTHER_USER_ID);
        getUserDetails(id);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (id == null) {
            fillProfileInfo(mSharedPreferences.getString(USER_NAME, null),
                    mSharedPreferences.getString(USER_EMAIL, null),
                    mSharedPreferences.getString(USER_IMAGE, null),
                    mSharedPreferences.getString(USER_DATE_JOINED, null));

        } else {
            editDisplayName.setVisibility(View.INVISIBLE);
            updateOptionsMenu();
        }

        editDisplayName.setOnClickListener(v -> {
            if (mFlagForDrawable) {
                mFlagForDrawable = false;
                editDisplayName.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
                displayName.setFocusableInTouchMode(true);
                displayName.setCursorVisible(true);
                displayName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).showSoftInput(displayName, InputMethodManager.SHOW_IMPLICIT);
            } else {
                mFlagForDrawable = true;
                editDisplayName.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_black_24dp));
                displayName.setFocusableInTouchMode(false);
                displayName.setCursorVisible(false);
                setUserDetails();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if ( view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.action_share_profile:
                shareProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getUserDetails(final String userId) {

        String uri;
        if (userId != null)
            uri = API_LINK_V2 + "get-user/" + userId;
        else
            uri = API_LINK_V2 + "get-user";
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
                        Long dateTime = rfc3339ToMills(dateJoined);
                        String date = getDate(dateTime);

                        fillProfileInfo(fullName, userName, imageURL, date);

                        if (userId == null) {
                            mSharedPreferences.edit().putString(USER_NAME, fullName).apply();
                            mSharedPreferences.edit().putString(USER_EMAIL, userName).apply();
                            mSharedPreferences.edit().putString(USER_DATE_JOINED, date).apply();
                            mSharedPreferences.edit().putString(USER_IMAGE, imageURL).apply();
                            mSharedPreferences.edit().putString(USER_ID, String.valueOf(id)).apply();
                        } else {
                            updateOptionsMenu();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR : ", "Message : " + e.getMessage());
                    }
                });
            }
        });
    }

    private void setUserDetails() {

        mDialog = new MaterialDialog.Builder(ProfileActivity.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        // to update user name
        String uri = API_LINK_V2 + "update-user-details";
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();

        // Add form parameters
        String fullName = String.valueOf(displayName.getText());
        String firstName = fullName.substring(0, fullName.indexOf(' '));
        String lastName = fullName.substring(fullName.indexOf(' ') + 1);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("firstname", firstName)
                .addFormDataPart("lastname", lastName)
                .build();

        // Create a http request object.
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .post(requestBody)
                .build();

        // Create a new Call object with post method.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                final int responseCode = response.code();
                mHandler.post(() -> {
                    if (responseCode == STATUS_CODE_OK) {
                        TravelmateSnackbars.createSnackBar(findViewById(R.id.activity_profile_id),
                                R.string.name_updated, Snackbar.LENGTH_SHORT).show();
                        mSharedPreferences.edit().putString(USER_NAME, fullName).apply();

                    } else {
                        displaySnackbar(res);
                    }
                });
                mDialog.dismiss();
            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    public static Intent getStartIntent(Context context, String userId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(OTHER_USER_ID, userId);
        return intent;
    }

    private void fillProfileInfo(String fullName, String email, String imageURL, String dateJoined) {
        displayName.setText(fullName);
        emailId.setText(email);
        joiningDate.setText(getString(R.string.text_joining_date) + " " + dateJoined);
        Picasso.with(ProfileActivity.this).load(imageURL).placeholder(R.drawable.default_user_icon)
                .error(R.drawable.default_user_icon).into(displayImage);
        setTitle(fullName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    private void updateOptionsMenu() {
        if (mOptionsMenu != null) {
            MenuItem item = mOptionsMenu.findItem(R.id.action_share_profile);
            item.setVisible(false);
        }
    }

    private void shareProfile() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        Uri profileURI = Uri.parse(SHARE_PROFILE_URI)
                .buildUpon()
                .appendQueryParameter(SHARE_PROFILE_USER_ID_QUERY, mSharedPreferences.getString(USER_ID, null))
                .build();

        Log.v("profile url", profileURI + "");

        intent.putExtra(Intent.EXTRA_TEXT , getResources().getString(R.string.share_profile_text) + " " + profileURI );
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.share_chooser)));
        } catch (android.content.ActivityNotFoundException ex) {
            Snackbar.make(Objects.requireNonNull(ProfileActivity.this).findViewById(android.R.id.content),
                    R.string.snackbar_no_share_app,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void displaySnackbar(final String message) {

        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.activity_profile_id),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }
}