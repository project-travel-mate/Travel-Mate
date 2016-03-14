package tie.hackathon.travelguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.processbutton.FlatButton;

public class LoginActivity extends AppCompatActivity {

    TextView signup,login;
    LinearLayout sig,log;
    EditText num_login,pass_login,num_signup,pass_signup,name;
    String Num,Pass,Name;
    FlatButton ok_login,ok_signup;

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


                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);

            }
        });

        ok_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Num = num_signup.getText().toString();
                Pass = pass_signup.getText().toString();
                Name = name.getText().toString();


                Intent i =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);

            }
        });



    }

}
