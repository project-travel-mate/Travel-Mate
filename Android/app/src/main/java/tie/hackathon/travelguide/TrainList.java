package tie.hackathon.travelguide;

import android.app.DatePickerDialog;
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

public class TrainList extends AppCompatActivity implements com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,View.OnClickListener {

    @BindView(R.id.pb)          ProgressBar     pb;
    @BindView(R.id.music_list)  ListView        lv;
    @BindView(R.id.city)        TextView        city;
    @BindView(R.id.seldate)     TextView        selectdate;

    private String dates = "17-10";
    private String source;
    private String dest;

    private static final String DATEPICKER_TAG = "datepicker";
    private SharedPreferences s;
    private SharedPreferences.Editor e;
    private Handler mHandler;
    private com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mHandler    = new Handler(Looper.getMainLooper());
        s           = PreferenceManager.getDefaultSharedPreferences(this);
        e           = s.edit();
        source      = s.getString(Constants.SOURCE_CITY, "delhi");
        dest        = s.getString(Constants.DESTINATION_CITY, "mumbai");

        city.setText(source + " to " + dest);
        selectdate.setText(dates);

        getTrainlist();

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());

        pb.setVisibility(View.GONE);

        setTitle("Trains");

        city.setOnClickListener(this);
        selectdate.setOnClickListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int day) {
        month++;
        dates = day + "-" + month;
        selectdate.setText(dates);
        getTrainlist();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {}

    /**
     * Calls API to get train list
     */
    private void getTrainlist() {

        pb.setVisibility(View.VISIBLE);
        String uri = Constants.apilink +
                "get-trains.php?src_city=" +
                source +
                "&dest_city=" +
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
                            JSONArray YTFeedItems = YTFeed.getJSONArray("trains");

                            Log.e("response", YTFeedItems + " ");
                            pb.setVisibility(View.GONE);
                            lv.setAdapter(new Train_adapter(TrainList.this, YTFeedItems));
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
        source = s.getString(Constants.SOURCE_CITY, "delhi");
        dest = s.getString(Constants.DESTINATION_CITY, "mumbai");
        city.setText(source + " to " + dest);

        getTrainlist();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.city :
                Intent i = new Intent(TrainList.this, SelectCity.class);
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

    public class Train_adapter extends BaseAdapter {

        final Context context;
        final JSONArray FeedItems;
        private LayoutInflater inflater = null;

        Train_adapter(Context context, JSONArray FeedItems) {
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
                vi = inflater.inflate(R.layout.train_listitem, null);

            TextView Title = (TextView) vi.findViewById(R.id.bus_name);
            TextView Description = (TextView) vi.findViewById(R.id.bustype);
            TextView atime = (TextView) vi.findViewById(R.id.arr);
            TextView dtime = (TextView) vi.findViewById(R.id.dep);
            Button more = (Button) vi.findViewById(R.id.more);
            Button book = (Button) vi.findViewById(R.id.book);
            TextView d0, d1, d2, d3, d4, d5, d6;
            d0 = (TextView) vi.findViewById(R.id.d0);
            d1 = (TextView) vi.findViewById(R.id.d1);
            d2 = (TextView) vi.findViewById(R.id.d2);
            d3 = (TextView) vi.findViewById(R.id.d3);
            d4 = (TextView) vi.findViewById(R.id.d4);
            d5 = (TextView) vi.findViewById(R.id.d5);
            d6 = (TextView) vi.findViewById(R.id.d6);

            try {
                Title.setText(FeedItems.getJSONObject(position).getString("name"));
                Description.setText("Train Number : " + FeedItems.getJSONObject(position).getString("train_number"));
                atime.setText("Arrival Time : " + FeedItems.getJSONObject(position).getString("arrival_time"));
                dtime.setText("Departure Time : " + FeedItems.getJSONObject(position).getString("departure_time"));

                JSONArray ar = FeedItems.getJSONObject(position).getJSONArray("days");
                for (int i = 0; i < ar.length(); i++) {
                    int m = ar.getInt(i);
                    if (m == 1)
                        continue;
                    switch (i) {

                        case 0:
                            d0.setText("N");
                            d0.setBackgroundResource(R.color.red);
                            break;
                        case 1:
                            d1.setText("N");
                            d1.setBackgroundResource(R.color.red);
                            break;
                        case 2:
                            d2.setText("N");
                            d2.setBackgroundResource(R.color.red);
                            break;
                        case 3:
                            d3.setText("N");
                            d3.setBackgroundResource(R.color.red);
                            break;
                        case 4:
                            d4.setText("N");
                            d4.setBackgroundResource(R.color.red);
                            break;
                        case 5:
                            d5.setText("N");
                            d5.setBackgroundResource(R.color.red);
                            break;
                        case 6:
                            d6.setText("N");
                            d6.setBackgroundResource(R.color.red);
                            break;
                    }

                }

                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = null;
                        try {
                            browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.cleartrip.com/trains/" +
                                            FeedItems.getJSONObject(position).getString("train_number")));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        context.startActivity(browserIntent);

                    }
                });
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try {
                            intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.cleartrip.com/trains/" +
                                            FeedItems.getJSONObject(position).getString("train_number")));
                            context.startActivity(intent);
                        } catch (JSONException e12) {
                            e12.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR : ", e.getMessage() + " ");
            }
            return vi;
        }

    }
}
