package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Util.Constants;
import Util.Utils;

public class MyTrips extends AppCompatActivity {

    GridView g;
    MaterialDialog dialog;
    List<String> id, name, image, start, end, tname;
    String userid;
    SharedPreferences s;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        id = new ArrayList<>();
        name = new ArrayList<>();
        tname = new ArrayList<>();
        image = new ArrayList<>();
        start = new ArrayList<>();
        end = new ArrayList<>();
        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        userid = s.getString(Constants.USER_ID, "1");
        g = (GridView) findViewById(R.id.gv);

        id.add("yo");
        name.add("yo");
        tname.add("yo");
        image.add("yo");
        start.add("yo");
        end.add("yo");


        new mytrip().execute();

        setTitle("My Trips");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    class mytrip extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(MyTrips.this)
                    .title("Travel Mate")
                    .content("Fetching trips...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(String... urls) {


            String readStream = null;
            try {
                String uri = Constants.apilink +
                        "trip/get-all.php?user=" + userid;
                Log.e("YO", uri);
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                readStream = Utils.readStream(con.getInputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return readStream;


        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("YO", "Done" + result);
            JSONArray arr;
            if(result == null){
                Toast.makeText(MyTrips.this,"Some error",Toast.LENGTH_LONG).show();
                return;
            }


            try {
                arr = new JSONArray(result);

                for (int i = 0; i < arr.length(); i++) {
                    id.add(arr.getJSONObject(i).getString("id"));
                    start.add(arr.getJSONObject(i).getString("start_time"));
                    end.add(arr.getJSONObject(i).getString("end_time"));
                    name.add(arr.getJSONObject(i).getString("city"));
                    tname.add(arr.getJSONObject(i).getString("title"));
                    image.add(arr.getJSONObject(i).getString("image"));

                }


            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            MyTripsadapter ad = new MyTripsadapter(MyTrips.this, id, name, image, start, end);
            g.setAdapter(ad);
            dialog.dismiss();


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    public class MyTripsadapter extends ArrayAdapter<String> {
        private final Activity context;
        private final List<String> ids, name, image, start, end;


        public MyTripsadapter(Activity context, List<String> id, List<String> name, List<String> image, List<String> start, List<String> end) {
            super(context, R.layout.trip_listitem, id);
            this.context = context;
            ids = id;
            this.name = name;
            this.image = image;
            this.start = start;
            this.end = end;


        }


            ImageView city;
            TextView cityname, date;




        @Override
        public View getView(final int position, View view2, ViewGroup parent) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View    view = mInflater.inflate(R.layout.trip_listitem, null);
                city = (ImageView) view.findViewById(R.id.profile_image);
                cityname = (TextView) view.findViewById(R.id.tv);
                date = (TextView) view.findViewById(R.id.date);


            if (position == 0) {
                city.setImageResource(R.drawable.ic_add_circle_black_24dp);
                cityname.setText("Add New Trip");
                date.setText("");

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MyTrips.this, AddNewTrip.class);
                        context.startActivity(i);


                    }
                });

            } else {
               //city.setImageResource(R.drawable.ic_add_circle_black_24dp);


                    Picasso.with(MyTrips.this).load(image.get(position)).placeholder(R.drawable.add_list_item)
                            .into(city);
                    cityname.setText(name.get(position));
                    date.setText(start.get(position));


                    Log.e("time", start.get(position) + " " + image.get(position));
                    final Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(Long.parseLong(start.get(position)) * 1000);
                    final String timeString =
                            new SimpleDateFormat("dd-MMM").format(cal.getTime());
                    date.setText(timeString);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MyTrips.this, MyTripInfo.class);
                            i.putExtra("_id", id.get(position));
                            i.putExtra("_image", image.get(position));
                            startActivity(i);

                        }
                    });


            }


            return view;
        }


    }
}
