package tie.hackathon.travelguide.destinations.description;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Util.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by niranjanb on 15/05/17.
 */

public class FinalCityInfoPresenter {
    FinalCityInfoView mFinalCityInfoView;
    OkHttpClient mOkHttpClient;

    public FinalCityInfoPresenter() {
        mOkHttpClient = new OkHttpClient();
    }

    public void attachView(FinalCityInfoView finalCityInfoView) {
        mFinalCityInfoView = finalCityInfoView;
    }

    public void fetchCityInfo(String id) {
        mFinalCityInfoView.showProgress();

        String uri = Constants.apilink + "city/info.php?id=" + id;
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
                final String res = response.body().string();

                try {
                    JSONObject ob = new JSONObject(res);
                    mFinalCityInfoView.parseResult(ob.getString("description"),
                            ob.getJSONObject("weather").getString("icon"),
                            ob.getJSONObject("weather").getString("temprature"),
                            ob.getJSONObject("weather").getString("humidity"),
                            ob.getJSONObject("weather").getString("description"),
                            ob.getString("lat"), ob.getString("lng"));
                    mFinalCityInfoView.dismissProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                    mFinalCityInfoView.dismissProgress();
                }
            }
        });
    }

}
