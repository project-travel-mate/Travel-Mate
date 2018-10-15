package io.github.project_travel_mate.mytrips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import objects.Trip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static android.app.Activity.RESULT_OK;
import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class MyTripsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, TravelmateSnackbars {

    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    private String mToken;
    private Handler mHandler;
    private Activity mActivity;
    private TripsListAdapter mMyTripsAdapter;
    static int ADDNEWTRIP_ACTIVITY = 203;
    private View mTripsView;

    private List<Trip> mTripList = new ArrayList<>();


    public MyTripsFragment() {
        // Required empty public constructor
    }
    public static MyTripsFragment newInstance() {
        return new MyTripsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTripsView = inflater.inflate(R.layout.fragment_my_trips, container, false);
        ButterKnife.bind(this, mTripsView);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mMyTripsAdapter = new TripsListAdapter(mTripList);
        mMyTripsAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = MyTripInfoActivity.getStartIntent(mActivity.getApplicationContext(),
                    mTripList.get(position));
            mActivity.getApplicationContext().startActivity(intent);
        });
        mRecyclerView.setAdapter(mMyTripsAdapter);
        mytrip();
        return mTripsView;

    }

    private LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
    }

    private void mytrip() {

        String uri = API_LINK_V2 + "get-all-trips";

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
                    swipeRefreshLayout.setRefreshing(false);
                    if (response.isSuccessful() && response.body() != null) {
                        JSONArray arr;
                        try {
                            final String res = response.body().string();
                            Log.v("Response", res);
                            arr = new JSONArray(res);

                            if (arr.length() < 1) {
                                noResults();
                                return;
                            }

                            for (int i = arr.length() - 1; i >= 0; i--) {
                                String id = arr.getJSONObject(i).getString("id");
                                String start = arr.getJSONObject(i).getString("start_date_tx");
                                String end = arr.getJSONObject(i).optString("end_date", null);
                                String name = arr.getJSONObject(i).getJSONObject("city").getString("city_name");
                                String tname = arr.getJSONObject(i).getString("trip_name");
                                String image = arr.getJSONObject(i).getJSONObject("city").getString("image");
                                mTripList.add(new Trip(id, name, image, start, end, tname));
                                animationView.setVisibility(View.GONE);
                            }
                            mMyTripsAdapter.notifyItemRangeInserted(0, arr.length());

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

    @OnClick(R.id.add_trip)
    void addTrip() {
        Intent intent = new Intent(getContext() , AddNewTripActivity.class);
        startActivityForResult(intent , ADDNEWTRIP_ACTIVITY);

    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    /**
     * Plays the no results animation in the view
     */
    private void noResults() {
        TravelmateSnackbars.createSnackBar(mTripsView.findViewById(R.id.my_trips_frag), R.string.no_trips,
                Snackbar.LENGTH_LONG).show();
        animationView.setAnimation(R.raw.empty_list);
        animationView.playAnimation();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDNEWTRIP_ACTIVITY && resultCode == RESULT_OK) {
            if (mMyTripsAdapter != null) {
                mMyTripsAdapter.notifyDataSetChanged();
            }
            mytrip();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        mytrip();
        swipeRefreshLayout.setRefreshing(true);
    }
}
