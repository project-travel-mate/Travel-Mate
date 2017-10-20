package tie.hackathon.travelguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.AdapterView;
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
import org.lucasr.twowayview.TwoWayView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utils.Constants;
import adapters.NestedListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyTripInfo extends AppCompatActivity {

    @BindView(R.id.image)       ImageView iv;
    @BindView(R.id.head)        TextView tite;
    @BindView(R.id.time)        TextView date;
    @BindView(R.id.newfrriend)  FlatButton add;
    @BindView(R.id.lv)          TwoWayView twoway;
    @BindView(R.id.friendlist)  NestedListView lv;
    @BindView(R.id.fname)       AutoCompleteTextView frendname;

    private String id;
    private String title;
    private String start;
    private String end;
    private String city;
    private String friendid;
    private String img;
    private String nameyet;

    private List<String> fname;
    private List<File> imagesuri;
    private List<File> mediaimages;

    private Intent intent;
    private MaterialDialog dialog;
    private Handler mHandler;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private final String mainfolder = "/storage/emulated/0/Pictures/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_info);

        ButterKnife.bind(this);

        intent      = getIntent();
        id          = intent.getStringExtra("_id");
        img         = intent.getStringExtra("_image");

        mediaimages = new ArrayList<>();
        imagesuri   = new ArrayList<>();
        fname       = new ArrayList<>();

        Picasso.with(this).load(img).into(iv);

        mHandler    = new Handler(Looper.getMainLooper());

        File sdDir  = new File(mainfolder);
        File[] sdDirFiles = sdDir.listFiles();
        for (File singleFile : sdDirFiles) {
            if (!singleFile.isDirectory())
                mediaimages.add(singleFile);
        }
        mediaimages.add(null);

        Imagesadapter ad = new Imagesadapter(this, mediaimages);
        twoway.setAdapter(ad);

        frendname.setThreshold(1);

        mytrip();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnTextChanged(R.id.fname) void onTextChanged(){
        nameyet = frendname.getText().toString();
        if (!nameyet.contains(" ")) {
            Log.e("name", nameyet + " ");
            friendautocomplete();
        }
    }

    @OnClick(R.id.newfrriend) void onClick(){
        addfriend();
    }

    private void mytrip() {

        dialog = new MaterialDialog.Builder(MyTripInfo.this)
                .title(R.string.app_name)
                .content("Fetching trips...")
                .progress(true, 0)
                .show();

        // to fetch city names
        String uri = Constants.apilink + "trip/get-one.php?trip=" + id;
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
                        JSONObject ob;
                        try {
                            ob = new JSONObject(res);
                            title = ob.getString("title");
                            start = ob.getString("start_time");
                            end = ob.getString("end_time");
                            city = ob.getString("city");

                            tite.setText(city);
                            tite = (TextView) findViewById(R.id.tname);
                            tite.setText(title);
                            final Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(Long.parseLong(start) * 1000);
                            final String timeString =
                                    new SimpleDateFormat("dd-MMM").format(cal.getTime());
                            date.setText("Started on : " + timeString);

                            JSONArray arrr = ob.getJSONArray("users");
                            for (int i = 0; i < arrr.length(); i++) {
                                fname.add(arrr.getJSONObject(i).getString("name"));

                                Log.e("fvdvdf", "adding " + arrr.getJSONObject(i).getString("name"));
                            }

                            Log.e("vdsv", fname.size() + " ");

                            Friendnameadapter dataAdapter = new Friendnameadapter(MyTripInfo.this, fname);
                            lv.setAdapter(dataAdapter);


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {

            ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            for (int i = 0; i < image_uris.size(); i++) {
                Log.e("cdscsd", image_uris.get(i).getPath());
            }
            Toast.makeText(MyTripInfo.this, "Images added", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void friendautocomplete() {

        String uri = Constants.apilink + "users/find.php?search=" + nameyet.trim();
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
                        Log.e("YO", "Done");
                        JSONArray arr;
                        final ArrayList list, list1;
                        try {
                            arr = new JSONArray(response.body().string());

                            list = new ArrayList<>();
                            list1 = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                try {
                                    list.add(arr.getJSONObject(i).getString("name"));
                                    list1.add(arr.getJSONObject(i).getString("id"));
                                    Log.e("adding", "aff");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("error ", " " + e.getMessage());
                                }
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                                    (getApplicationContext(), R.layout.spinner_layout, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            frendname.setThreshold(1);
                            frendname.setAdapter(dataAdapter);
                            frendname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    // TODO Auto-generated method stub
                                    Log.e("jkjb", "uihgiug" + arg2);

                                    friendid = list1.get(arg2).toString();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("erro", e.getMessage() + " ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private void addfriend() {

        dialog = new MaterialDialog.Builder(MyTripInfo.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

        String uri = Constants.apilink + "trip/add-user.php?user=" + friendid + "&trip=" + id;
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
                        Toast.makeText(MyTripInfo.this, "City added", Toast.LENGTH_LONG).show();
                        finish();
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public class Imagesadapter extends ArrayAdapter<File> {
        private final Activity context;
        private final List<File> name;

        Imagesadapter(Activity context, List<File> name) {
            super(context, R.layout.trip_listitem, name);
            this.context = context;
            this.name = name;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = mInflater.inflate(R.layout.image_listitem, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) view.findViewById(R.id.iv);

                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();
            if (position == name.size() - 1) {
                holder.iv.setImageResource(R.drawable.add_image);
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MyTripInfo.this, ImagePickerActivity.class);
                        startActivityForResult(intent1, INTENT_REQUEST_GET_IMAGES);
                    }
                });
            } else {
                holder.iv.setImageDrawable(Drawable.createFromPath(name.get(position).getAbsolutePath()));
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MyTripInfo.this, EventImage.class);
                        ArrayList<String> a = new ArrayList<>();
                        a.add(name.get(position).getAbsolutePath());

                        i.putExtra(Constants.EVENT_IMG, a);
                        i.putExtra(Constants.EVENT_NAME, "Image");
                        startActivity(i);
                    }
                });
            }
            return view;
        }

        private class ViewHolder {
            ImageView iv;
        }
    }

    public class Friendnameadapter extends ArrayAdapter<String> {
        private final Activity context;
        private final List<String> name;

        public Friendnameadapter(Activity context, List<String> name) {
            super(context, R.layout.friend_listitem, name);
            this.context = context;
            this.name = name;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = mInflater.inflate(R.layout.friend_listitem, null);
                holder = new ViewHolder();
                holder.iv = (TextView) view.findViewById(R.id.name);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();
            holder.iv.setText(name.get(position) + " ");
            return view;
        }

        private class ViewHolder {
            TextView iv;
        }
    }

}
