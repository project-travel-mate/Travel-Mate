package tie.hackathon.travelguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;
import adapters.City_info_adapter;
import adapters.Shop_adapter;

public class Shopping_currentcity extends AppCompatActivity {

    SharedPreferences s ;
    SharedPreferences.Editor e;
    ProgressBar pb;
    ListView lv;
    String item="bags";
    EditText q;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_currentcity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        lv = (ListView) findViewById(R.id.music_list);
        pb = (ProgressBar) findViewById(R.id.pb);
        setTitle("Shopping");
        q = (EditText) findViewById(R.id.query);
        ok = (Button) findViewById(R.id.go);

        new Book_RetrieveFeed().execute();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("vfs", "clcike");
                pb.setVisibility(View.VISIBLE);
                try {
                    item = q.getText().toString();

                    Log.e("click", "going" + item);
                    new Book_RetrieveFeed().execute();


                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Shopping_currentcity.this).create();
                    alertDialog.setTitle("Can't connect.");
                    alertDialog.setMessage("We cannot connect to the internet right now. Please try again later.");
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
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() ==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
    public class Book_RetrieveFeed extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("vdslmvdspo", "started");
        }

        protected String doInBackground(String... urls) {
            try {
                String uri = "http://csinsit.org/prabhakar/tie/online-shopping.php?string="+item;
                uri = uri.replace(" ","+");
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                Log.e("here",uri +" ");
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
                if(YTFeedItems.length()==0){
                    Utils.hideKeyboard(Shopping_currentcity.this);
                    Snackbar.make(pb, "No results found", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                pb.setVisibility(View.GONE);
                lv.setAdapter(new Shop_adapter(Shopping_currentcity.this , YTFeedItems) );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
