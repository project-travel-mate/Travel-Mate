package tie.hackathon.travelguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;
import adapters.Books_adapter;


public class books_all2_fragment extends Fragment implements View.OnClickListener {


    public static Activity activity;
    SharedPreferences s ;
    SharedPreferences.Editor e;
    ProgressBar pb;
    ListView lv;
    EditText q;
    TextView b1,b2,b3,b4;
    Button ok;
    String mood = "happy";
    public books_all2_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.content_books_fragment_all2, container, false);

        s = PreferenceManager.getDefaultSharedPreferences(activity);
        e = s.edit();
        q = (EditText) v.findViewById(R.id.query);
        ok = (Button) v.findViewById(R.id.go);
        b1= (TextView) v.findViewById(R.id.b1);
        b2= (TextView) v.findViewById(R.id.b2);
        b3= (TextView) v.findViewById(R.id.b3);
        b4= (TextView) v.findViewById(R.id.b4);

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

        lv = (ListView) v.findViewById(R.id.music_list);
        pb = (ProgressBar) v.findViewById(R.id.pb);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("vfs","clcike");
                pb.setVisibility(View.VISIBLE);
                try {
                    mood = q.getText().toString();

                    Log.e("click","going"+mood);
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

            }
        });


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        int sset = 0;
        switch (view.getId()){


            case R.id.b1 :sset=1; mood = "Harry%20Potter";break;
            case R.id.b2 :sset=1; mood = "Dinosaurs";break;
            case R.id.b3 :sset=1; mood = "Joey%20Tribbiani";break;
            case R.id.b4 :sset=1; mood = "Fiction";break;
        }
        if(sset==1){
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
        }
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
                lv.setAdapter(new Books_adapter(activity , YTFeedItems) );
                pb.setVisibility(View.GONE);
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
