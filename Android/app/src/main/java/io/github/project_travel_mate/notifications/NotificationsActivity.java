package io.github.project_travel_mate.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import objects.Trip;
import objects.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class NotificationsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        TravelmateSnackbars {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.notification_list)
    ListView listView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private String mToken;
    private Handler mHandler;
    ArrayList<Notification> notifications;
    private NotificationsAdapter mAdapter;
    private MaterialDialog mDialog;
    private Menu mOptionsMenu;
    boolean allRead = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        notifications = new ArrayList<>();
        swipeRefreshLayout.setOnRefreshListener(this);
        getNotifications();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateOptionsMenu();
    }

    private void getNotifications() {
        allRead = false;
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
                            Log.v("Response", res + " ");

                            if (array.length() == 0) {
                                emptyList();
                            } else {

                                notifications.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    int id = array.getJSONObject(i).getInt("id");
                                    String type = array.getJSONObject(i).getString("notification_type");
                                    String text = array.getJSONObject(i).getString("text");
                                    boolean read = array.getJSONObject(i).getBoolean("is_read");
                                    if (!read) {
                                        allRead = true;
                                    }
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
                                    if (!Objects.equals(array.getJSONObject(i).getString("trip"), "null") &&
                                            array.getJSONObject(i).getString("notification_type").equals("Trip")) {

                                        JSONObject obj = array.getJSONObject(i).getJSONObject("trip");
                                        String tripId = obj.getString("id");
                                        JSONObject subObject = obj.getJSONObject("city");
                                        String name = subObject.getString("city_name");
                                        String image = subObject.getString("images");
                                        String start = obj.getString("start_date_tx");
                                        String tname = obj.getString("trip_name");
                                        Trip trip = new Trip(tripId, name, image, start, "", tname);
                                        notifications.add(new Notification(id, type, text, read, user, trip));
                                    } else {
                                        Trip trip = new Trip("", "", "", "", "", "");
                                        notifications.add(new Notification(id, type, text, read, user, trip));
                                    }
                                }
                                mAdapter = new NotificationsAdapter(NotificationsActivity.this, notifications);
                                listView.setAdapter(mAdapter);
                                if (!allRead) {
                                    MenuItem item = mOptionsMenu.findItem(R.id.action_sort);
                                    item.setVisible(false);
                                }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_notification_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    private void updateOptionsMenu() {
        if (mOptionsMenu != null) {
            MenuItem item = mOptionsMenu.findItem(R.id.action_sort);
            item.setVisible(false);
        }
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
            case R.id.action_sort:
                markAllAsRead();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void markAllAsRead() {
        //set AlertDialog before marking All as read
        ContextThemeWrapper crt = new ContextThemeWrapper(this, R.style.AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(crt);
        builder.setMessage(R.string.mark_all_read_notifications)
                .setPositiveButton(R.string.positive_button,
                        (dialog, which) -> {
                            mDialog = new MaterialDialog.Builder(NotificationsActivity.this)
                                    .title(R.string.app_name)
                                    .content(R.string.progress_wait)
                                    .progress(true, 0)
                                    .show();

                            String uri;
                            uri = API_LINK_V2 + "mark-all-notification";
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
                                        if (response.isSuccessful()) {
                                            getNotifications();
                                            TravelmateSnackbars.createSnackBar(findViewById
                                                            (R.id.notifications_id_layout), res,
                                                    Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            TravelmateSnackbars.createSnackBar(findViewById
                                                            (R.id.notifications_id_layout), res,
                                                    Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                                    mDialog.dismiss();
                                    MenuItem item = mOptionsMenu.findItem(R.id.action_sort);
                                    item.setVisible(false);
                                }
                            });

                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {

                        });
        builder.create().show();
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
                .make(findViewById(R.id.notifications_id_layout),
                        R.string.no_notifications, Snackbar.LENGTH_LONG);
        snackbar.show();

        animationView.setAnimation(R.raw.no_notifications);
        animationView.playAnimation();
    }

    @Override
    public void onRefresh() {
        listView.setAdapter(null);
        swipeRefreshLayout.setRefreshing(false);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        getNotifications();
    }
    @Override
    public void onResume() {
        super.onResume();
        getNotifications();
    }
}
