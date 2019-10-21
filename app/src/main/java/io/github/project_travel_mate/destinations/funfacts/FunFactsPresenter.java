package io.github.project_travel_mate.destinations.funfacts;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import objects.FunFact;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static objects.FunFact.createFunFactList;
import static utils.Constants.API_LINK_V2;

/**
 * Created by niranjanb on 14/06/17.
 */

class FunFactsPresenter {
    private final FunFactsView mFunFactsView;
    private ArrayList<String> mFactsText;
    private ArrayList<String> mFactsSourceText;
    private ArrayList<String> mFactsSourceURL;

    private ArrayList<String> mImages;
    FunFactsPresenter(FunFactsView funFactsView) {
        mFunFactsView = funFactsView;
    }

    public void initPresenter(String id, String token) {
        mFunFactsView.showProgressDialog();
        mFactsText = new ArrayList<>();
        mFactsSourceText = new ArrayList<>();
        mFactsSourceURL = new ArrayList<>();
        mImages = new ArrayList<>();
        getCityFacts(id, token);
    }

    // Fetch fun facts about city
    private void getCityFacts(final String id, final String token) {

        // to fetch city names
        String uri = API_LINK_V2 + "get-city-facts/" + id;
        Log.v("EXECUTING", uri );

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                try {
                    JSONArray array = new JSONArray(res);
                    for (int i = 0; i < array.length(); i++) {
                        mFactsText.add(array.getJSONObject(i).getString("fact"));
                        mFactsSourceText.add(array.getJSONObject(i).getString("source_text"));
                        mFactsSourceURL.add(array.getJSONObject(i).getString("source_url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getCityImages(id, token);
            }
        });
    }


    // Fetch images of a city
    private void getCityImages(String id, String token) {

        // to fetch city names
        String uri = API_LINK_V2 + "get-city-images/" + id;
        Log.v("EXECUTING", uri );

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + token)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                Log.v("RESPONSE", res);
                try {
                    JSONArray array = new JSONArray(res);
                    for (int i = 0; i < array.length(); i++) {
                        mImages.add(array.getJSONObject(i).getString("image_url"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<FunFact> funFacts = createFunFactList(mFactsText, mImages, mFactsSourceText, mFactsSourceURL);
                mFunFactsView.setupViewPager(funFacts);
                mFunFactsView.hideProgressDialog();
            }
        });

    }
}
