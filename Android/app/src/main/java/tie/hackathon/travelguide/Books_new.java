package tie.hackathon.travelguide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;

public class Books_new extends AppCompatActivity {

    public static ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_new);


        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 1) {

                }
            }
        });

        PagerTabStrip titleStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        titleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        FragmentManager fm = getSupportFragmentManager();
        Books_mainadapter pageAdapter = new Books_mainadapter(fm);
        pager.setAdapter(pageAdapter);

        setTitle("Books");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public class Books_mainadapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;

        public Books_mainadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            if (arg0 == 0) {
                books_all_fragment umf = new books_all_fragment();

                return umf;
            } else {
                books_all2_fragment umf = new books_all2_fragment();
                return umf;
            }

        }


        /**
         * Returns the number of pages
         */
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 1)
                return "All";
            else
                return "Suggested";

        }

    }

}
