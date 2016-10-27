package tie.hackathon.travelguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import Util.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Displays a list of available buses
 */
public class BusList extends AppCompatActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String DATEPICKER_TAG = "datepicker";
    ProgressBar pb;
    ListView lv;
    SharedPreferences sharedPreferences;
    TextView selectdate, city;
    String source, dest, dates = "17-October-2015";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler(Looper.getMainLooper());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        source = sharedPreferences.getString(Constants.SOURCE_CITY, "delhi");
        dest = sharedPreferences.getString(Constants.DESTINATION_CITY, "mumbai");
        lv = (ListView) findViewById(R.id.music_list);
        pb = (ProgressBar) findViewById(R.id.pb);
        selectdate = (TextView) findViewById(R.id.seldate);
        city = (TextView) findViewById(R.id.city);

        selectdate.setText(dates);
        city.setText(source + " to " + dest);

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BusList.this, SelectCity.class);
                startActivity(i);
            }
        });


        getBuslist();
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                isVibrate());


        // Select travel date
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        setTitle("Buses");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        // Set date in format 17-October-2016
        dates = day + "-";

        String monthString;
        switch (month + 1) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }

        dates = dates + monthString;
        dates = dates + "-" + year;

        selectdate.setText(dates);
        //Update bus list
        getBuslist();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
    }

    /**
     * Calls API to get bus list
     */
    public void getBuslist() {

        pb.setVisibility(View.VISIBLE);
        String uri = Constants.apilink + "bus-booking.php?src=" +
                source +
                "&dest=" +
                dest +
                "&date=" +
                dates;

        Log.e("CALLING : ", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("RESPONSE : ", "Done");
                        try {
                            JSONObject YTFeed = new JSONObject(String.valueOf(res));
                            JSONArray YTFeedItems = YTFeed.getJSONArray("results");
                            Log.e("response", YTFeedItems + " ");
                            pb.setVisibility(View.GONE);
                            lv.setAdapter(new Bus_adapter(BusList.this, YTFeedItems));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        source = sharedPreferences.getString(Constants.SOURCE_CITY, "delhi");
        dest = sharedPreferences.getString(Constants.DESTINATION_CITY, "mumbai");
        city.setText(source + " to " + dest);
        // Update Bus list
        getBuslist();
    }

    // Sets adapter for bus list
    public class Bus_adapter extends BaseAdapter {

        Context context;
        JSONArray FeedItems;
        private LayoutInflater inflater = null;

        Bus_adapter(Context context, JSONArray FeedItems) {
            this.context = context;
            this.FeedItems = FeedItems;

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
                vi = inflater.inflate(R.layout.bus_listitem, null);

            TextView Title = (TextView) vi.findViewById(R.id.bus_name);
            TextView Description = (TextView) vi.findViewById(R.id.bustype);
            TextView add = (TextView) vi.findViewById(R.id.add);
            Button contact = (Button) vi.findViewById(R.id.call);
            Button url = (Button) vi.findViewById(R.id.book);
            TextView fair = (TextView) vi.findViewById(R.id.fair);


            try {
                Title.setText(FeedItems.getJSONObject(position).getString("name"));
                Description.setText(FeedItems.getJSONObject(position).getString("type"));
                add.setText(FeedItems.getJSONObject(position).getString("dep_add"));

                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        try {
                            intent.setData(Uri.parse("tel:" + FeedItems.getJSONObject(position).getString("contact")));
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = null;
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://redbus.in"));

                        context.startActivity(browserIntent);
                    }
                });

                fair.setText(FeedItems.getJSONObject(position).getString("fair") + " Rs");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR : ", e.getMessage() + " ");
            }
            return vi;
        }
    }
}
