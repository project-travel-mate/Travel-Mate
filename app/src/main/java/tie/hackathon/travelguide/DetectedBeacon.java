package tie.hackathon.travelguide;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;

public class DetectedBeacon extends AppCompatActivity {

    String major;
    String name,des,image,cname,cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_beacon);

        Intent i = getIntent();

        major = i.getStringExtra(Constants.CUR_MAJOR);
        Log.e("goit the beacon",major+" ");


            new getcitytask().execute();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }




    public class getcitytask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Log.e("doing","dbgsbghd");
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String uri = "http://csinsit.org/prabhakar/tie/estimote_monuments/get_info.php?id=" +
                      major;
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

            if(result ==null)
                return;

            try {
                //Tranform the string into a json object

                final JSONObject json = new JSONObject(result);


                name = json.getString("monument_name");
                des = json.getString("monument_description");
                image = json.getString("monument_image");
                cname = json.getString("city_name");
                cid = json.getString("city_id");



                TextView tv = (TextView) findViewById(R.id.tv);
                tv.setText(des);

                tv = (TextView) findViewById(R.id.head);
                tv.setText(name);

                ImageView iv = (ImageView) findViewById(R.id.imag);
                Picasso.with(DetectedBeacon.this).load(image).error(R.drawable.delhi).placeholder(R.drawable.delhi).into(iv);


            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() ==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


}
