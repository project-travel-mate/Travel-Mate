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

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    /**
     * method called by presenter after successful network request
     * Presenter passes JSON facts array used for setting up view-pager
     * @param factsArray -> JSON array of facts
     */
    @Override
    public void setupViewPager(final JSONArray factsArray) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<Fragment> fList = new ArrayList<>();
                for (int i = 0; i < factsArray.length(); i++)
                    try {
                        fList.add(FunfactFragment.newInstance(factsArray.getJSONObject(i).getString("image"),
                                factsArray.getJSONObject(i).getString("fact"), name));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                viewPager.setAdapter(new MyPageAdapter(FunFacts.this.getSupportFragmentManager(), fList));
                viewPager.setPageTransformer(true, new AccordionTransformer());
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
