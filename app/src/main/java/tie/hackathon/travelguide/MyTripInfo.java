package tie.hackathon.travelguide;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Util.Utils;
import objects.NestedListView;

public class MyTripInfo extends AppCompatActivity {
    String id, title, start, end, city, friendid;
    Intent i;
    MaterialDialog dialog;
    ImageView iv;
    TextView tite, date;
    FlatButton click, add;
    List<String> fname;
    NestedListView lv;
    AutoCompleteTextView frendname;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    String nameyet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_info);
        i = getIntent();
        id = i.getStringExtra("_id");
        iv = (ImageView) findViewById(R.id.image);
        tite = (TextView) findViewById(R.id.head);
        date = (TextView) findViewById(R.id.time);
        click = (FlatButton) findViewById(R.id.click);
        lv = (NestedListView) findViewById(R.id.friendlist);
        add = (FlatButton) findViewById(R.id.newfrriend);
        frendname = (AutoCompleteTextView) findViewById(R.id.fname);
        fname = new ArrayList<>();


        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                


            }
        });


        frendname.setThreshold(1);
        frendname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameyet = frendname.getText().toString();
                if (!nameyet.contains(" ")) {
                    Log.e("name", nameyet + " ");
                    new friendautocomplete().execute();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addfriend().execute();
            }
        });


        new mytrip().execute();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    class mytrip extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(MyTripInfo.this)
                    .title("Travel Mate")
                    .content("Fetching trips...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(String... urls) {


            String readStream = null;
            try {
                String uri = "http://csinsit.org/prabhakar/tie/trip/get-one.php?trip=" + id;
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
            Log.e("YO", "Done");
            JSONObject ob;

            try {
                ob = new JSONObject(result);
                title = ob.getString("title");
                start = ob.getString("start_time");
                end = ob.getString("end_time");
                city = ob.getString("city");

                tite.setText(title + " - " + city);
                final Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(start) * 1000);
                final int minutes = cal.get(Calendar.MINUTE);
                final String timeString =
                        new SimpleDateFormat("dd-MMM").format(cal.getTime());
                date.setText(timeString);

                JSONArray arrr = ob.getJSONArray("users");
                for (int i = 0; i < arrr.length(); i++) {
                    fname.add(arrr.getJSONObject(i).getString("name"));

                    Log.e("fvdvdf", "adding " + arrr.getJSONObject(i).getString("name"));
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_layout, fname);
                lv.setAdapter(dataAdapter);


            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            dialog.dismiss();


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    //collegename autocomplete
    class friendautocomplete extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {


            String readStream = null;
            try {
                String uri = "http://csinsit.org/prabhakar/tie/users/find.php?search=" + nameyet.trim();
                Log.e("executing", uri + " ");
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                readStream = Utils.readStream(con.getInputStream());
                Log.e("executing", readStream);
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
                Log.e("erro", result + " ");

                list = new ArrayList<String>();
                list1 = new ArrayList<String>();
                for (int i = 0; i < arr.length(); i++) {
                    try {
                        list.add(arr.getJSONObject(i).getString("name"));
                        list1.add(arr.getJSONObject(i).getString("id"));
                        Log.e("adding", "aff");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error ", " " + e.getMessage());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_layout, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                frendname.setThreshold(1);
                frendname.setAdapter(dataAdapter);
                frendname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        Log.e("jkjb", "uihgiug" + arg2);

                        friendid = list1.get(arg2).toString();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("erro", e.getMessage() + " ");
            }

        }
    }


    class addfriend extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(MyTripInfo.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(String... urls) {


            String readStream = null;
            try {
                String uri = "http://csinsit.org/prabhakar/tie/trip/add-user.php?user=" +
                        friendid +
                        "&trip=" +
                        id;

                Log.e("Bfbsd", uri + " ");
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
            Toast.makeText(MyTripInfo.this, "Friend added", Toast.LENGTH_LONG).show();
            finish();
            dialog.dismiss();


        }
    }


}
