package tie.hackathon.travelguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.GPSTracker;
import Util.Utils;

public class PlacesOnMap extends AppCompatActivity {

    TwoWayView lv;
    String id, name;
    Intent i;
    private ProgressDialog progressDialog;
    String  deslon, deslat;
    SharedPreferences s;
    int mode,icon;


    String curlat, curlon,type;



    com.google.android.gms.maps.MapFragment mapFragment;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_on_map);
        lv = (TwoWayView) findViewById(R.id.lv);
        i = getIntent();
        name = i.getStringExtra("name_");
        setTitle(name);
        id = i.getStringExtra("id_");
        type = i.getStringExtra("type_");


        switch (type){

            case "restaurant" : mode = 0;
                icon = R.drawable.restaurant;

                break;
            case "hangout" : mode = 1;
                icon = R.drawable.hangout;
                break;
            case "monument" : mode = 2 ;
                icon = R.drawable.monuments;
                break;
            default : mode = 4;
                icon = R.drawable.shopping;
                break;
        }


        deslat = i.getStringExtra("lat_");
        deslon = i.getStringExtra("lng_");


        new getplaces().execute();

        this.mapFragment = (com.google.android.gms.maps.MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        GPSTracker tracker = new GPSTracker(this);
        if (tracker.canGetLocation() == false) {
            tracker.showSettingsAlert();
            Log.e("cdsknvdsl ", curlat + "dsbjvdks" + curlon);
        } else {
            curlat = Double.toString(tracker.getLatitude());
            curlon = Double.toString(tracker.getLongitude());
            Log.e("cdsknvdsl", tracker.getLatitude() + " " + curlat + "dsbjvdks" + curlon);
            if (curlat.equals("0.0")) {
                curlat = "28.5952242";
                curlon = "77.1656782";
            }
            LatLng coordinate = new LatLng(Double.parseDouble(curlat), Double.parseDouble(curlon));
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
            map.animateCamera(yourLocation);

        }





        setTitle("Places");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    public void ShowMarker(Double LocationLat, Double LocationLong, String LocationName, Integer LocationIcon) {
        LatLng Coord = new LatLng(LocationLat, LocationLong);

        if (map != null) {
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(Coord, 14));

            MarkerOptions abc = new MarkerOptions();
            MarkerOptions x = abc
                    .title(LocationName)
                    .position(Coord)
                    .icon(BitmapDescriptorFactory.fromResource(LocationIcon));
            map.addMarker(x);

        }
    }


    public class getplaces extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlacesOnMap.this);
            progressDialog.setMessage("Fetching data, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
            Log.e("vdslmvdspo", "started");
        }

        protected String doInBackground(String... urls) {
            try {
                Log.e("cbvsbk", id + " ");

                String uri = Constants.apilink +
                        "places-api.php?lat=" +
                        deslat +
                        "&lng=" +
                        deslon +
                        "&mode=" +
                        mode;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                Log.e("here", uri + " ");
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




                JSONArray YTFeedItems = YTFeed.getJSONArray("results");
                Log.e("response", YTFeedItems + " ");





                lv.setAdapter(new City_info_adapter(PlacesOnMap.this, YTFeedItems, icon));

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("vsdfkvaes", e.getMessage() + " dsv");
            }
            progressDialog.dismiss();
        }
    }

    public class City_info_adapter extends BaseAdapter {

        Context context;
        JSONArray FeedItems;
        int rd;
        LinearLayout b2;
        private LayoutInflater inflater = null;

        public City_info_adapter(Context context, JSONArray FeedItems, int r) {
            this.context = context;
            this.FeedItems = FeedItems;
            rd = r;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return FeedItems.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            try {
                return FeedItems.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.city_infoitem, null);

            TextView Title = (TextView) vi.findViewById(R.id.item_name);
            TextView Description = (TextView) vi.findViewById(R.id.item_address);
            LinearLayout onmap = (LinearLayout) vi.findViewById(R.id.map);
            b2 = (LinearLayout) vi.findViewById(R.id.b2);


            try {
                Title.setText(FeedItems.getJSONObject(position).getString("name"));
                Description.setText(FeedItems.getJSONObject(position).getString("address"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("eroro", e.getMessage() + " ");
            }

            ImageView iv = (ImageView) vi.findViewById(R.id.image);
            iv.setImageResource(rd);




            onmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                                FeedItems.getJSONObject(position).getString("name") +
                                "+(name)+@" +
                                FeedItems.getJSONObject(position).getString("lat") +
                                "," +
                                FeedItems.getJSONObject(position).getString("lng")
                        ));
                        context.startActivity(browserIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });


            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent browserIntent = null;
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/"

                    ));
                    context.startActivity(browserIntent);


                }
            });


            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    map.clear();
                    try {
                        ShowMarker(Double.parseDouble(FeedItems.getJSONObject(position).getString("lat")),
                                Double.parseDouble(FeedItems.getJSONObject(position).getString("lng")),
                                FeedItems.getJSONObject(position).getString("name"),
                                R.drawable.ic_pin_drop_black_24dp
                                );




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


            return vi;
        }

    }


}
