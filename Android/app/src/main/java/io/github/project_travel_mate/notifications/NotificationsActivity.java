package io.github.project_travel_mate.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.Notification;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class NotificationsActivity extends AppCompatActivity {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.notification_list)
    ListView listView;

    private String mToken;
    private Handler mHandler;
    private SharedPreferences mSharedPreferences;
    ArrayList<Notification> notifications;
    private NotificationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        notifications = new ArrayList<>();

        getNotifications();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getNotifications() {

        String uri = API_LINK_V2 + "get-notifications";
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
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        try {
                            JSONArray array = new JSONArray(res);

                            if (array.length() == 0) {
                                emptyList();
                            } else {

                                notifications.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    int id = array.getJSONObject(i).getInt("id");
                                    String type = array.getJSONObject(i).getString("notification_type");
                                    String text = array.getJSONObject(i).getString("text");
                                    boolean read = array.getJSONObject(i).getBoolean("is_read");

                                    JSONObject object = array.getJSONObject(i).getJSONObject("initiator_user");
                                    String userName = object.getString("username");
                                    String firstName = object.getString("first_name");
                                    String lastName = object.getString("last_name");
                                    int ids = object.getInt("id");
                                    String imageURL = object.getString("image");
                                    String dateJoined = object.getString("date_joined");
                                    String status = object.getString("status");
                                    User user =
                                            new User(userName, firstName, lastName, ids, imageURL, dateJoined, status);

                                    notifications.add(new Notification(id, type, text, read, user));
                                }
                                mAdapter = new NotificationsAdapter(NotificationsActivity.this, notifications);
                                listView.setAdapter(mAdapter);
                                animationView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", "Message : " + e.getMessage());
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                });
            }
        });
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NotificationsActivity.class);
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

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    private void emptyList() {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.content),
                        R.string.no_notifications, Snackbar.LENGTH_LONG);
        snackbar.show();
        animationView.setAnimation(R.raw.no_notifications);
        animationView.playAnimation();
    }
}
