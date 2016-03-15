package tie.hackathon.travelguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.dd.processbutton.FlatButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import Util.Utils;

public class LoginActivity extends AppCompatActivity {

    TextView signup, login;
    LinearLayout sig, log;
    EditText num_login, pass_login, num_signup, pass_signup, name;
    String Num, Pass, Name;
    FlatButton ok_login, ok_signup;
    SharedPreferences s;
    SharedPreferences.Editor e;

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


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);

            }
        });

        ok_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = num_signup.getText().toString();
                Pass = pass_signup.getText().toString();
                Name = name.getText().toString();


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);

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
        protected String doInBackground(Void... params) {
            try {
                Log.e("started", "strted");
                String uri = "http://csinsit.org/prabhakar/tie/get-real-time-data.php?mode=";
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


            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
        }

    }

    public class signuptask extends AsyncTask<Void, Void, String> {

        String num, pass,name;

        public signuptask(String name,String num, String pass) {
            this.num = num;
            this.pass = pass;
            this.name = name;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Log.e("started", "strted");
                String uri = "http://csinsit.org/prabhakar/tie/get-real-time-data.php?mode=";
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


            } catch (JSONException e) {
                Log.e("here11", e.getMessage() + " ");

            }
        }

    }

}
