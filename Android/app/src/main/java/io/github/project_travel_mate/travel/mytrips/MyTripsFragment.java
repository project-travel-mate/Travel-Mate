package io.github.project_travel_mate.travel.mytrips;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.travel.TravelFragment;
import objects.Trip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_TOKEN;

public class MyTripsFragment extends Fragment {
    private final List<Trip> mTrips = new ArrayList<>();
    @BindView(R.id.gv)
    GridView gridView;
    private MaterialDialog mDialog;
    private String mToken;
    private Handler mHandler;

    public MyTripsFragment() {
        // Required empty public constructor
    }
    public static MyTripsFragment newInstance() {
        MyTripsFragment fragment = new MyTripsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mToken = sharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());

        mTrips.add(new Trip());

        mytrip();
        return view;

    }
    private void mytrip() {

        mDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

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
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful() && response.body() != null) {
                    final String res = response.body().string();
                    Log.v("Response", res);
                    JSONArray arr;
                    try {
                        arr = new JSONArray(res);

                        for (int i = 0; i < arr.length(); i++) {
                            String id = arr.getJSONObject(i).getString("id");
                            String start = arr.getJSONObject(i).getString("start_date_tx");
                            String end = arr.getJSONObject(i).optString("end_date", null);
                            String name = arr.getJSONObject(i).getJSONObject("city").getString("city_name");
                            String tname = arr.getJSONObject(i).getString("trip_name");
                            JSONArray array = arr.getJSONObject(i).getJSONObject("city").getJSONArray("images");
                            String image = array.length() > 1 ? array.getString(0) : null;
                            mTrips.add(new Trip(id, name, image, start, end, tname));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    }
                }
                mHandler.post(() -> {
                    mDialog.dismiss();
                    gridView.setAdapter(new MyTripsAdapter(getContext().getApplicationContext(), mTrips));
                });
            }
        });
    }


}
