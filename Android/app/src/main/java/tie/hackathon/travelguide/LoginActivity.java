package tie.hackathon.travelguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Constants;
import Util.Utils;

public class LoginActivity extends AppCompatActivity {

    TextView signup, login;
    LinearLayout sig, log;
    EditText num_login, pass_login, num_signup, pass_signup, name;
    String Num, Pass, Name;
    FlatButton ok_login, ok_signup;
    SharedPreferences s;
    SharedPreferences.Editor e;
    MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        s = PreferenceManager.getDefaultSharedPreferences(this);
        e = s.edit();

        if (s.getString(Constants.USER_ID, null) != null) {

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                log.setVisibility(View.GONE);
                sig.setVisibility(View.VISIBLE);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.setVisibility(View.VISIBLE);
                sig.setVisibility(View.GONE);

            }
        });


        ok_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = num_login.getText().toString();
                Pass = pass_login.getText().toString();
                new logintask(Num, Pass).execute();


            }
        });

        ok_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = num_signup.getText().toString();
                Pass = pass_signup.getText().toString();
                Name = name.getText().toString();
                new signuptask(Name, Num, Pass).execute();


            }
        });


    }


    public class logintask extends AsyncTask<Void, Void, String> {

        String num, pass;

        public logintask(String num, String pass) {
            this.num = num;
            this.pass = pass;
        }

        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(LoginActivity.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("started", "strted");
                String uri = Constants.apilink +
                        "users/login.php?contact=" +
                        num +
                        "&password=" + pass;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here", url + readStream + " ");
                return readStream;
            } catch (Exception e) {
                Log.e("here", e.getMessage() + " ");
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null)
                return;

            try {
                //Tranform the string into a json object
                JSONObject ob = new JSONObject(result);

                Boolean success = ob.getBoolean("success");
                if (success) {

                    JSONObject o = ob.getJSONObject("user_id");
                    String id = o.getString("id");
                    String name = o.getString("name");
                    e.putString(Constants.USER_ID, id);
                    e.putString(Constants.USER_NAME, name);
                    e.putString(Constants.USER_NUMBER,num);
                    e.commit();

                    Log.e("vrsb", "id id" + id + name);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();


                } else {

                    Toast.makeText(LoginActivity.this, "Invalid Password or number", Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
            dialog.dismiss();
        }

    }

    public class signuptask extends AsyncTask<Void, Void, String> {

        String num, pass, name;

        public signuptask(String name, String num, String pass) {
            this.num = num;
            this.pass = pass;
            this.name = name;
        }


        @Override
        protected void onPreExecute() {
            dialog = new MaterialDialog.Builder(LoginActivity.this)
                    .title("Travel Mate")
                    .content("Please wait...")
                    .progress(true, 0)
                    .show();
        }


        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("started", "strted");
                String uri = Constants.apilink +
                        "users/signup.php?name=" +
                        name +
                        "&contact=" +
                        num +
                        "&password=" +
                        pass;
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                String readStream = Utils.readStream(con.getInputStream());
                Log.e("here", url + readStream + " ");
                return readStream;
            } catch (Exception e) {
                Log.e("here", e.getMessage() + " ");
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null)
                return;

            try {
                //Tranform the string into a json object
                JSONObject ob = new JSONObject(result);
                String id = ob.getString("user_id");

                e.putString(Constants.USER_ID, id);
                e.putString(Constants.USER_NAME, name);
                e.putString(Constants.USER_NUMBER,num);
                e.commit();

                Log.e("vrsb", "id id" + id + name);

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");
                Toast.makeText(LoginActivity.this,"Some error occured : " + e.getMessage(),Toast.LENGTH_LONG).show();

            }
            dialog.dismiss();
        }

    }

}
