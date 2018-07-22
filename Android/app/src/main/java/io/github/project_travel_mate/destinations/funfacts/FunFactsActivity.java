package io.github.project_travel_mate.destinations.funfacts;

import android.content.Context;
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
import android.util.Log;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

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
public class FunFactsActivity extends AppCompatActivity implements FunFactsView {

    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;

    private City mCity;
    private String mToken;
    private Handler mHandler;
    Boolean isFirstVisitEnd = true;
    public int scrollState;



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
    }

    private void initPresenter() {
        FunFactsPresenter mPresenter = new FunFactsPresenter(this);
        mPresenter.initPresenter(mCity.getId(), mToken);
    }

    @Override
    public void showProgressDialog() {
        animationView.playAnimation();
    }

    @Override
    public void hideProgressDialog() {
    }

    /**
     * method called by presenter after successful network request
     * Presenter passes JSON facts array used for setting up view-pager
     * @param factsArray -> JSON array of facts
     */
    @Override
    public void setupViewPager(final ArrayList<FunFact> factsArray) {
        mHandler.post(() -> {
            List<Fragment> fList = new ArrayList<>();
            for (int i = 0; i < factsArray.size(); i++) {
                FunFact fact = new FunFact(mCity.getNickname(),
                        factsArray.get(i).getImage(),
                        factsArray.get(i).getText(), factsArray.get(i).getSource(), factsArray.get(i).getSourceURL());
                fList.add(FunfactFragment.newInstance(fact));
            }
            viewPager.setAdapter(new MyPageAdapter(FunFactsActivity.this.getSupportFragmentManager(), fList));
            viewPager.setPageTransformer(true, new AccordionTransformer());
            animationView.setVisibility(View.GONE);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    try {
                        if (position == factsArray.size() - 1 & scrollState == 1 & !isFirstVisitEnd) {
                            viewPager.setCurrentItem(0, false);
                        }
                        if (position == 0 & scrollState == 1) {
                            viewPager.setCurrentItem(factsArray.size() - 1, false);
                        }
                    } catch (Exception e) {
                        Log.e("Message :", e.getMessage());
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    isFirstVisitEnd = position != factsArray.size() - 1;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    scrollState = state;
                }
            });
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

    public static Intent getStartIntent(Context context, City city) {
        Intent intent = new Intent(context, FunFactsActivity.class);
        intent.putExtra(EXTRA_MESSAGE_CITY_OBJECT, city);
        return intent;
    }

}
