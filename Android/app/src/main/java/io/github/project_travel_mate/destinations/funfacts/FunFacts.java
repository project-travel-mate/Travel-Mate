package io.github.project_travel_mate.destinations.funfacts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;
import objects.City;
import objects.FunFact;

import static utils.Constants.EXTRA_MESSAGE_CITY_OBJECT;
import static utils.Constants.USER_TOKEN;

/**
 * Funfacts activity
 */
public class FunFacts extends AppCompatActivity implements FunFactsView {

    @BindView(R.id.vp)
    ViewPager viewPager;

    private City mCity;
    private String mToken;
    private MaterialDialog mDialog;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mCity = (City) intent.getSerializableExtra(EXTRA_MESSAGE_CITY_OBJECT);
        mHandler  = new Handler(Looper.getMainLooper());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        initPresenter();
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void initPresenter() {
        FunFactsPresenter mPresenter = new FunFactsPresenter(this);
        mPresenter.initPresenter(mCity.getId(), mToken);
    }

    @Override
    public void showProgressDialog() {
        mDialog = new MaterialDialog.Builder(FunFacts.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();
    }

    @Override
    public void hideProgressDialog() {
        mDialog.dismiss();
    }

    /**
     * method called by presenter after successful network request
     * Presenter passes JSON facts array used for setting up view-pager
     * @param factsArray -> JSON array of facts
     */
    @Override
    public void setupViewPager(final ArrayList<FunFact> factsArray) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<Fragment> fList = new ArrayList<>();
                for (int i = 0; i < factsArray.size(); i++) {
                    FunFact fact = new FunFact(mCity.getNickname(),
                            factsArray.get(i).getImage(),
                            factsArray.get(i).getText());
                    fList.add(FunfactFragment.newInstance(fact));
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
        private final List<Fragment> mFragments;

        MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragments.get(position);
        }

        @Override
        public int getCount() {
            return this.mFragments.size();
        }
    }
}
