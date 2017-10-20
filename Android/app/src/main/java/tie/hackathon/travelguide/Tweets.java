package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tweets extends AppCompatActivity {

    @BindView(R.id.list) ListView lv;

    private Intent i;
    private String id;
    private String tit;
    private String image;
    private MaterialDialog dialog;
    private List<String> nam;
    private List<String> cou;
    private List<String> lin;
    private Tweetsadapter adapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        ButterKnife.bind(this);

        mHandler = new Handler(Looper.getMainLooper());

        i       = getIntent();
        tit     = i.getStringExtra("name_");
        id      = i.getStringExtra("id_");
        image   = i.getStringExtra("image_");
        nam     = new ArrayList<>();
        cou     = new ArrayList<>();
        lin     = new ArrayList<>();

        setTitle(tit);
        getTweets();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    private void getTweets() {

        dialog = new MaterialDialog.Builder(Tweets.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

        // to fetch city names
        String uri = Constants.apilink + "city/trends/twitter.php?city=" + id;
        Log.e("executing", uri + " ");


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
                        try {
                            //Tranform the string into a json object
                            JSONArray ob = new JSONArray(res);
                            for (int i = 0; i < ob.length(); i++) {
                                nam.add(ob.getJSONObject(i).getString("name"));
                                lin.add(ob.getJSONObject(i).getString("url"));
                                cou.add(ob.getJSONObject(i).getString("tweet_volume"));
                            }

                            adapter = new Tweetsadapter(Tweets.this, nam, cou, lin);
                            lv.setAdapter(adapter);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("erro", e.getMessage() + " ");
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public class Tweetsadapter extends ArrayAdapter<String> {
        private final Activity context;
        private final List<String> name, count, link;

        Tweetsadapter(Activity context, List<String> name, List<String> count, List<String> link) {
            super(context, R.layout.trip_listitem, name);
            this.context = context;
            this.name = name;
            this.count = count;
            this.link = link;

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
                    startActivity(browserIntent);
                }
            });
            return view;
        }

        private class ViewHolder {
            TextView name;
        }
    }

}
