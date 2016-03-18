package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Util.Utils;

public class Tweets extends AppCompatActivity {

    Intent i;
    String id, tit, image,description,icon;
    ListView lv;
    MaterialDialog dialog;
    List<String> nam,cou,lin;
    Tweetsadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        lv  = (ListView) findViewById(R.id.list);


        new cityinfo().execute();

        i = getIntent();
        tit = i.getStringExtra("name_");
        setTitle(tit);
        id = i.getStringExtra("id_");
        image = i.getStringExtra("image_");

        nam = new ArrayList<>();
        cou = new ArrayList<>();
        lin = new ArrayList<>();





        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }




    public class cityinfo extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(Tweets.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("started", "strted");
                String uri = "http://csinsit.org/prabhakar/tie/city/trends/twitter.php?city=" + id;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here", url + readStream + " ");
                return readStream;
            } catch (Exception e) {
                Log.e("here", e.getMessage() + " ");
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null)
                return;

            try {
                //Tranform the string into a json object
                JSONArray ob = new JSONArray(result);
                for(int i=0;i<ob.length();i++){
                    nam.add(ob.getJSONObject(i).getString("name"));
                    lin.add(ob.getJSONObject(i).getString("url"));
                    cou.add(ob.getJSONObject(i).getString("tweet_volume"));
                }

                adapter = new Tweetsadapter(Tweets.this,nam,cou,lin);
                lv.setAdapter(adapter);

            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
            dialog.dismiss();
        }

    }




    public class Tweetsadapter extends ArrayAdapter<String> {
        private final Activity context;
        private final List<String> name,count,link;


        public Tweetsadapter(Activity context, List<String> name,List<String> count,List<String> link) {
            super(context, R.layout.trip_listitem, name);
            this.context = context;
            this.name = name;
            this.count = count;
            this.link = link;

        }

        private class ViewHolder {

            TextView name,count;


        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = mInflater.inflate(R.layout.tweet_listitem, null);
                holder = new ViewHolder();

                holder.name = (TextView) view.findViewById(R.id.tagmain);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();

            holder.name.setText(name.get(position));

           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.get(position)));
                   startActivity(browserIntent);  }
           });
            return view;
        }


    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}
