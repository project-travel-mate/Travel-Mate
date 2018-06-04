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

import static utils.Constants.API_LINK;

/**
 * Created by niranjanb on 15/05/17.
 */

class FinalCityInfoPresenter {
    private FinalCityInfoView   mFinalCityInfoView;
    private final OkHttpClient  mOkHttpClient;

    public FinalCityInfoPresenter() {
        mOkHttpClient = new OkHttpClient();
    }

    public void attachView(FinalCityInfoView finalCityInfoView) {
        mFinalCityInfoView = finalCityInfoView;
    }

    public void fetchCityInfo(String id) {
        mFinalCityInfoView.showProgress();

        String uri = API_LINK + "city/info.php?id=" + id;
        Request request = new Request.Builder()
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
                    mFinalCityInfoView.parseResult(responseObject.getString("description"),
                            responseObject.getJSONObject("weather").getString("icon"),
                            responseObject.getJSONObject("weather").getString("temprature"),
                            responseObject.getJSONObject("weather").getString("humidity"),
                            responseObject.getJSONObject("weather").getString("description"),
                            responseObject.getString("lat"), responseObject.getString("lng"));
                    mFinalCityInfoView.dismissProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mFinalCityInfoView.dismissProgress();
                }
            }
        });
    }

}
