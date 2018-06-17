package io.github.project_travel_mate.destinations.description;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private FinalCityInfoView   mFinalCityInfoView;
    private final OkHttpClient  mOkHttpClient;

    FinalCityInfoPresenter() {
        mOkHttpClient = new OkHttpClient();
    }

    public void attachView(FinalCityInfoView finalCityInfoView) {
        mFinalCityInfoView = finalCityInfoView;
    }

    /**
     * Calls the API & fetch details of the weather of city with given name
     * @param cityName the city id
     * @param token authentication token
     */
    public void fetchCityInfo(String cityName, String token) {
        mFinalCityInfoView.showProgress();

        String uri = API_LINK_V2 + "get-city-weather/" + cityName;

        Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mFinalCityInfoView.dismissProgress();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();

                try {
                    JSONObject responseObject = new JSONObject(res);
                    mFinalCityInfoView.parseResult(
                            responseObject.getString("icon"),
                            responseObject.getString("temp") + " " + responseObject.getString("temp_units"),
                            responseObject.getString("humidity") + " " + responseObject.getString("humidity_units"),
                            responseObject.getString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mFinalCityInfoView.dismissProgress();
            }
        });
    }
}
