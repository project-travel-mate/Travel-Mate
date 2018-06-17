package io.github.project_travel_mate.destinations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import flipviewpager.utils.FlipSettings;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.description.FinalCityInfo;
import objects.City;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK;
import static utils.Constants.EXTRA_MESSAGE_ID;
import static utils.Constants.EXTRA_MESSAGE_IMAGE;
import static utils.Constants.EXTRA_MESSAGE_NAME;

public class CityFragment extends Fragment {

    @BindView(R.id.cityname)
    AutoCompleteTextView    cityname;
    @BindView(R.id.pb)
    ProgressBar             pb;
    @BindView(R.id.music_list)
    ListView                lv;

    private final List<String> mImage = new ArrayList<>();

    private String mNameyet;
    private String mCityid;
    private Activity mActivity;
    private Handler mHandler;

    public CityFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_citylist, container, false);

        ButterKnife.bind(this, view);

        // Hide keyboard
        InputMethodManager imm  = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);

        mHandler    = new Handler(Looper.getMainLooper());
        cityname.setThreshold(1);

        getCity();

        return view;
    }

    @OnTextChanged(R.id.cityname) void onTextChanged() {
        mNameyet = cityname.getText().toString();
        if (!mNameyet.contains(" ")) {
            tripAutoComplete();
        }
    }

    private void tripAutoComplete() {

        // to fetch city names
        String uri = API_LINK +
                "city/autocomplete.php?search=" + mNameyet.trim();
        Log.v("executing", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
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

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray arr;
                        final ArrayList name, id;
                        try {
                            arr = new JSONArray(Objects.requireNonNull(response.body()).string());
                            Log.v("RESPONSE : ", arr.toString());

                            name = new ArrayList<>();
                            id = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                try {
                                    name.add(arr.getJSONObject(i).getString("name"));
                                    id.add(arr.getJSONObject(i).getString("id"));
                                    mImage.add(arr.getJSONObject(i).optString("image", "http://i.ndtvimg.com/i/2015-12/delhi-pollution-traffic-cars-afp_650x400_71451565121.jpg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ArrayAdapter<String> dataAdapter =
                                    new ArrayAdapter<>(
                                            mActivity.getApplicationContext(), R.layout.spinner_layout, name);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cityname.setThreshold(1);
                            cityname.setAdapter(dataAdapter);
                            cityname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    mCityid = id.get(arg2).toString();
                                    Intent i = new Intent(mActivity, FinalCityInfo.class);
                                    i.putExtra(EXTRA_MESSAGE_ID, mCityid);
                                    i.putExtra(EXTRA_MESSAGE_NAME, name.get(arg2).toString());
                                    i.putExtra(EXTRA_MESSAGE_IMAGE, mImage.get(arg2));
                                    startActivity(i);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void getCity() {

        // to fetch city names
        String uri = API_LINK + "all-cities.php";
        Log.v("EXECUTING", uri );

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
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

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(Objects.requireNonNull(response.body()).string());
                            JSONArray ar = ob.getJSONArray("cities");
                            pb.setVisibility(View.GONE);
                            FlipSettings settings = new FlipSettings.Builder().defaultPage().build();
                            List<City> friends = new ArrayList<>();
                            for (int i = 0; i < ar.length(); i++) {

                                double color = Math.random();
                                int c = (int) (color * 100) % 8;

                                int colo;
                                switch (c) {
                                    case 0: colo = R.color.sienna; break;
                                    case 1: colo = R.color.saffron; break;
                                    case 2: colo = R.color.green; break;
                                    case 3: colo = R.color.pink; break;
                                    case 4: colo = R.color.orange; break;
                                    case 5: colo = R.color.saffron; break;
                                    case 6: colo = R.color.purple; break;
                                    case 7: colo = R.color.blue; break;
                                    default: colo = R.color.blue; break;
                                }

                                friends.add(new City(
                                        ar.getJSONObject(i).getString("id"),
                                        ar.getJSONObject(i).optString("image", "yolo"),
                                        ar.getJSONObject(i).getString("name"),
                                        colo,
                                        ar.getJSONObject(i).getString("lat"),
                                        ar.getJSONObject(i).getString("lng"),
                                        "Know More", "View on Map", "Fun Facts", "View Website"));
                            }

                            lv.setAdapter(new CityAdapter(mActivity, friends, settings));
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                                    City f = (City) lv.getAdapter().getItem(position);
                                    Toast.makeText(mActivity, f.getmNickname(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(mActivity, FinalCityInfo.class);
                                    i.putExtra(EXTRA_MESSAGE_ID, f.getId());
                                    i.putExtra(EXTRA_MESSAGE_NAME, f.getmNickname());
                                    i.putExtra(EXTRA_MESSAGE_IMAGE, f.getmAvatar());
                                    startActivity(i);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR", "Message : " + e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        this.mActivity = (Activity) activity;
    }
}
