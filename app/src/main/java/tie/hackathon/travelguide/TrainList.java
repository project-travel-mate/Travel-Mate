package tie.hackathon.travelguide;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import Util.Constants;
import Util.Utils;

public class TrainList extends AppCompatActivity implements com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    ProgressBar pb;
    ListView lv;
    DatePickerDialog.OnDateSetListener date;
    SharedPreferences s;
    SharedPreferences.Editor e;
    TextView city;
    TextView selectdate;
    String dates = "17-10";
    String source, dest;
    public static final String DATEPICKER_TAG = "datepicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        source = s.getString(Constants.SOURCE_CITY, "delhi");
        dest = s.getString(Constants.DESTINATION_CITY, "mumbai");
        lv = (ListView) findViewById(R.id.music_list);
        pb = (ProgressBar) findViewById(R.id.pb);
        selectdate = (TextView) findViewById(R.id.seldate);
        city = (TextView) findViewById(R.id.city);
        city.setText(source + " to " + dest);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TrainList.this, SelectCity.class);
                startActivity(i);
            }
        });
        selectdate.setText(dates);

        try {
            new Book_RetrieveFeed().execute();

        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Can't connect.");
            alertDialog.setMessage("We cannot connect to the internet right now. Please try again later.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        final Calendar calendar = Calendar.getInstance();
        final com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());


        pb.setVisibility(View.GONE);
        selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        setTitle("Trains");

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
        try {
            new Book_RetrieveFeed().execute();


        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(TrainList.this).create();
            alertDialog.setTitle("Can't connect.");
            alertDialog.setMessage("We cannot connect to the internet right now. Please try again later. Exception e: " + e.toString());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            Log.e("YouTube:", "Cannot fetch " + e.toString());
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }

    public class Book_RetrieveFeed extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                String uri = "http://csinsit.org/prabhakar/tie/get-trains.php?src_city=" +
                        source +
                        "&dest_city=" +
                        dest +
                        "&date=" +
                        dates;

                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                Log.e("here", uri + readStream + " ");
                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String Result) {
            try {
                JSONObject YTFeed = new JSONObject(String.valueOf(Result));
                JSONArray YTFeedItems = YTFeed.getJSONArray("trains");

                Log.e("response", YTFeedItems + " ");
                pb.setVisibility(View.GONE);
                lv.setAdapter(new Train_adapter(TrainList.this, YTFeedItems));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        source = s.getString(Constants.SOURCE_CITY, "delhi");
        dest = s.getString(Constants.DESTINATION_CITY, "mumbai");
        city.setText(source + " to " + dest);

        try {
            new Book_RetrieveFeed().execute();


        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(TrainList.this).create();
            alertDialog.setTitle("Can't connect.");
            alertDialog.setMessage("We cannot connect to the internet right now. Please try again later. Exception e: " + e.toString());
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            Log.e("YouTube:", "Cannot fetch " + e.toString());
        }
    }

    public class Train_adapter extends BaseAdapter {

        Context context;
        JSONArray FeedItems;
        private  LayoutInflater inflater = null;

        public Train_adapter(Context context, JSONArray FeedItems) {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("eroro", e.getMessage() + " ");
            }


            return vi;
        }

    }
}
