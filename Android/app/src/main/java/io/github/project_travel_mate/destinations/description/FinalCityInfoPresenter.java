package io.github.project_travel_mate.destinations.description;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;

/**
 * Created by niranjanb on 15/05/17.
 */

class FinalCityInfoPresenter {
    private final OkHttpClient mOkHttpClient;
    private FinalCityInfoView mFinalCityInfoView;

    FinalCityInfoPresenter() {
        mOkHttpClient = new OkHttpClient();
    }

    public void attachView(FinalCityInfoView finalCityInfoView) {
        mFinalCityInfoView = finalCityInfoView;
    }

    /**
     * Calls the API & fetch details of the weather of city with given name
     *
     * @param cityid the city id
     * @param token    authentication token
     */
    public void fetchCityWeather(String cityid, String token) {

        String uri = API_LINK_V2 + "get-city-weather/" + cityid;
        uri = uri.replaceAll(" ", "%20");

        Log.v("EXECUTING", uri);

        Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mFinalCityInfoView.networkError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {

                    final String res = Objects.requireNonNull(response.body()).string();
                    try {
                        JSONObject responseObject = new JSONObject(res);
                        mFinalCityInfoView.parseResult(
                                responseObject.getString("icon"),
                                responseObject.getInt("code"),
                                responseObject.getString("temp") +
                                        (char) 0x00B0 + responseObject.getString("temp_units"),
                                responseObject.getString("humidity") + " " + responseObject.getString("humidity_units"),
                                responseObject.getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mFinalCityInfoView.networkError();
                    }
                } else {
                    mFinalCityInfoView.networkError();
                }
            }
        });
    }


    /**
     * Calls the API & fetch details of city with given id
     *
     * @param cityid the city id
     * @param token    authentication token
     */
    public void fetchCityInfo(String cityid, String token) {

        String uri = API_LINK_V2 + "get-city/" + cityid;

        final Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mFinalCityInfoView.networkError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    final String res = Objects.requireNonNull(response.body()).string();
                    try {
                        Log.v("Response", res);
                        JSONObject responseObject = new JSONObject(res);
                        JSONArray arr = responseObject.getJSONArray("images");
                        ArrayList<String> imagesArray = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            imagesArray.add(arr.getString(i));
                        }
                        mFinalCityInfoView.parseInfoResult(
                                responseObject.getString("description"),
                                responseObject.getString("latitude"),
                                responseObject.getString("longitude"),
                                imagesArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mFinalCityInfoView.networkError();
                    }
                } else {
                    mFinalCityInfoView.networkError();
                }
            }
        });
    }
}
