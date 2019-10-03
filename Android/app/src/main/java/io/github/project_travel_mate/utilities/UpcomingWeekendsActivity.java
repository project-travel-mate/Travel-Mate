package io.github.project_travel_mate.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.UpcomingWeekends;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class UpcomingWeekendsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.upcoming_weekends_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.upcoming_weekends_activity)
    CoordinatorLayout mLayout;
    @BindView(R.id.upcoming_weekends_no_items)
    TextView mNoItems;
    @BindView(R.id.upcoming_weekends_main_layout)
    RelativeLayout mMainLayout;

    private String mToken;
    private Handler mHandler;
    private UpcomingWeekendsListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_weekends);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mAdapter = new UpcomingWeekendsListAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        getUpcomingLongWeekends();

        setTitle(R.string.upcoming_long_weekends);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, UpcomingWeekendsActivity.class);
    }

    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void getUpcomingLongWeekends() {
        String uri = API_LINK_V2 + "get-upcoming-holidays/" + Calendar.getInstance().get(Calendar.YEAR);

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
                mHandler.post(() -> networkError());
            }

            @Override
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        JSONArray arr;
                        try {
                            final String res = response.body().string();
                            Log.v("Response", res);
                            arr = new JSONArray(res);

                            if (arr.length() < 1) {
                                noResults();
                                mNoItems.setVisibility(View.VISIBLE);
                            } else {
                                mNoItems.setVisibility(View.GONE);
                                ArrayList<UpcomingWeekends> weekends = new ArrayList<>();
                                Date todayDate = new Date();
                                for (int i = 0; i < arr.length(); i++) {
                                    String month = arr.getJSONObject(i).getString("month");
                                    int date = arr.getJSONObject(i).getInt("date");

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.DAY_OF_MONTH, date);
                                    calendar.set(Calendar.MONTH, getMonthByString(month));

                                    String day = arr.getJSONObject(i).optString("day");
                                    String name = arr.getJSONObject(i).getString("name");
                                    String type = arr.getJSONObject(i).getString("type");

                                    // since it is upcoming weekends we need only these records
                                    // which are going to happen in future
                                    if (calendar.getTime().after(todayDate)) {
                                        weekends.add(new UpcomingWeekends(month, date, day, name, type));
                                    }
                                }
                                animationView.setVisibility(View.GONE);
                                mMainLayout.setVisibility(View.VISIBLE);
                                mAdapter.initData(weekends);
                            }
                        } catch (JSONException | IOException | NullPointerException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                            networkError();
                        }
                    } else {
                        networkError();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        mMainLayout.setVisibility(View.GONE);
        animationView.playAnimation();
    }

    private void noResults() {
        TravelmateSnackbars.createSnackBar(mLayout, R.string.upcoming_long_weekends_no_items,
                Snackbar.LENGTH_LONG).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    private int getMonthByString(String month) {
        switch (month) {
            case "January":
                return Calendar.JANUARY;
            case "February":
                return Calendar.FEBRUARY;
            case "March":
                return Calendar.MARCH;
            case "April":
                return Calendar.APRIL;
            case "May":
                return Calendar.MAY;
            case "June":
                return Calendar.JUNE;
            case "July":
                return Calendar.JULY;
            case "August":
                return Calendar.AUGUST;
            case "September":
                return Calendar.SEPTEMBER;
            case "October":
                return Calendar.OCTOBER;
            case "November":
                return Calendar.NOVEMBER;
            case "December":
                return Calendar.DECEMBER;
        }
        return -1;
    }

    @Override
    public void onRefresh() {
        getUpcomingLongWeekends();
    }

    @Override
    public void onResume() {
        getUpcomingLongWeekends();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
