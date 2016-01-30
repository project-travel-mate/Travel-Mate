package tie.hackathon.travelguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import Util.Utils;
import adapters.Books_adapter;
import adapters.CheckList_adapter;
import adapters.mood_adapter;
import database.DBhelp_new;
import database.TableEntry_new;


public class books_all_fragment extends Fragment {


    public static Activity activity;
    SharedPreferences s;
    SharedPreferences.Editor e;
    ProgressBar pb;
    ListView lv;
    String mood = "happy";

    public books_all_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.content_books_fragment_all, container, false);

        s = PreferenceManager.getDefaultSharedPreferences(activity);
        e = s.edit();
        Integer moods = Integer.parseInt(s.getString(Constants.CURRENT_SCORE, "2"));
        ImageView iv = (ImageView) v.findViewById(R.id.bg);
        if (moods > 10) {
            mood = "veryhappy";
            iv.setBackgroundResource(R.color.verhappy);
        } else if (moods > 2) {
            mood = "happy";
            iv.setBackgroundResource(R.color.happy);
        } else if (moods > -2) {
            mood = "normal";
            iv.setBackgroundResource(R.color.normal);
        } else if (moods > -10) {
            mood = "sad";
            iv.setBackgroundResource(R.color.sad);
        } else {
            mood = "verysad";
            iv.setBackgroundResource(R.color.versad);
        }

        lv = (ListView) v.findViewById(R.id.music_list);
        pb = (ProgressBar) v.findViewById(R.id.pb);


        try {
            new Book_RetrieveFeed().execute();


        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
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


        return v;
    }

    public class Book_RetrieveFeed extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String uri = "https://www.googleapis.com/books/v1/volumes?q=" + mood;
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
                Log.e("response", YTFeedItems + " ");
                pb.setVisibility(View.GONE);
                lv.setAdapter(new Books_adapter(activity, YTFeedItems));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


}
