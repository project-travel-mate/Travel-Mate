package tie.hackathon.travelguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Initiates login
 */
public class LoginActivity extends AppCompatActivity {

    TextView signup, login;
    LinearLayout sig, log;
    EditText num_login, pass_login, num_signup, pass_signup, name;
    String Num, Pass, Name;
    FlatButton ok_login, ok_signup;
    SharedPreferences sharedPreferences;
    MaterialDialog dialog;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialization
        signup = (TextView) findViewById(R.id.signup);
        login = (TextView) findViewById(R.id.login);
        sig = (LinearLayout) findViewById(R.id.signup_layout);
        log = (LinearLayout) findViewById(R.id.loginlayout);
        num_login = (EditText) findViewById(R.id.input_num_login);
        pass_login = (EditText) findViewById(R.id.input_pass_login);
        num_signup = (EditText) findViewById(R.id.input_num_signup);
        pass_signup = (EditText) findViewById(R.id.input_pass_signup);
        name = (EditText) findViewById(R.id.input_name_signup);
        ok_login = (FlatButton) findViewById(R.id.ok_login);
        ok_signup = (FlatButton) findViewById(R.id.ok_signup);
        mHandler = new Handler(Looper.getMainLooper());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // If user is already logged in, open MainActivity
        if (sharedPreferences.getString(Constants.USER_ID, null) != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // Open signup
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setVisibility(View.GONE);
                sig.setVisibility(View.VISIBLE);
            }
        });

        // Open login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setVisibility(View.VISIBLE);
                sig.setVisibility(View.GONE);
            }
        });

        // Call login
        ok_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = num_login.getText().toString();
                Pass = pass_login.getText().toString();
                login(Num, Pass);
            }
        });

        // Call signup
        ok_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = num_signup.getText().toString();
                Pass = pass_signup.getText().toString();
                Name = name.getText().toString();
                signup(Name, Num, Pass);
            }
        });
    }

    /**
     * Calls Login API and checks for validity of credentials
     * If yes, transfer to MainActivity
     *
     * @param num  user's phone number
     * @param pass password user entered
     */
    public void login(final String num, String pass) {

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
    public void signup(final String name, final String num, String pass) {

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
}
