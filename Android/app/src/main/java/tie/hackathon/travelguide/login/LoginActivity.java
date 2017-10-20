package tie.hackathon.travelguide.login;

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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;

import utils.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import tie.hackathon.travelguide.MainActivity;
import tie.hackathon.travelguide.R;

/**
 * Initiates login
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {

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

    private String numString;
    private String passString;
    private String nameString;

    private SharedPreferences   sharedPreferences;
    private MaterialDialog      dialog;
    private Handler             mhandler;

    private LoginPresenter loginPresenter = new LoginPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginPresenter.bind(this);
        ButterKnife.bind(this);

        // Initialization
        mhandler = new Handler(Looper.getMainLooper());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get runtime permissions for Android M
        getRunTimePermissions();

        // If user is already logged in, open MainActivity
        checkUserSession();

        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        ok_login.setOnClickListener(this);
        ok_signup.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Open signup
            case R.id.signup :
                loginPresenter.signUp();
                break;
            // Open login
            case R.id.login :
                loginPresenter.login();
                break;
            // Call login
            case R.id.ok_login :
                numString = num_login.getText().toString();
                passString = pass_login.getText().toString();
                loginPresenter.ok_login(numString, passString, mhandler);
                break;
            // Call signup
            case R.id.ok_signup :
                numString = num_signup.getText().toString();
                passString = pass_signup.getText().toString();
                nameString = name.getText().toString();
                loginPresenter.ok_signUp(nameString, numString, passString, mhandler);
                break;
        }
    }

    @Override
    public void rememberUserInfo(String id, String name, String num) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_ID, id);
        editor.putString(Constants.USER_NAME, name);
        editor.putString(Constants.USER_NUMBER, num);
        editor.apply();
    }

    @Override
    public void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this, "Invalid Password or number", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showLoadingDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissLoadingDialog() {
        dialog.dismiss();
    }

    @Override
    public void getRunTimePermissions() {
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
    }

    @Override
    public void checkUserSession() {
        if (sharedPreferences.getString(Constants.USER_ID, null) != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void openSignUp() {
        log.setVisibility(View.GONE);
        sig.setVisibility(View.VISIBLE);
    }

    @Override
    public void openLogin() {
        log.setVisibility(View.VISIBLE);
        sig.setVisibility(View.GONE);
    }
}
