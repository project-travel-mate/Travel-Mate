package tie.hackathon.travelguide.destinations.funfacts;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by niranjanb on 14/06/17.
 */

public class FunFactsPresenter {
    FunFactsView mFunFactsView;

    public FunFactsPresenter(FunFactsView funFactsView) {
        mFunFactsView = funFactsView;
    }

    public void initPresenter(String id) {
        getCityFacts(id);
    }

    // Fetch fun facts about city
    private void getCityFacts(String id) {
        mFunFactsView.showProgressDialog();

        // to fetch city names
        String uri = Constants.apilink + "city_facts.php?id=" + id;

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
                mFunFactsView.hideProgressDialog();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();

                try {
                    JSONObject ob = new JSONObject(res);
                    JSONArray ar = ob.getJSONArray("facts");
                    mFunFactsView.setupViewPager(ar);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mFunFactsView.hideProgressDialog();
                }
                mFunFactsView.hideProgressDialog();
            }
        });
    }
}
