package tie.hackathon.travelguide;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import Util.Utils;

public class FunFacts extends AppCompatActivity {

    String id, name;
    ViewPager vp;
    MaterialDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_facts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        id = i.getStringExtra("id_");
        name = i.getStringExtra("name_");

        vp = (ViewPager) findViewById(R.id.vp);


        new getcityfacts().execute();


        getSupportActionBar().hide();


    }


    public class getcityfacts extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(FunFacts.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();

        }

        @Override
        protected String doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access



            try {
                String uri = Constants.apilink +
                        "city_facts.php?id=" + id;
                Log.e("egesg","gegrgrgrwgh" + uri);

                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here", uri + " " + readStream + " ");
                return readStream;

//                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            List<Fragment> fList = new ArrayList<Fragment>();

            if (result == null) {
                Toast.makeText(FunFacts.this, "No result", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject ob = new JSONObject(result);
                JSONArray ar = ob.getJSONArray("facts");


                //        lv.setAdapter(new Cities_adapter(activity,ar));

                for (int i = 0; i < ar.length(); i++) {


                    fList.add(Funfact_fragment.newInstance(ar.getJSONObject(i).getString("image"),
                            ar.getJSONObject(i).getString("fact"), name));
                }


                vp.setAdapter(new MyPageAdapter(getSupportFragmentManager(), fList));
                vp.setPageTransformer(true, new AccordionTransformer());

            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.e("heer", e1.getMessage() + " ");
            }
            dialog.dismiss();
        }

    }


    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
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
