package tie.hackathon.travelguide;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;

public class FinalCityInfo extends AppCompatActivity implements View.OnClickListener {

    Intent i;
    String id, tit, image,description;
    ImageView iv,ico;
    TextView title;
    String lat,lon;
    ExpandableTextView des;
    Typeface code,tex,codeb;

    TextView fftext;
    TextView temp,humidity,weatherinfo;
    MaterialDialog dialog;
    LinearLayout funfact, restau, hangout, monum, shopp, trend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_city_info);


        code = Typeface.createFromAsset(getAssets(), "fonts/whitney_book.ttf");
        codeb = Typeface.createFromAsset(getAssets(), "fonts/CODE_Bold.otf");
        tex = Typeface.createFromAsset(getAssets(), "fonts/texgyreadventor-regular.otf");
        des = (ExpandableTextView) findViewById(R.id.expand_text_view);

        des.setText(getString(R.string.sample_string));
        iv = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.head);
        ico = (ImageView) findViewById(R.id.icon);
        temp = (TextView) findViewById(R.id.temp);
        humidity = (TextView) findViewById(R.id.humidit);
        weatherinfo = (TextView) findViewById(R.id.weatherinfo);

        i = getIntent();
        tit = i.getStringExtra("name_");
        setTitle(tit);
        id = i.getStringExtra("id_");
        image = i.getStringExtra("image_");

        title.setTypeface(codeb);
        title.setText(tit);

        funfact = (LinearLayout) findViewById(R.id.funfact);
        restau = (LinearLayout) findViewById(R.id.restau);
        hangout = (LinearLayout) findViewById(R.id.hangout);
        monum = (LinearLayout) findViewById(R.id.monu);
        shopp = (LinearLayout) findViewById(R.id.shoppp);
        trend = (LinearLayout) findViewById(R.id.trends);


        fftext = (TextView) findViewById(R.id.fftext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.hgtext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.shtext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.mntext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.rstext);
        fftext.setTypeface(code);
        fftext = (TextView) findViewById(R.id.cttext);
        fftext.setTypeface(code);




        funfact.setOnClickListener(this);
        restau.setOnClickListener(this);
        hangout.setOnClickListener(this);
        monum.setOnClickListener(this);
        shopp.setOnClickListener(this);
        trend.setOnClickListener(this);

        new cityinfo().execute();

        Picasso.with(this).load(image).into(iv);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {

            case R.id.funfact:
                i = new Intent(FinalCityInfo.this, FunFacts.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                startActivity(i);
                break;


            case R.id.restau:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("lat_",lat);
                i.putExtra("lng_",lon);
                i.putExtra("name_", tit);
                i.putExtra("type_", "restaurant");
                startActivity(i);
                break;
            case R.id.hang:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("lat_",lat);
                i.putExtra("lng_",lon);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "hangout");
                startActivity(i);
                break;
            case R.id.monu:
                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("lat_",lat);
                i.putExtra("lng_",lon);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("type_", "monument");
                startActivity(i);
                break;
            case R.id.shoppp:

                i = new Intent(FinalCityInfo.this, PlacesOnMap.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                i.putExtra("lat_",lat);
                i.putExtra("lng_",lon);
                i.putExtra("type_", "shopping");
                startActivity(i);
                break;


            case R.id.trends :
                i = new Intent(FinalCityInfo.this, Tweets.class);
                i.putExtra("id_", id);
                i.putExtra("name_", tit);
                startActivity(i);
                break;


        }

    }


    public class cityinfo extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(FinalCityInfo.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("started", "strted");
                String uri = Constants.apilink +
                        "city/info.php?id=" + id;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here", url + readStream + " ");
                return readStream;
            } catch (Exception e) {
                Log.e("here", e.getMessage() + " ");
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null)
                return;

            try {
                //Tranform the string into a json object
                JSONObject ob = new JSONObject(result);

                description = ob.getString("description");
                des.setText(description);

                Picasso.with(FinalCityInfo.this).load(ob.getJSONObject("weather").getString("icon")).into(ico);
                temp.setText(ob.getJSONObject("weather").getString("temprature")+  (char) 0x00B0 + " C ");
                humidity.setText("Humidity : " + ob.getJSONObject("weather").getString("humidity"));
                weatherinfo.setText(ob.getJSONObject("weather").getString("description"));
                lat = ob.getString("lat");
                lon = ob.getString("lng");





            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
            dialog.dismiss();
        }

    }

}
