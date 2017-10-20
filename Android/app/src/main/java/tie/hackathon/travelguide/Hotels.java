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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Display list of hotels in destination city
 */
public class Hotels extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,View.OnClickListener {

    @BindView(R.id.pb)          ProgressBar pb;
    @BindView(R.id.music_list)  ListView    lv;
    @BindView(R.id.seldate)     TextView    selectdate;
    @BindView(R.id.city)        TextView    city;

    private String source;
    private String dest;
    private String sourcet;
    private String destt;
    private String dates = "17-October-2015";

    DatePickerDialog.OnDateSetListener date;
    private static final String DATEPICKER_TAG = "datepicker";
    private SharedPreferences s;
    private Handler mHandler;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler    = new Handler(Looper.getMainLooper());
        s           = PreferenceManager.getDefaultSharedPreferences(this);
        source      = s.getString(Constants.SOURCE_CITY_ID, "1");
        dest        = s.getString(Constants.DESTINATION_CITY_ID, "1");
        sourcet     = s.getString(Constants.SOURCE_CITY, "Delhi");
        destt       = s.getString(Constants.DESTINATION_CITY, "Mumbai");

        city.setText("Showing " + destt + " hotels");
        selectdate.setText("Check In : " + dates);

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());

        getHotellist();

        setTitle("Hotels");

        selectdate.setOnClickListener(this);
        city.setOnClickListener(this);

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
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        dates = day + "-";
        String monthString;
        switch (month + 1) {
            case 1: monthString = "January";    break;
            case 2: monthString = "February";   break;
            case 3: monthString = "March";      break;
            case 4: monthString = "April";      break;
            case 5: monthString = "May";        break;
            case 6: monthString = "June";       break;
            case 7: monthString = "July";       break;
            case 8: monthString = "August";     break;
            case 9: monthString = "September";  break;
            case 10:monthString = "October";    break;
            case 11:monthString = "November";   break;
            case 12:monthString = "December";   break;
            default:monthString = "Invalid month";break;
        }

        dates = dates + monthString;
        dates = dates + "-" + year;
        selectdate.setText("Check In : " + dates);

        getHotellist();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {}

    /**
     * Calls API to get hotel list
     */
    private void getHotellist() {

        pb.setVisibility(View.VISIBLE);
        String uri = Constants.apilink +
                "get-city-hotels.php?id=" +
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
                            JSONArray YTFeedItems = YTFeed.getJSONArray("hotels");
                            Log.e("response", YTFeedItems + " ");
                            pb.setVisibility(View.GONE);
                            lv.setAdapter(new Hotels_adapter(Hotels.this, YTFeedItems));
                            pb.setVisibility(View.GONE);
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
        source = s.getString(Constants.SOURCE_CITY_ID, "1");
        dest = s.getString(Constants.DESTINATION_CITY_ID, "1");
        sourcet = s.getString(Constants.SOURCE_CITY, "Delhi");
        destt = s.getString(Constants.DESTINATION_CITY, "Mumbai");

        city.setText("Showing " + destt + " hotels");
        getHotellist();
    }

    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    public class Hotels_adapter extends BaseAdapter {

        final Context context;
        final JSONArray FeedItems;
        private LayoutInflater inflater = null;

        Hotels_adapter(Context context, JSONArray FeedItems) {
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
                vi = inflater.inflate(R.layout.hotel_listitem, null);

            LinearLayout call, map, book;

            TextView Title          = (TextView)        vi.findViewById(R.id.VideoTitle);
            TextView Description    = (TextView)        vi.findViewById(R.id.VideoDescription);
            call                    = (LinearLayout)    vi.findViewById(R.id.call);
            map                     = (LinearLayout)    vi.findViewById(R.id.map);
            book                    = (LinearLayout)    vi.findViewById(R.id.book);

            try {
                Title.setText(FeedItems.getJSONObject(position).getString("name"));
                Description.setText(FeedItems.getJSONObject(position).getString("address"));

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        try {
                            intent.setData(Uri.parse("tel:" + FeedItems.getJSONObject(position).getString("phone")));
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                map.setOnClickListener(new View.OnClickListener() {
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
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = null;
                        try {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FeedItems.getJSONObject(position).getString("websites")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        context.startActivity(browserIntent);

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR : ", e.getMessage() + " ");
            }
            return vi;
        }
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.city :
                Intent i = new Intent(Hotels.this, SelectCity.class);
                startActivity(i);
                break;
            case R.id.seldate :
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
        }
    }
}
