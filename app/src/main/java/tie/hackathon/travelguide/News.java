package tie.hackathon.travelguide;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import Util.Utils;
import adapters.Books_adapter;
import adapters.News_adapter;

public class News extends AppCompatActivity {



    ProgressBar pb;
    ListView lv;
    String query = "tech";
    EditText q;
    Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        lv = (ListView) findViewById(R.id.music_list);
        pb = (ProgressBar) findViewById(R.id.pb);
        q = (EditText) findViewById(R.id.query);
        ok = (Button) findViewById(R.id.go);


        pb.setVisibility(View.GONE);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("vfs","clcike");
                pb.setVisibility(View.VISIBLE);
                try {
                    query = q.getText().toString();

                    Log.e("click","going"+query);
                    new Book_RetrieveFeed().execute();


                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(News.this).create();
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
        new Book_RetrieveFeed().execute();

        setTitle("News");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
                String uri = "https://ajax.googleapis.com/ajax/services/feed/find?v=1.0&q="+query;
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
                JSONArray YTFeedItems = YTFeed.getJSONObject("responseData").getJSONArray("entries");
                Log.e("response",YTFeedItems+" ");
                if(YTFeedItems.length()==0){
                    Utils.hideKeyboard(News.this);
                    Snackbar.make(pb, "No results found", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }       pb.setVisibility(View.GONE);
             lv.setAdapter(new News_adapter(News.this , YTFeedItems) );
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
