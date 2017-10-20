package tie.hackathon.travelguide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.net.HttpURLConnection;
import java.net.URL;

import utils.Constants;
import utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingCurrentCity extends AppCompatActivity {

    @BindView(R.id.pb)          ProgressBar     pb;
    @BindView(R.id.music_list)  ListView        lv;
    @BindView(R.id.query)       EditText        q;
    @BindView(R.id.go)          Button          ok;

    private SharedPreferences s ;
    private MaterialSearchView searchView;
    private SharedPreferences.Editor e;
    private String item="bags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_currentcity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        setTitle("Shopping");

        new Book_RetrieveFeed().execute();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Log.e("VDSvsd",query+" ");
                pb.setVisibility(View.VISIBLE);
                try {
                    item = query;
                    Log.e("click", "going" + item);
                    new Book_RetrieveFeed().execute();

                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ShoppingCurrentCity.this).create();
                    alertDialog.setTitle("Can't connect.");
                    alertDialog.setMessage("We cannot connect to the internet right now. Please try again later.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
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

    @OnClick(R.id.go) void onClick(){
        Log.e("vfs", "clcike");
        pb.setVisibility(View.VISIBLE);
        try {
            item = q.getText().toString();

            Log.e("click", "going" + item);
            new Book_RetrieveFeed().execute();


        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(ShoppingCurrentCity.this).create();
            alertDialog.setTitle("Can't connect.");
            alertDialog.setMessage("We cannot connect to the internet right now. Please try again later.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            Log.e("YouTube:", "Cannot fetch " + e.toString());
        }
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

        if(item.getItemId() ==android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private class Book_RetrieveFeed extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("vdslmvdspo", "started");
        }

        protected String doInBackground(String... urls) {
            try {
                String uri = Constants.apilink +
                        "online-shopping.php?string="+item;
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
                    Utils.hideKeyboard(ShoppingCurrentCity.this);
                    Snackbar.make(pb, "No results found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                pb.setVisibility(View.GONE);
                lv.setAdapter(new Shop_adapter(ShoppingCurrentCity.this , YTFeedItems) );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class Shop_adapter extends BaseAdapter {

        final Context context;
        final JSONArray FeedItems;
        private  LayoutInflater inflater = null;

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
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    context.startActivity(browserIntent);
                }
            });
            return vi;
        }
    }
}
