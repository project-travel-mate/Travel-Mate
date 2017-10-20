package tie.hackathon.travelguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

@SuppressWarnings("WeakerAccess")
public class SelectCity extends AppCompatActivity {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.source) Spinner source;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.destination) Spinner dest;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.pb) ProgressBar pb;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.ok) Button ok;
    @SuppressWarnings("WeakerAccess")
    private
    SharedPreferences s;
    @SuppressWarnings("WeakerAccess")
    private
    String[] cities;
    @SuppressWarnings("WeakerAccess")
    private
    SharedPreferences.Editor e;
    @SuppressWarnings("WeakerAccess")
    private final
    List<String> id = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    private final
    List<String> names = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    private final
    List<String> lat = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    private final
    List<String> lon = new ArrayList<>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();
        mHandler = new Handler(Looper.getMainLooper());


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sposition = source.getSelectedItemPosition();
                int dposition = dest.getSelectedItemPosition();

                if (sposition == dposition) {
                    Snackbar.make(view, "Source and destination cannot be same", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else {
                    e.putString(Constants.DESTINATION_CITY_ID, id.get(dposition));
                    e.putString(Constants.SOURCE_CITY_ID, id.get(sposition));
                    e.putString(Constants.DESTINATION_CITY, names.get(dposition));
                    e.putString(Constants.SOURCE_CITY, names.get(sposition));
                    e.putString(Constants.DESTINATION_CITY_LAT, lat.get(dposition));
                    e.putString(Constants.SOURCE_CITY_LAT, lat.get(sposition));
                    e.putString(Constants.DESTINATION_CITY_LON, lon.get(dposition));
                    e.putString(Constants.SOURCE_CITY_LON, lon.get(sposition));
                    SelectCity.this.startService(new Intent(SelectCity.this, LocationService.class));

                    e.apply();
                    SelectCity.this.finish();
                }
            }
        });

        getcitytask();


        getSupportActionBar().setTitle(" ");

    }



    @SuppressWarnings("WeakerAccess")
    private void getcitytask() {

        // to fetch city names
        String uri = Constants.apilink + "all-cities.php";
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

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(response.body().string());
                            JSONArray ar = ob.getJSONArray("cities");
                            for (int i = 0; i < ar.length(); i++) {
                                id.add(ar.getJSONObject(i).getString("id"));
                                names.add(ar.getJSONObject(i).getString("name"));
                                lat.add(ar.getJSONObject(i).getString("lat"));
                                lon.add(ar.getJSONObject(i).getString("lng"));

                            }
                            cities = new String[id.size()];
                            cities = names.toArray(cities);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectCity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                            source.setAdapter(adapter);
                            dest.setAdapter(adapter);
                            pb.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("erro", e.getMessage() + " ");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

            }
        });
    }
}
