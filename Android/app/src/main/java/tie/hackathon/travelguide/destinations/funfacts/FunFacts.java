package tie.hackathon.travelguide.destinations.funfacts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tie.hackathon.travelguide.R;

/**
 * Funfacts activity
 */
public class FunFacts extends AppCompatActivity implements FunFactsView {

    @BindView(R.id.vp) ViewPager viewPager;

    private String id;
    private String name;
    private MaterialDialog dialog;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Intent i    = getIntent();
        id          = i.getStringExtra("id_");
        name        = i.getStringExtra("name_");
        mHandler    = new Handler(Looper.getMainLooper());

        initPresenter();
        getSupportActionBar().hide();
    }

    private void initPresenter() {
        FunFactsPresenter mPresenter = new FunFactsPresenter(this);
        mPresenter.initPresenter(id);
    }

    @Override
    public void showProgressDialog() {
        dialog = new MaterialDialog.Builder(FunFacts.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgressDialog() {
        dialog.dismiss();
    }

<<<<<<< HEAD:Android/app/src/main/java/tie/hackathon/travelguide/FunFacts.java
        // to fetch city names
        String uri = Constants.apilink + "city_facts.php?id=" + id;
        Log.e("executing", uri + " ");

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
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject ob = new JSONObject(res);
                            JSONArray ar = ob.getJSONArray("facts");
                            List<Fragment> fList = new ArrayList<>();
                            for (int i = 0; i < ar.length(); i++)
                                fList.add(FunfactFragment.newInstance(ar.getJSONObject(i).getString("image"),
                                        ar.getJSONObject(i).getString("fact"), name));
                            viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), fList));
                            viewPager.setPageTransformer(true, new AccordionTransformer());

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            Log.e("ERROR : ", e1.getMessage() + " ");
                        }
                        dialog.dismiss();
                    }
                });

            }
=======
    /**
     * method called by presenter after successful network request
     * Presenter passes JSON facts array used for setting up view-pager
     * @param factsArray -> JSON array of facts
     */
    @Override
    public void setupViewPager(JSONArray factsArray) {
        mHandler.post(() -> {
            List<Fragment> fList = new ArrayList<>();
            for (int i = 0; i < factsArray.length(); i++)
                try {
                    fList.add(FunfactFragment.newInstance(factsArray.getJSONObject(i).getString("image"),
                            factsArray.getJSONObject(i).getString("fact"), name));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), fList));
            viewPager.setPageTransformer(true, new AccordionTransformer());
>>>>>>> 0a5829f844a19d1dca45d52e54107ab8f7705cff:Android/app/src/main/java/tie/hackathon/travelguide/destinations/funfacts/FunFacts.java
        });
    }

    /**
     * Sets adapter for funfacts
     */
    class MyPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments;

        MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}
