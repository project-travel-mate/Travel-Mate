package tie.hackathon.travelguide;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;
import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;
import adapters.Books_adapter;
import adapters.City_info_adapter;

public class CityInfo extends AppCompatActivity {

    SharedPreferences s ;
    SharedPreferences.Editor e;
    private ProgressDialog progressDialog;
    TwoWayView lvRest,lvShop, lvTour,lvhang;
    ProgressBar pb1,pb2,pb3,pb4;
    TextView city_info,min,max,pre;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        i = getIntent();

        lvRest = (TwoWayView) findViewById(R.id.lvRestaurants);
        lvTour = (TwoWayView) findViewById(R.id.lvTourists);
        lvShop = (TwoWayView) findViewById(R.id.lvShopping);
        lvhang = (TwoWayView) findViewById(R.id.lvhangout);
        pre = (TextView) findViewById(R.id.pre);
        pb1 = (ProgressBar) findViewById(R.id.pb1);
        pb2 = (ProgressBar) findViewById(R.id.pb2);
        pb3 = (ProgressBar) findViewById(R.id.pb3);
        pb4 = (ProgressBar) findViewById(R.id.pb4);
        min = (TextView) findViewById(R.id.min);
        max= (TextView) findViewById(R.id.max);
        city_info = (TextView) findViewById(R.id.city_info);

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();


        try {
            new Book_RetrieveFeed().execute();

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Can't connect.");
            alertDialog.setMessage("We cannot connect to the internet right now. Please try again later. Exception e: " + e.toString());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            Log.e("YouTube:", "Cannot fetch " + e.toString());
        }



        String tit = i.getStringExtra("name_");
        if(tit==null)
            tit = s.getString(Constants.DESTINATION_CITY, "Delhi");
        setTitle(tit);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() ==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public class Book_RetrieveFeed extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CityInfo.this);
            progressDialog.setMessage("Fetching data, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();     Log.e("vdslmvdspo","started");
        }

        protected String doInBackground(String... urls) {
            try {
                String id = i.getStringExtra("id_");
                Log.e("cbvsbk",id+" " );
                if(id==null){
                 id = s.getString(Constants.DESTINATION_CITY_ID,"1");
                }
                String uri = "http://csinsit.org/prabhakar/tie/get-city-info.php?id="+id;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                Log.e("here",uri +" ");
                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String Result) {
            try {
                JSONObject YTFeed = new JSONObject(String.valueOf(Result));

                city_info.setText(YTFeed.getString("description"));


                min.setText("MIN : " +YTFeed.getJSONObject("weather").getString("min")+ " C");
                max.setText("MAX : " +YTFeed.getJSONObject("weather").getString("max") + " C");

                if(Double.parseDouble(YTFeed.getJSONObject("weather").getString("min"))<20){

                    pre.setText("Do not forget to take extra sweaters.");

                }else {
                    if (Double.parseDouble(YTFeed.getJSONObject("weather").getString("max")) > 35) {

                        pre.setText("It seems pretty hot there.");

                    } else {
                        pre.setText("Enjoy weather.");

                    }
                }


                JSONArray YTFeedItems = YTFeed.getJSONArray("food");
                Log.e("response", YTFeedItems + " ");
                pb1.setVisibility(View.GONE);
                lvRest.setAdapter(new City_info_adapter(CityInfo.this, YTFeedItems, R.drawable.restaurant));

                YTFeedItems = YTFeed.getJSONArray("monuments");
                Log.e("response", YTFeedItems + " ");
                pb2.setVisibility(View.GONE);
                lvTour.setAdapter(new City_info_adapter(CityInfo.this, YTFeedItems, R.drawable.monuments));


                YTFeedItems = YTFeed.getJSONArray("hangout-places");
                Log.e("response", YTFeedItems + " ");
                pb3.setVisibility(View.GONE);
                lvhang.setAdapter(new City_info_adapter(CityInfo.this, YTFeedItems, R.drawable.hangout));

                YTFeedItems = YTFeed.getJSONArray("shopping");
                Log.e("response", YTFeedItems + " ");
                pb4.setVisibility(View.GONE);
                lvShop.setAdapter(new City_info_adapter(CityInfo.this, YTFeedItems, R.drawable.shopping));
                progressDialog.hide();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("vsdfkvaes",e.getMessage()+" dsv");
            }
        }
    }

}
