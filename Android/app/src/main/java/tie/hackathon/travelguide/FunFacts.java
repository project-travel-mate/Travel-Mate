package tie.hackathon.travelguide;

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

/**
 * Funfacts activity
 */
public class FunFacts extends AppCompatActivity {

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

        // Fetch fun facts about city
        getCityFacts();

        getSupportActionBar().hide();
    }

    private void getCityFacts() {

        dialog = new MaterialDialog.Builder(FunFacts.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

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
                mHandler.post(() -> {

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
                });

            }
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
