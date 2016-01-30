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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;
import adapters.Books_adapter;
import adapters.mood_adapter;

public class Books extends AppCompatActivity {



    SharedPreferences s ;
    SharedPreferences.Editor e;
    ProgressBar pb;
    ListView lv;
    TwoWayView lv2;
    String mood = "happy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);








        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        Integer moods = Integer.parseInt(s.getString(Constants.CURRENT_SCORE,"2"));
        if(moods>10)
            mood = "veryhappy";
        else if(moods>2)
            mood = "happy";
        else if(moods>-2)
            mood = "normal";
        else if(moods>-10)
            mood = "sad";
        else
            mood = "verysad";

        lv = (ListView) findViewById(R.id.music_list);
        lv2 = (TwoWayView) findViewById(R.id.lvmoods);
        pb = (ProgressBar) findViewById(R.id.pb);


        lv2.setAdapter(new mood_adapter(this));
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
            Log.e("YouTube:", "Cannot fetch " + e.toString());
        }

        setTitle("Books");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {

                    case 0:
                        mood = "veryhappy";
                        break;
                    case 1: mood = "happy";
                        break;
                    case 2: mood = "normal";
                        break;
                    case 3: mood = "sad";
                        break;
                    case 4: mood = "verysad";
                        break;
                }

                try {
                    new Book_RetrieveFeed().execute();


                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Books.this).create();
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
        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String uri = "https://www.googleapis.com/books/v1/volumes?q="+mood;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                return readStream;
            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String Result) {
            try {
                JSONObject YTFeed = new JSONObject(String.valueOf(Result));
                JSONArray YTFeedItems = YTFeed.getJSONArray("items");
                Log.e("response",YTFeedItems+" ");
                pb.setVisibility(View.GONE);
             lv.setAdapter(new Books_adapter(Books.this , YTFeedItems) );
                pb.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
