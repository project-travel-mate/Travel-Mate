package tie.hackathon.travelguide;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Util.Constants;
import Util.Utils;


public class AddNewTrip extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    AutoCompleteTextView cityname;
    String nameyet,cityid="2";
    FlatButton sdate,edate,ok;
    EditText tname;
    public static final String DATEPICKER_TAG1 = "datepicker1";
    public static final String DATEPICKER_TAG2 = "datepicker2";
    String startdate,enddate,tripname,userid;
    MaterialDialog dialog;

    SharedPreferences s;
    SharedPreferences.Editor e;
    String[] array = { "Paries,France", "PA,United States","Parana,Brazil", "Padua,Italy", "Pasadena,CA,United States"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
        cityname = (AutoCompleteTextView) findViewById(R.id.cityname);
        sdate = (FlatButton) findViewById(R.id.sdate);
        edate = (FlatButton) findViewById(R.id.edate);
        ok = (FlatButton) findViewById(R.id.ok);
        tname = (EditText) findViewById(R.id.tripname);
        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        userid = s.getString(Constants.USER_ID,"1");

        cityname.setThreshold(1);
        cityname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameyet = cityname.getText().toString();
                if (!nameyet.contains(" ")) {
                    Log.e("name",nameyet+" ");
                    new tripautocomplete().execute();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });






        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());


        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG1);



            }
        });

        edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG2);


            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tripname = tname.getText().toString();

                new addtrip().execute();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        if(datePickerDialog.getTag() == DATEPICKER_TAG1){

            Calendar calendar = new GregorianCalendar(year, month, day);
            startdate = Long.toString(calendar.getTimeInMillis()/1000);
        }
        if(datePickerDialog.getTag() == DATEPICKER_TAG2){

            Calendar calendar = new GregorianCalendar(year, month, day);
            enddate = Long.toString(calendar.getTimeInMillis()/1000);
        }

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }


    //collegename autocomplete
    class tripautocomplete extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            String readStream = null;
            try {

                String uri = Constants.apilink + "city/autocomplete.php?search=" + nameyet.trim();
                Log.e("executing",uri+" ");
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                readStream = Utils.readStream(con.getInputStream());
                Log.e("executing",readStream);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return readStream;



        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("YO", "Done");
            JSONArray arr;
            final ArrayList list, list1;
            try {
                arr = new JSONArray(result);
                Log.e("erro",result+" ");

                list = new ArrayList<String>();
                list1 = new ArrayList<String>();
                for (int i = 0; i < arr.length(); i++) {
                    try {
                        list.add(arr.getJSONObject(i).getString("name"));
                        list1.add(arr.getJSONObject(i).getString("id"));
                        Log.e("adding","aff");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error ", " " + e.getMessage());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_layout, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cityname.setThreshold(1);
                cityname.setAdapter(dataAdapter);
                cityname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        Log.e("jkjb", "uihgiug" + arg2);

                        cityid = list1.get(arg2).toString();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("erro",e.getMessage()+" ");
            }

        }
    }




    class addtrip extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(AddNewTrip.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(String... urls) {




            String readStream = null;
            try {
                String uri = Constants.apilink +"trip/add-trip.php?user=" +
                        userid +
                        "&title=" +
                        tripname +
                        "&" +
                        "start_time=" +
                        startdate +
                        "&city=" +
                        cityid;

                Log.e("addinhg",uri+" ");

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
            Log.e("YO", "Done");
            Toast.makeText(AddNewTrip.this,"Trip added",Toast.LENGTH_LONG).show();
            finish();
            dialog.dismiss();


        }
    }



    private boolean isVibrate() {
        return false;
    }

    private boolean isCloseOnSingleTapDay() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}
