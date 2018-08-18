package io.github.project_travel_mate.friend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
import objects.Trip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static android.view.View.GONE;
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
    RelativeLayout dateJoinedLayout;
    @BindView(R.id.display_mutual_trips)
    TextView mutualTripsText;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String mToken;
    private Handler mHandler;
    private String mFriendImageUri;
    private String mFriendName;
    private List<Trip> mTrips = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mMutualTripsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        int mFriendId = (int) intent.getSerializableExtra(EXTRA_MESSAGE_FRIEND_ID);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        getFriendDetails(String.valueOf(mFriendId));
        getMutualTrips(String.valueOf(mFriendId));
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
                            emailIcon.setImageResource(R.drawable.baseline_date_range_black);
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
            public void onResponse(Call call, final Response response) throws IOException {

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
                                mTrips.add(new Trip(id, name, image, start, end, tname));
                            }
                            //display trips only if there exists at least one trip
                            //else hide the view
                            if (!mTrips.isEmpty()) {
                                // Specify a layout for RecyclerView
                                // Create a horizontal RecyclerView
                                mLayoutManager = new LinearLayoutManager(FriendsProfileActivity.this,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                );
                                recyclerView.setLayoutManager(mLayoutManager);
                                mMutualTripsAdapter = new MutualTripsAdapter(FriendsProfileActivity.this, mTrips);
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