package io.github.project_travel_mate.travel.mytrips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import adapters.NestedListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.github.project_travel_mate.R;
import objects.Trip;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.EXTRA_MESSAGE_TRIP_OBJECT;
import static utils.Constants.STATUS_CODE_OK;
import static utils.Constants.USER_TOKEN;

public class MyTripInfoActivity extends AppCompatActivity {

    public static final int INTENT_REQUEST_GET_IMAGES = 13;
    @BindView(R.id.image)
    ImageView iv;
    @BindView(R.id.head)
    TextView tite;
    @BindView(R.id.time)
    TextView date;
    @BindView(R.id.newfrriend)
    FlatButton add;
    @BindView(R.id.friendlist)
    NestedListView lv;
    @BindView(R.id.fname)
    AutoCompleteTextView frendname;
    private String mFriendid = null;
    private String mNameYet;
    private String mToken;
    private Trip mTrip;
    private List<String> mFname;
    private MaterialDialog mDialog;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_info);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        mTrip = (Trip) intent.getSerializableExtra(EXTRA_MESSAGE_TRIP_OBJECT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sharedPreferences.getString(USER_TOKEN, null);

        List<File> mediaimages = new ArrayList<>();
        List<File> imagesuri = new ArrayList<>();
        mFname = new ArrayList<>();

        Picasso.with(this).load(mTrip.getImage()).error(R.drawable.delhi)
                .placeholder(R.drawable.delhi).into(iv);

        mHandler = new Handler(Looper.getMainLooper());

        frendname.setThreshold(1);

        getSingleTrip();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnTextChanged(R.id.fname)
    void onTextChanged() {
        mNameYet = frendname.getText().toString();
        if (!mNameYet.contains(" ") && mNameYet.length() % 3 == 0) {
            friendautocomplete();
        }
    }

    @OnClick(R.id.newfrriend)
    void onClick() {
        if (mFriendid == null) {
            Toast.makeText(MyTripInfoActivity.this,
                    getResources().getString(R.string.no_friend_selected),
                    Toast.LENGTH_LONG).show();
        } else {
            addfriend();
        }
    }

    private void getSingleTrip() {

        mDialog = new MaterialDialog.Builder(MyTripInfoActivity.this)
                .title(R.string.app_name)
                .content(R.string.progress_fetching_trip)
                .progress(true, 0)
                .show();

        // to fetch mCity names
        String uri = API_LINK_V2 + "get-trip/" + mTrip.getId();

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
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

                final String res = Objects.requireNonNull(response.body()).string();
                mHandler.post(() -> {
                    JSONObject ob;
                    try {
                        ob = new JSONObject(res);
                        String title = ob.getString("trip_name");
                        String start = ob.getString("start_date_tx");
                        String end = ob.optString("end_date", null);
                        String city = ob.getJSONObject("city").getString("city_name");

                        tite.setText(city);
                        tite = findViewById(R.id.tname);
                        tite.setText(title);
                        final Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(Long.parseLong(start) * 1000);
                        final String timeString =
                                getResources().getString(R.string.text_started_on) +
                                        new SimpleDateFormat("dd-MMM", Locale.US).format(cal.getTime());
                        date.setText(timeString);

                        JSONArray usersArray = ob.getJSONArray("users");
                        for (int i = 0; i < usersArray.length(); i++) {
                            mFname.add(usersArray.getJSONObject(i).getString("first_name"));
                        }

                        MyTripFriendnameAdapter dataAdapter = new MyTripFriendnameAdapter(
                                MyTripInfoActivity.this, mFname);
                        lv.setAdapter(dataAdapter);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    mDialog.dismiss();
                    mFriendid = null;
                });

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {
            ArrayList<Uri> imageUris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            Toast.makeText(MyTripInfoActivity.this, "Images added", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void friendautocomplete() {

        String uri = API_LINK_V2 + "get-user/" + mNameYet.trim();
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        final Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) {

                mHandler.post(() -> {
                    JSONArray arr;
                    final ArrayList<String> id, email;
                    try {
                        String result = response.body().string();
                        Log.e("RES", result);
                        if (response.body() == null)
                            return;
                        arr = new JSONArray(result);

                        id = new ArrayList<>();
                        email = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                id.add(arr.getJSONObject(i).getString("id"));
                                email.add(arr.getJSONObject(i).getString("username"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ERROR ", "Message : " + e.getMessage());
                            }
                        }
                        ArrayAdapter<String> dataAdapter =
                                new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_layout, email);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        frendname.setAdapter(dataAdapter);
                        frendname.setOnItemClickListener((arg0, arg1, arg2, arg3) -> mFriendid = id.get(arg2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR", "Message : " + e.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
        });
    }

    private void addfriend() {

        mDialog = new MaterialDialog.Builder(MyTripInfoActivity.this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();

        String uri = API_LINK_V2 + "add-friend-to-trip/" + mTrip.getId() + "/" + mFriendid;

        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String res = response.body().string();
                    final int responseCode = response.code();
                    mHandler.post(() -> {
                        if (responseCode == STATUS_CODE_OK) {
                            Toast.makeText(MyTripInfoActivity.this, R.string.friend_added, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MyTripInfoActivity.this, res, Toast.LENGTH_LONG).show();
                        }
                        mDialog.dismiss();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Intent getStartIntent(Context context, Trip trip) {
        Intent intent = new Intent(context, MyTripInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE_TRIP_OBJECT, trip);
        return intent;
    }
}
