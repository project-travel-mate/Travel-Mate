package tie.hackathon.travelguide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import Util.GPSTracker;
import Util.Utils;

public class MapActivity extends AppCompatActivity {

    com.google.android.gms.maps.MapFragment mapFragment;
    GoogleMap map;
    SharedPreferences s ;
    SharedPreferences.Editor e;
    String sorcelat,deslat,sorcelon,deslon,surce,dest,distancetext,mode,curlat,curlon;
    Intent i;
    List<String > name,call,web,addr;
    ScrollView sc;
    int index = 0;
    int icon = R.drawable.ic_attach_money_black_24dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        i = getIntent();
        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        sorcelat = s.getString(Constants.SOURCE_CITY_LAT, Constants.DELHI_LAT);
        sorcelon  = s.getString(Constants.SOURCE_CITY_LON, Constants.DELHI_LON);
        deslat = s.getString(Constants.DESTINATION_CITY_LAT,Constants.MUMBAI_LAT);
        deslon  = s.getString(Constants.DESTINATION_CITY_LON, Constants.MUMBAI_LON);
        surce = s.getString(Constants.SOURCE_CITY, "Delhi");
        dest  = s.getString(Constants.DESTINATION_CITY, "MUmbai");
        mode = i.getStringExtra(Constants.MODE);
        sc = (ScrollView) findViewById(R.id.data);

        Integer mo = Integer.parseInt(mode);
        switch(mo){

            case 0 : icon = R.drawable.ic_local_pizza_black_24dp;break;
            case 1 : icon = R.drawable.ic_local_bar_black_24dp;break;
            case 2 : icon = R.drawable.ic_camera_alt_black_24dp;break;
            case 3 : icon = R.drawable.ic_directions_bus_black_24dp;break;
            case 4 : icon = R.drawable.ic_local_mall_black_24dp;break;
            case 5 : icon = R.drawable.ic_local_gas_station_black_24dp;break;
            case 6 : icon = R.drawable.ic_local_atm_black_24dp;break;
            case 7 : icon = R.drawable.ic_local_hospital_black_24dp;break;

            default : icon = R.drawable.ic_attach_money_black_24dp;break;
        }


        sc.setVisibility(View.GONE);
        name = new ArrayList<String>();
        call = new ArrayList<String>();
        web = new ArrayList<String>();
        addr = new ArrayList<String>();

        curlat = deslat;
        curlon = deslon;

        setTitle("Places");
        GPSTracker tracker = new GPSTracker(this);
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
            Log.e("cdsknvdsl ", curlat + "dsbjvdks" + curlon);
        } else {
            curlat = Double.toString(tracker.getLatitude());
            curlon = Double.toString(tracker.getLongitude());
            Log.e("cdsknvdsl",tracker.getLatitude() + " " + curlat+"dsbjvdks"+curlon);
            if(curlat.equals("0.0")){
                curlat = "28.5952242";
                 curlon = "77.1656782";
            }

            new getcitytask().execute();
        }


        LatLng coordinate = new LatLng(Double.parseDouble(curlat), Double.parseDouble(curlon));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 16);
        map.animateCamera(yourLocation);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                sc.setVisibility(View.VISIBLE);
                int i;
                for (i = 0; i < name.size(); i++) {
                    if (name.get(i).equals(marker.getTitle())) {
                        index = i;
                        break;
                    }
                }


                TextView Title = (TextView) findViewById(R.id.VideoTitle);
                TextView Description = (TextView) findViewById(R.id.VideoDescription);
                final Button calls, map, book;
                calls = (Button) findViewById(R.id.call);
                book = (Button) findViewById(R.id.book);

                Title.setText(name.get(index));
                Description.setText(addr.get(index));
                calls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + call.get(index)));
                        startActivity(intent);

                    }
                });
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = null;
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web.get(index)));
                        startActivity(browserIntent);
                    }
                });
                return false;
            }

        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



 }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() ==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public void ShowMarker(Double LocationLat, Double LocationLong, String LocationName, Integer LocationIcon){
        LatLng Coord = new LatLng(LocationLat, LocationLong);

        if(map!=null) {
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Coord, 5));

            MarkerOptions abc = new MarkerOptions();
            MarkerOptions x = abc
                    .title(LocationName)
                    .position(Coord)
                    .icon(BitmapDescriptorFactory.fromResource(LocationIcon));
            map.addMarker(x);

        }
    }


    public class getcitytask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String uri = "http://csinsit.org/prabhakar/tie/get-real-time-data.php?mode=" +
                        mode +
                        "&lat=" +
                        curlat +
                        "&lng=" +
                        curlon;
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
            try {
                //Tranform the string into a json object

                final JSONObject json = new JSONObject(result);
                JSONArray routeArray = json.getJSONArray("results");

                for(int i=0;i<routeArray.length();i++){
                    name.add(routeArray.getJSONObject(i).getString("name"));
                    web.add(routeArray.getJSONObject(i).getString("website"));
                    call.add(routeArray.getJSONObject(i).getString("phone"));
                    addr.add(routeArray.getJSONObject(i).getString("address"));
                    ShowMarker(Double.parseDouble(routeArray.getJSONObject(i).getString("lat")),
                            Double.parseDouble(routeArray.getJSONObject(i).getString("lng")),
                            routeArray.getJSONObject(i).getString("name"),
                            icon);


                }



            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
        }

    }


}
