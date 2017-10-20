package tie.hackathon.travelguide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * If estimote beacon is detected, this activity is opened up
 */
public class DetectedBeacon extends AppCompatActivity {

    private String major;
    private String name;
    private String des;
    private String image;
    private String cname;
    private String cid;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_beacon);

        Intent intent = getIntent();
        major = intent.getStringExtra(Constants.CUR_MAJOR);
        Log.e("Detected Beacon : ", major + " ");

        mHandler = new Handler(Looper.getMainLooper());
        getCity(); // Get city name from latitude longitude

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getCity() {
        // to fetch city name
        String uri = Constants.apilink + "estimote_monuments/get_info.php?id=" + major;
        Log.e("executing", uri + " ");

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
                        try {
                            final JSONObject json = new JSONObject(res);
                            name = json.getString("monument_name");
                            des = json.getString("monument_description");
                            image = json.getString("monument_image");
                            cname = json.getString("city_name");
                            cid = json.getString("city_id");

                            TextView tv = (TextView) findViewById(R.id.tv);
                            tv.setText(des);
                            tv = (TextView) findViewById(R.id.head);
                            tv.setText(name);

                            ImageView iv = (ImageView) findViewById(R.id.imag);
                            Picasso.with(DetectedBeacon.this)
                                    .load(image)
                                    .error(R.drawable.delhi)
                                    .placeholder(R.drawable.delhi)
                                    .into(iv);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("EXCEPTION : ", e.getMessage() + " ");
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
}
