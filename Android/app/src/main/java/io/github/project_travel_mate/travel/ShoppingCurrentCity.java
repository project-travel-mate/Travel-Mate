package io.github.project_travel_mate.travel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.project_travel_mate.R;
import utils.Constants;
import utils.Utils;

public class ShoppingCurrentCity extends AppCompatActivity {

    @BindView(R.id.pb) ProgressBar pb;
    @BindView(R.id.music_list) ListView lv;
    @BindView(R.id.query) EditText q;
    @BindView(R.id.go) Button ok;

    private MaterialSearchView searchView;
    private String item = "bags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_currentcity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        setTitle("Shopping");

        new GetShoppingItems().execute();

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Log.e("QUERY ITEM : ", query + " ");
                pb.setVisibility(View.VISIBLE);
                try {
                    item = query;
                    new GetShoppingItems().execute();

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

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @OnClick(R.id.go) void onClick() {
        pb.setVisibility(View.VISIBLE);
        try {
            item = q.getText().toString();
            new GetShoppingItems().execute();
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

    private class GetShoppingItems extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            try {
                String uri = Constants.apilink +
                        "online-shopping.php?string=" + item;
                uri = uri.replace(" ", "+");
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());

                Log.e("here", uri + " ");
                return readStream;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject feed = new JSONObject(String.valueOf(result));
                JSONArray feedItems = feed.getJSONArray("results");
                Log.e("response", feedItems + " ");
                if (feedItems.length() == 0) {
                    Utils.hideKeyboard(ShoppingCurrentCity.this);
                    Snackbar.make(pb, "No results found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                pb.setVisibility(View.GONE);
                lv.setAdapter(new ShoppingAdapter(ShoppingCurrentCity.this , feedItems) );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
