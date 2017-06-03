package tie.hackathon.travelguide;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Initiates login
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.signup)              TextView        signup;
    @BindView(R.id.login)               TextView        login;
    @BindView(R.id.signup_layout)       LinearLayout    sig;
    @BindView(R.id.loginlayout)         LinearLayout    log;
    @BindView(R.id.input_num_login)     EditText        num_login;
    @BindView(R.id.input_pass_login)    EditText        pass_login;
    @BindView(R.id.input_num_signup)    EditText        num_signup;
    @BindView(R.id.input_pass_signup)   EditText        pass_signup;
    @BindView(R.id.input_name_signup)   EditText        name;
    @BindView(R.id.ok_login)            FlatButton      ok_login;
    @BindView(R.id.ok_signup)           FlatButton      ok_signup;

    private String Num;
    private String Pass;
    private String Name;

    private SharedPreferences   sharedPreferences;
    private MaterialDialog      dialog;
    private Handler             mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ButterKnife.setDebug(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Initialization
        mHandler = new Handler(Looper.getMainLooper());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get runtime permissions for Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.VIBRATE,

                }, 0);
            }
        }

        // If user is already logged in, open MainActivity
        if (sharedPreferences.getString(Constants.USER_ID, null) != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        ok_login.setOnClickListener(this);
        ok_signup.setOnClickListener(this);
    }

    /**
     * Calls Login API and checks for validity of credentials
     * If yes, transfer to MainActivity
     *
     * @param num  user's phone number
     * @param pass password user entered
     */
    private void login(final String num, String pass) {

        dialog = new MaterialDialog.Builder(LoginActivity.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

        String uri = Constants.apilink + "users/login.php?contact=" + num + "&password=" + pass;
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
                            JSONObject ob = new JSONObject(res);
                            Boolean success = ob.getBoolean("success");
                            if (success) {
                                JSONObject o = ob.getJSONObject("user_id");
                                String id = o.getString("id");
                                String name = o.getString("name");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.USER_ID, id);
                                editor.putString(Constants.USER_NAME, name);
                                editor.putString(Constants.USER_NUMBER, num);
                                editor.apply();

                                Log.e("LOGIN : ", "id id" + id + name);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Invalid Password or number", Toast.LENGTH_LONG)
                                        .show();
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage() + " ");
                        }
                    }
                });

            }
        });
    }

    /**
     * Calls Signup API
     *
     * @param name user's name
     * @param num  user's phone number
     * @param pass password user entered
     */
    private void signup(final String name, final String num, String pass) {

        dialog = new MaterialDialog.Builder(LoginActivity.this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();

        String uri = Constants.apilink + "users/signup.php?name=" + name + "&contact=" + num + "&password=" + pass;
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
                            JSONObject ob = new JSONObject(res);
                            String id = ob.getString("user_id");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.USER_ID, id);
                            editor.putString(Constants.USER_NAME, name);
                            editor.putString(Constants.USER_NUMBER, num);
                            editor.apply();

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR : ", e.getMessage() + " ");
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            // Open signup
            case R.id.signup :
                log.setVisibility(View.GONE);
                sig.setVisibility(View.VISIBLE);
                break;
            // Open login
            case R.id.login :
                log.setVisibility(View.VISIBLE);
                sig.setVisibility(View.GONE);
                break;
            // Call login
            case R.id.ok_login :
                Num = num_login.getText().toString();
                Pass = pass_login.getText().toString();
                login(Num, Pass);
                break;
            // Call signup
            case R.id.ok_signup :
                Num = num_signup.getText().toString();
                Pass = pass_signup.getText().toString();
                Name = name.getText().toString();
                signup(Name, Num, Pass);
                break;
        }
    }
}
