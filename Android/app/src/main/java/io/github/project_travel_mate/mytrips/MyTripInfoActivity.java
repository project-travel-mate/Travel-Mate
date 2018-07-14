package io.github.project_travel_mate.mytrips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import adapters.NestedListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.description.FinalCityInfoActivity;
import objects.City;
import objects.Trip;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_FRIEND_ID;
import static utils.Constants.EXTRA_MESSAGE_TRIP_OBJECT;
import static utils.Constants.USER_TOKEN;

public class MyTripInfoActivity extends AppCompatActivity {

    public static final int INTENT_REQUEST_GET_IMAGES = 13;
    @BindView(R.id.city_image)
    ImageView cityImageView;
    @BindView(R.id.city_name)
    TextView cityName;
    @BindView(R.id.trip_start_date)
    TextView tripDate;
    @BindView(R.id.add_new_friend)
    FlatButton addNewFriend;
    @BindView(R.id.friend_list)
    NestedListView listView;
    @BindView(R.id.friend_email)
    AutoCompleteTextView friendEmail;
    @BindView(R.id.trip_name)
    EditText tripName;
    @BindView(R.id.friend_title)
    TextView friendTitle;
    @BindView(R.id.plus_icon)
    ImageView showIcon;
    @BindView(R.id.edit_trip_icon)
    ImageView editTrip;
    @BindView(R.id.know_more)
    Button details;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String mFriendId = null;
    private String mNameYet;
    private String mToken;
    private Trip mTrip;
    private Handler mHandler;
    private boolean mIsClicked = false;
    private boolean mIsTripNameEdited = false;
    private MaterialDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_info);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mTrip = (Trip) intent.getSerializableExtra(EXTRA_MESSAGE_TRIP_OBJECT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        if (!mTrip.getImage().isEmpty())
            Picasso.with(this).load(mTrip.getImage()).error(R.drawable.placeholder_image)
               .placeholder(R.drawable.placeholder_image).into(cityImageView);
        showIcon.setVisibility(View.GONE);
        editTrip.setVisibility(View.GONE);
        mHandler = new Handler(Looper.getMainLooper());
        friendEmail.clearFocus();
        friendEmail.setThreshold(1);

        getSingleTrip();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsTripNameEdited) {
                    //if edit trip is clicked before editing the name
                    editTrip.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_black_24dp));
                    tripName.setFocusableInTouchMode(true);
                    tripName.setCursorVisible(true);
                    tripName.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(inputMethodManager)
                            .showSoftInput(tripName, InputMethodManager.SHOW_IMPLICIT);
                    mIsTripNameEdited = true;
                } else {
                    //clicking edit trip after editing the trip name
                    if (tripName.getText().length() != 0 ) {
                        editTrip.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit_black_24dp));
                        tripName.setFocusableInTouchMode(false);
                        tripName.setCursorVisible(false);
                        mIsTripNameEdited = false;
                        updateTripName();
                    } else {
                        Toast.makeText(MyTripInfoActivity.this, R.string.cannot_edit, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @OnTextChanged(R.id.friend_email)
    void onTextChanged() {
        mNameYet = friendEmail.getText().toString();
        if (!mNameYet.contains(" ") && mNameYet.length() % 3 == 0) {
            friendAutoComplete();
        }
    }

    @OnClick(R.id.add_new_friend)
    void onClick() {
        if (mFriendid == null) {
            displaySnackbar(getString(R.string.no_friend_selected),
                    Snackbar.LENGTH_LONG);

        } else {
            addFriend();
        }
    }

    private void getSingleTrip() {

        progressBar.setVisibility(View.VISIBLE);

        // to fetch mCity names
        String uri = API_LINK_V2 + "get-trip/" + mTrip.getId();

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
                    JSONObject ob;
                    try {
                        ob = new JSONObject(res);
                        String title = ob.getString("trip_name");
                        String start = ob.getString("start_date_tx");
                        String end = ob.optString("end_date", null);
                        String city = ob.getJSONObject("city").getString("city_name");
                        details.setVisibility(View.VISIBLE);
                        details.setText(String.format(getString(R.string.know_more_about), city));
                        details.setOnClickListener(view -> getCity(city));
                        cityName.setText(city);
                        tripName.setText(title);
                        final Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(start) * 1000);
                        final String timeString =
                                getResources().getString(R.string.text_started_on) +
                                        new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
                        tripDate.setText(timeString);
                        editTrip.setVisibility(View.VISIBLE);
                        updateFriendList();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    tripName.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    mFriendId = null;
                });
            }
        });
    }
    private void getCity(String cityname) {
        // to fetch city names
        String uri = API_LINK_V2 + "get-city-by-name/" + cityname;
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
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    JSONArray arr;
                    City city = null;
                    try {
                        arr = new JSONArray(Objects.requireNonNull(response.body()).string());
                        Log.v("RESPONSE : ", arr.toString());

                        try {
                            city = new City(arr.getJSONObject(0).getString("id"),
                                    arr.getJSONObject(0).getString("image"),
                                    arr.getJSONObject(0).getString("city_name"),
                                    arr.getJSONObject(0).getInt("facts_count"),
                                    "Know More", "View on Map", "Fun Facts", "City Trends");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = FinalCityInfoActivity.getStartIntent(MyTripInfoActivity.this, city);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {
            ArrayList<Uri> imageUris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            displaySnackbar(getString(R.string.images_added), Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void friendAutoComplete() {

        if (mNameYet.trim().equals(""))
            return;

        String uri = API_LINK_V2 + "get-user/" + mNameYet.trim();
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
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
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    JSONArray arr;
                    final ArrayList<String> id, email;
                    try {
                        String result = response.body().string();
                        Log.e("RES", result);
                        if (response.body() == null)
                            return;
                        arr = new JSONArray(result);

                        id = new ArrayList<>();
                        email = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                id.add(arr.getJSONObject(i).getString("id"));
                                email.add(arr.getJSONObject(i).getString("username"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ERROR ", "Message : " + e.getMessage());
                            }
                        }
                        ArrayAdapter<String> dataAdapter =
                                new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, email);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        friendEmail.setAdapter(dataAdapter);
                        friendEmail.setOnItemClickListener((arg0, arg1, arg2, arg3) -> mFriendId = id.get(arg2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }

    private void addFriend() {

        final MaterialDialog mDialog = new MaterialDialog.Builder(MyTripInfoActivity.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        String uri = API_LINK_V2 + "add-friend-to-trip/" + mTrip.getId() + "/" + mFriendId;

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
            public void onResponse(Call call, final Response response) {
                try {
                    final String res = response.body().string();
                    mHandler.post(() -> {

                        if (response.isSuccessful()) {
                            displaySnackbar(getString(R.string.friend_added), Snackbar.LENGTH_LONG);
                            updateFriendList();
                            friendEmail.setText(null);
                        } else {
                            displaySnackbar(res, Snackbar.LENGTH_LONG);
                        }
                        mDialog.dismiss();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateFriendList() {
        List<User> tripFriends = new ArrayList<>();
        String uri = API_LINK_V2 + "get-trip/" + mTrip.getId();

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
                    JSONObject ob;
                    try {
                        ob = new JSONObject(res);
                        JSONArray usersArray = ob.getJSONArray("users");
                        if (usersArray.length() == 0) {
                            addNewFriend.setVisibility(View.GONE);
                            friendEmail.setVisibility(View.GONE);
                            friendTitle.setVisibility(View.VISIBLE);
                            friendTitle.setTypeface(null, Typeface.NORMAL);
                            String mystring = getString(R.string.friends_title);
                            SpannableString content = new SpannableString(mystring);
                            content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
                            friendTitle.setText(content);
                            friendTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f);

                            friendTitle.setOnClickListener(view -> {
                                addNewFriend.setVisibility(View.VISIBLE);
                                friendEmail.setVisibility(View.VISIBLE);
                                friendTitle.setText(R.string.friends_show_title);
                                friendTitle.setTypeface(null, Typeface.BOLD);
                                friendTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
                            });
                        } else {
                            friendTitle.setText(R.string.friends_show_title);
                            friendTitle.setVisibility(View.VISIBLE);
                            showIcon.setVisibility(View.VISIBLE);
                            addNewFriend.setVisibility(View.VISIBLE);
                            friendEmail.setVisibility(View.VISIBLE);
                            for (int i = 0; i < usersArray.length(); i++) {

                                JSONObject jsonObject = usersArray.getJSONObject(i);
                                String friendFirstName = jsonObject.getString("first_name");
                                String friendLastName = jsonObject.getString("last_name");
                                String friendImage = jsonObject.getString("image");
                                String friendJoinedOn = jsonObject.getString("date_joined");
                                String friendUserName = jsonObject.getString("username");
                                String friendStatus = jsonObject.getString("status");
                                int friendId = jsonObject.getInt("id");
                                tripFriends.add(new User(friendUserName, friendFirstName, friendLastName, friendId,
                                        friendImage, friendJoinedOn, friendStatus));
                            }
                            showIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MyTripFriendNameAdapter dataAdapter = new MyTripFriendNameAdapter(
                                            MyTripInfoActivity.this, tripFriends);
                                    if (!mIsClicked) {
                                        listView.setAdapter(dataAdapter);
                                        showIcon.setImageResource(R.drawable.ic_remove_circle_black_24dp);
                                        mIsClicked = true;
                                    } else {
                                        listView.setAdapter(null);
                                        showIcon.setImageResource(R.drawable.ic_add_circle_black_24dp);
                                        mIsClicked = false;
                                    }
                                }
                            });
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(MyTripInfoActivity.this, FriendsProfileActivity.class);
                                    intent.putExtra(EXTRA_MESSAGE_FRIEND_ID, tripFriends.get(position).getId());
                                    intent.putExtra(EXTRA_MESSAGE_TRIP_OBJECT, mTrip);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    private void updateTripName() {

        mDialog = new MaterialDialog.Builder(MyTripInfoActivity.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        String editedTripName = String.valueOf(tripName.getText());
        String uri = API_LINK_V2 + "update-trip-name/" + mTrip.getId() + "/" + editedTripName;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(MyTripInfoActivity.this, R.string.trip_name_updated, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MyTripInfoActivity.this, res, Toast.LENGTH_LONG).show();
                    }
                });
                mDialog.dismiss();
            }
        });
    }

    public static Intent getStartIntent(Context context, Trip trip) {
        Intent intent = new Intent(context, MyTripInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE_TRIP_OBJECT, trip);
        return intent;
    }

    private void displaySnackbar(final String message, int length) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.activityMyTripInfo),
                message, length);
        snackbar.show();
    }
}