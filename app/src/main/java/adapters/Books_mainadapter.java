package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tie.hackathon.travelguide.books_all2_fragment;
import tie.hackathon.travelguide.books_all_fragment;

/**
 * Created by Swati Garg on 08-06-2015.
 */
public class Books_mainadapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;

        public Books_mainadapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            if (arg0==0)
            {
               books_all_fragment umf = new books_all_fragment();

                return umf;
            }
            else
            {
                books_all2_fragment umf = new books_all2_fragment();
                return umf;
            }

        }




        /** Returns the number of pages */
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position==1)
                return "All";
            else
                return "Suggested";

        }

    }
