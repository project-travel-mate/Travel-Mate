package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import io.github.project_travel_mate.destinations.CityFragment;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.DESTINATION_CITY;
import static utils.Constants.DESTINATION_CITY_ID;
import static utils.Constants.DESTINATION_CITY_LAT;
import static utils.Constants.DESTINATION_CITY_LON;
import static utils.Constants.HERE_API_APP_CODE;
import static utils.Constants.HERE_API_APP_ID;
import static utils.Constants.HERE_API_LINK;
import static utils.Constants.MUMBAI_LAT;
import static utils.Constants.MUMBAI_LON;
import static utils.Constants.SOURCE_CITY;
import static utils.Constants.SOURCE_CITY_ID;

/**
 * Display list of hotels in destination city
 */
public class Hotels extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        View.OnClickListener {

    private static final String DATEPICKER_TAG = "datepicker";
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.music_list)
    ListView lv;
    @BindView(R.id.seldate)
    TextView selectdate;
    @BindView(R.id.city)
    TextView city;
    Toolbar toolbar;

    private String mSource;
    private String mDestination;
    private String mSourceText;
    private String mDestinationText;
    private String mDeslat;
    private String mDeslon;
    private String mDate = "17-October-2015";
    private SharedPreferences mSharedPreferences;
    private Handler mHandler;
    private DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler = new Handler(Looper.getMainLooper());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSource = mSharedPreferences.getString(SOURCE_CITY_ID, "1");
        mDestination = mSharedPreferences.getString(DESTINATION_CITY_ID, "1");
        mSourceText = mSharedPreferences.getString(SOURCE_CITY, "Delhi");
        mDestinationText = mSharedPreferences.getString(DESTINATION_CITY, "Mumbai");
        mDeslat = mSharedPreferences.getString(DESTINATION_CITY_LAT, MUMBAI_LAT);
        mDeslon = mSharedPreferences.getString(DESTINATION_CITY_LON, MUMBAI_LON);

        String cityText = "Showing " + mDestinationText + " hotels";
        String selectDateText = "Check In : " + mDate;
        city.setText(cityText);
        selectdate.setText(selectDateText);

        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                isVibrate());

        getHotellist();

        setTitle("Hotels");

        selectdate.setOnClickListener(this);
        city.setOnClickListener(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
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
        mDate = day + "-";
        String monthString = new DateFormatSymbols().getMonths()[month];
        mDate = mDate + monthString;
        mDate = mDate + "-" + year;
        String selectDateText = "Check In : " + mDate;
        selectdate.setText(selectDateText);

        getHotellist();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
    }

    /**
     * Calls API to get hotel list
     */
    private void getHotellist() {

        pb.setVisibility(View.VISIBLE);
        String uri = HERE_API_LINK + "?at=" + mDeslat + "," + mDeslon + "&cat=accomodation&app_id=" +
                HERE_API_APP_ID + "&app_code=" + HERE_API_APP_CODE;


        Log.v("EXECUTING", uri);

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
                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    try {
                        JSONObject json = new JSONObject(res);
                        Log.v("Response", res);
                        json = json.getJSONObject("results");
                        JSONArray feedItems = json.getJSONArray("items");

                        Log.v("response", feedItems + " ");
                        pb.setVisibility(View.GONE);
                        if (feedItems.length() > 0) {
                            lv.setAdapter(new HotelsAdapter(Hotels.this, feedItems));
                        } else {
                            Toast.makeText(Hotels.this,
                                    getResources().getString(R.string.no_hotels),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    pb.setVisibility(View.GONE);

                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSource = mSharedPreferences.getString(SOURCE_CITY_ID, "1");
        mDestination = mSharedPreferences.getString(DESTINATION_CITY_ID, "1");
        mSourceText = mSharedPreferences.getString(SOURCE_CITY, "Delhi");
        mDestinationText = mSharedPreferences.getString(DESTINATION_CITY, "Mumbai");
        String cityText = "Showing " + mDestinationText + " hotels";
        city.setText(cityText);
        getHotellist();
    }

    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city:

                Intent i = new Intent(Hotels.this, CityFragment.class);
                startActivity(i);

               //TODO :: show a dialog with list of cities

                break;
            case R.id.seldate:
                mDatePickerDialog.setVibrate(isVibrate());
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                mDatePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                break;
        }
    }

    class HotelsAdapter extends BaseAdapter {

        final Context mContext;
        final JSONArray mFeedItems;
        private final LayoutInflater mInflater;

        HotelsAdapter(Context context, JSONArray feedItems) {
            this.mContext = context;
            this.mFeedItems = feedItems;

            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mFeedItems.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return mFeedItems.getJSONObject(position);
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

            ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = mInflater.inflate(R.layout.hotel_listitem, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }

            try {
                holder.title.setText(mFeedItems.getJSONObject(position).getString("name"));
                holder.description.setText(mFeedItems.getJSONObject(position).getString("address"));
                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        try {
                            intent.setData(Uri.parse("tel:" +
                                    mFeedItems.getJSONObject(position).optString("phone", "000")));
                            mContext.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder.map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent;
                        try {
                            Double latitude = Double.parseDouble(
                                    mFeedItems.getJSONObject(position).getJSONArray("position").get(0).toString());
                            Double longitude = Double.parseDouble(
                                    mFeedItems.getJSONObject(position).getJSONArray("position").get(1).toString());

                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps?q=" +
                                    mFeedItems.getJSONObject(position).getString("title") +
                                    "+(name)+@" + latitude +
                                    "," + longitude));

                            mContext.startActivity(browserIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                holder.book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = null;
                        try {
                            browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(mFeedItems.getJSONObject(position).getString("href")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR : ", "Message : " + e.getMessage());
            }
            return convertView;
        }


        class ViewHolder {
            @BindView(R.id.hotel_name)
            TextView title;
            @BindView(R.id.hotel_address)
            TextView description;
            @BindView(R.id.call)
            LinearLayout call;
            @BindView(R.id.map)
            LinearLayout map;
            @BindView(R.id.book)
            LinearLayout book;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
