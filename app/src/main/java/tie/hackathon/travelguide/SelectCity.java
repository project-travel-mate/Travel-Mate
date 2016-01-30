package tie.hackathon.travelguide;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import Util.Utils;

public class SelectCity extends AppCompatActivity {

    Spinner source,dest;
    ProgressBar pb;
    Button ok;
    SharedPreferences s ;
    String[] cities;
    SharedPreferences.Editor e;
    List<String> id = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> lat = new ArrayList<>();
    List<String> lon = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       


        pb = (ProgressBar) findViewById(R.id.pb);
        source = (Spinner) findViewById(R.id.source);
        dest = (Spinner) findViewById(R.id.destination);
        ok = (Button) findViewById(R.id.ok);
        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sposition = source.getSelectedItemPosition();
                int dposition = dest.getSelectedItemPosition();

                if(sposition == dposition){
                    Snackbar.make(view, "Source and destination cannot be same", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }else {
                    e.putString(Constants.DESTINATION_CITY_ID, id.get(dposition));
                    e.putString(Constants.SOURCE_CITY_ID, id.get(sposition));
                    e.putString(Constants.DESTINATION_CITY, names.get(dposition));
                    e.putString(Constants.SOURCE_CITY, names.get(sposition));
                    e.putString(Constants.DESTINATION_CITY_LAT, lat.get(dposition));
                    e.putString(Constants.SOURCE_CITY_LAT, lat.get(sposition));
                    e.putString(Constants.DESTINATION_CITY_LON, lon.get(dposition));
                    e.putString(Constants.SOURCE_CITY_LON, lon.get(sposition));
                    startService(new Intent(SelectCity.this, LocationService.class));

                    e.commit();
                    finish();
                }
            }
        });

        new getcitytask().execute();


        getSupportActionBar().setTitle(" ");

    }



    public class getcitytask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access


            try {
                String uri = "http://csinsit.org/prabhakar/tie/all-cities.php";
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here",readStream+" ");
                return readStream;

//                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }



        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject ob  = new JSONObject(result);
                JSONArray ar = ob.getJSONArray("cities");
                for(int i = 0 ; i<ar.length();i++){
                    id.add(ar.getJSONObject(i).getString("id"));
                    names.add(ar.getJSONObject(i).getString("name"));
                    lat.add(ar.getJSONObject(i).getString("lat"));
                    lon.add(ar.getJSONObject(i).getString("lng"));

                }
                cities = new String[id.size()];
                cities = names.toArray(cities);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectCity.this, android.R.layout.simple_spinner_dropdown_item,cities);
                source.setAdapter(adapter);
                dest.setAdapter(adapter);
                pb.setVisibility(View.GONE);

            } catch (JSONException e1) {
                e1.printStackTrace();
                Log.e("heer",e1.getMessage()+" ");
            }
        }

    }


}
