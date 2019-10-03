package io.github.project_travel_mate.friend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.FullScreenImage;
import io.github.project_travel_mate.R;
import objects.FriendCity;
import objects.Trip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static android.view.View.GONE;
import static com.google.android.flexbox.FlexDirection.ROW;
import static com.google.android.flexbox.JustifyContent.FLEX_START;
import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_FRIEND_ID;
import static utils.Constants.USER_TOKEN;
import static utils.DateUtils.getDate;
import static utils.DateUtils.rfc3339ToMills;


public class FriendsProfileActivity extends AppCompatActivity implements TravelmateSnackbars {

    @BindView(R.id.display_image)
    ImageView friendDisplayImage;
    @BindView(R.id.display_email)
    TextView friendsEmail;
    @BindView(R.id.display_name)
    TextView friendUserName;
    @BindView(R.id.display_joining_date)
    TextView friendJoiningDate;
    @BindView(R.id.display_status)
    TextView displayStatus;
    @BindView(R.id.status_icon)
    ImageView statusIcon;
    @BindView(R.id.profile_icon)
    ImageView profileIcon;
    @BindView(R.id.date_joined_icon)
    ImageView dateJoinedIcon;
    @BindView(R.id.email_icon)
    ImageView emailIcon;
    @BindView(R.id.trips_together_layout)
    RelativeLayout tripsTogetherLayout;
    @BindView(R.id.date_joined_layout)
    LinearLayout dateJoinedLayout;
    @BindView(R.id.display_mutual_trips)
    TextView mutualTripsText;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.friend_city_list_recycler)
    RecyclerView cityRecycler;

    private String mToken;
    private Handler mHandler;
    private String mFriendImageUri;
    private String mFriendName;
    private List<Trip> mTrips = new ArrayList<>();
    private RecyclerView.Adapter mMutualTripsAdapter;
    private FriendCityRecyclerAdapter mCityAdapter;
    private ArrayList<FriendCity> mFriendCityList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);
        ButterKnife.bind(this);

        mFriendCityList = new ArrayList<>();
        mCityAdapter = new FriendCityRecyclerAdapter(this, mFriendCityList);
        cityRecycler.setAdapter(mCityAdapter);
        cityRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Intent intent = getIntent();
        int mFriendId = (int) intent.getSerializableExtra(EXTRA_MESSAGE_FRIEND_ID);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        getFriendDetails(String.valueOf(mFriendId));
        getMutualTrips(String.valueOf(mFriendId));
        getVisitedCities(String.valueOf(mFriendId));
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //open friend's profile image in full screen
        friendDisplayImage.setOnClickListener(v -> {
            Intent fullScreenIntent = FullScreenImage.getStartIntent(FriendsProfileActivity.this,
                    mFriendImageUri, mFriendName);
            startActivity(fullScreenIntent);
        });
    }

    public static Intent getStartIntent(Context context, int id) {
        Intent intent = new Intent(context, FriendsProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE_FRIEND_ID, id);
        return intent;
    }

    private void getVisitedCities(final String friendId) {
        mFriendCityList.clear();
        String uri;
        if (friendId != null)
            uri = API_LINK_V2 + "get-visited-city/" + friendId;
        else
            uri = API_LINK_V2 + "get-visited-city";
        Log.d("FriendsProfileActivity", " executing getVisitedCities: " + uri);
        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                try {
                    JSONArray citiesArray = new JSONArray(res);
                    for (int i = 0; i < citiesArray.length(); i++) {
                        JSONObject city = citiesArray.getJSONObject(i);
                        int cityId = city.getInt("id");
                        String cityName = city.getString("city_name");
                        String cityNickname = city.getString("nickname");
                        int cityFactsCount = city.getInt("facts_count");
                        String image = city.getString("image");
                        mFriendCityList.add(new FriendCity(cityId, cityName, cityNickname, cityFactsCount, image));
                    }
                    mHandler.post(() -> mCityAdapter.notifyDataSetChanged());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFriendDetails(final String friendId) {

        friendDisplayImage.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();

        String uri;
        if (friendId != null)
            uri = API_LINK_V2 + "get-user/" + friendId;
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
                        Log.d(FriendsProfileActivity.class.getSimpleName(), "onResponse: " + res);
                        String userName = object.getString("username");
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");
                        mFriendName = firstName + " " + lastName;
                        mFriendImageUri = object.getString("image");
                        String dateJoined = object.getString("date_joined");
                        String status = object.getString("status");
                        Long dateTime = rfc3339ToMills(dateJoined);
                        String date = getDate(dateTime);
                        Picasso.with(FriendsProfileActivity.this).load(mFriendImageUri)
                                .placeholder(R.drawable.default_user_icon)
                                .error(R.drawable.default_user_icon).into(friendDisplayImage);
                        if (!status.equals("null")) {
                            displayStatus.setText(status);
                            friendUserName.setText(mFriendName);
                            friendJoiningDate.setText(String.format(getString(R.string.text_joining_date), date));
                            friendsEmail.setText(userName);
                        } else {
                            displayStatus.setText(mFriendName);
                            friendsEmail.setText(String.format(getString(R.string.text_joining_date), date));
                            friendUserName.setText(userName);
                            friendUserName.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                            friendUserName.setSingleLine(true);
                            statusIcon.setImageResource(R.drawable.ic_person_black_24dp);
                            profileIcon.setImageResource(R.drawable.ic_email_black_24dp);
                            emailIcon.setImageResource(R.drawable.ic_date_range_black_24dp);
                            dateJoinedLayout.setVisibility(GONE);
                        }

                        setTitle(mFriendName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR : ", "Message : " + e.getMessage());
                    }
                });
            }
        });
    }

    /**
     * Fetches list of all trips user has travelled with given friend
     */
    private void getMutualTrips(String friendId) {

        Handler handler = new Handler(Looper.getMainLooper());

        String uri = API_LINK_V2 + "get-common-trips/" + friendId;
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
                handler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {

                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONArray arr;
                        try {
                            final String res = response.body().string();
                            Log.v("Response", res);
                            arr = new JSONArray(res);

                            for (int i = 0; i < arr.length(); i++) {
                                String id = arr.getJSONObject(i).getString("id");
                                String start = arr.getJSONObject(i).getString("start_date_tx");
                                String end = arr.getJSONObject(i).optString("end_date", null);
                                String name = arr.getJSONObject(i).getJSONObject("city").getString("city_name");
                                String tname = arr.getJSONObject(i).getString("trip_name");
                                String image = arr.getJSONObject(i).getJSONObject("city").getString("image");
                                boolean isPublic = arr.getJSONObject(i).getBoolean("is_public");
                                mTrips.add(new Trip(id, name, image, start, end, tname, isPublic));
                            }
                            //display trips only if there exists at least one trip
                            //else hide the view
                            if (!mTrips.isEmpty()) {
                                // Specify a layout for RecyclerView
                                // Create a horizontal RecyclerView
                                FlexboxLayoutManager layoutManager =
                                        new FlexboxLayoutManager(FriendsProfileActivity.this);
                                layoutManager.setFlexDirection(ROW);
                                layoutManager.setJustifyContent(FLEX_START);
                                recyclerView.setLayoutManager(layoutManager);
                                mMutualTripsAdapter =
                                        new MutualTripsAdapter(FriendsProfileActivity.this, mTrips);
                                recyclerView.setAdapter(mMutualTripsAdapter);
                            } else {
                                mutualTripsText.setVisibility(GONE);
                            }
                            animationView.setVisibility(View.GONE);
                            friendDisplayImage.setVisibility(View.VISIBLE);
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // return back to trip activity
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

}