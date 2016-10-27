package tie.hackathon.travelguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Util.Constants;
import Util.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShoppingCurrentcity extends AppCompatActivity {

    SharedPreferences s;
    MaterialSearchView searchView;
    SharedPreferences.Editor e;
    ProgressBar pb;
    ListView lv;
    String item = "bags";
    EditText q;
    Button ok;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_currentcity);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler(Looper.getMainLooper());
        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        lv = (ListView) findViewById(R.id.music_list);
        pb = (ProgressBar) findViewById(R.id.pb);
        setTitle("Shopping");
        q = (EditText) findViewById(R.id.query);
        ok = (Button) findViewById(R.id.go);


        getShoplist();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("vfs", "clcike");
                pb.setVisibility(View.VISIBLE);
                item = q.getText().toString();
                getShoplist();

            }
        });


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Log.e("VDSvsd", query + " ");
                pb.setVisibility(View.VISIBLE);
                getShoplist();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }


    /**
     * Calls API to get bus list
     */
    public void getShoplist() {

        String uri = Constants.apilink +
                "online-shopping.php?string=" + item;

        Log.e("CALLING : ", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
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


                            JSONArray YTFeedItems = YTFeed.getJSONArray("results");
                            Log.e("response", YTFeedItems + " ");
                            if (YTFeedItems.length() == 0) {
                                Utils.hideKeyboard(ShoppingCurrentcity.this);
                                Snackbar.make(pb, "No results found", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }
                            pb.setVisibility(View.GONE);
                            lv.setAdapter(new Shop_adapter(ShoppingCurrentcity.this, YTFeedItems));
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    public class Shop_adapter extends BaseAdapter {

        Context context;
        JSONArray FeedItems;
        private LayoutInflater inflater = null;

        public Shop_adapter(Context context, JSONArray FeedItems) {
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
                vi = inflater.inflate(R.layout.shop_listitem, null);

            TextView Title = (TextView) vi.findViewById(R.id.VideoTitle);
            TextView Description = (TextView) vi.findViewById(R.id.VideoDescription);
            ImageView iv = (ImageView) vi.findViewById(R.id.VideoThumbnail);


            try {
                String x = FeedItems.getJSONObject(position).getString("name");
                x = Html.fromHtml(x).toString();
                Title.setText(x);


                String DescriptionText = FeedItems.getJSONObject(position).getString("value");

                DescriptionText = Html.fromHtml(DescriptionText).toString();
                Description.setText(DescriptionText + " Rs");

                Picasso.with(context).load(FeedItems.getJSONObject(position).getString("image")).into(iv);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent browserIntent = null;
                    try {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FeedItems.getJSONObject(position).getString("url")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    context.startActivity(browserIntent);
                }
            });


            return vi;
        }

    }
}
