package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyTrips extends AppCompatActivity {

    @BindView(R.id.gv) GridView g;

    private MaterialDialog dialog;
    private List<String> id;
    private List<String> name;
    private List<String> image;
    private List<String> start;
    private List<String> end;
    private List<String> tname;
    private String userid;
    private SharedPreferences s;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        ButterKnife.bind(this);

        id      = new ArrayList<>();
        name    = new ArrayList<>();
        tname   = new ArrayList<>();
        image   = new ArrayList<>();
        start   = new ArrayList<>();
        end     = new ArrayList<>();
        s       = PreferenceManager.getDefaultSharedPreferences(this);
        userid  = s.getString(Constants.USER_ID, "1");
        mHandler = new Handler(Looper.getMainLooper());

        id.add("yo");
        name.add("yo");
        tname.add("yo");
        image.add("yo");
        start.add("yo");
        end.add("yo");

        mytrip();

        setTitle("My Trips");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void mytrip() {

        dialog = new MaterialDialog.Builder(MyTrips.this)
                .title(R.string.app_name)
                .content("Fetching trips...")
                .progress(true, 0)
                .show();

        String uri = Constants.apilink + "trip/get-all.php?user=" + userid;
        Log.e("executing", uri + " ");


        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = response.body().string();
                JSONArray arr;
                try {
                    arr = new JSONArray(res);

                    for (int i = 0; i < arr.length(); i++) {
                        id.add(arr.getJSONObject(i).getString("id"));
                        start.add(arr.getJSONObject(i).getString("start_time"));
                        end.add(arr.getJSONObject(i).getString("end_time"));
                        name.add(arr.getJSONObject(i).getString("city"));
                        tname.add(arr.getJSONObject(i).getString("title"));
                        image.add(arr.getJSONObject(i).getString("image"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("erro", e.getMessage() + " ");
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        g.setAdapter(new MyTripsadapter(MyTrips.this, id, name, image, start, end));
                    }
                });

            }
        });
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
        ImageView city;
        TextView cityname, date;
        public MyTripsadapter(Activity context, List<String> id, List<String> name, List<String> image, List<String> start, List<String> end) {
            super(context, R.layout.trip_listitem, id);
            this.context    = context;
            ids             = id;
            this.name       = name;
            this.image      = image;
            this.start      = start;
            this.end        = end;
        }

        @Override
        public View getView(final int position, View view2, ViewGroup parent) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = mInflater.inflate(R.layout.trip_listitem, null);
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
