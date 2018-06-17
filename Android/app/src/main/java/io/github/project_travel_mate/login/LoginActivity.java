package io.github.project_travel_mate.login;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.MainActivity;
import io.github.project_travel_mate.R;

import static utils.Constants.USER_EMAIL;
import static utils.Constants.USER_TOKEN;

/**
 * Initiates login
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginView {

    @BindView(R.id.signup)
    TextView        signup;
    @BindView(R.id.login)
    TextView        login;
    @BindView(R.id.signup_layout)
    LinearLayout    sig;
    @BindView(R.id.loginlayout)
    LinearLayout    log;
    @BindView(R.id.input_email_login)
    EditText        email_login;
    @BindView(R.id.input_pass_login)
    EditText        pass_login;
    @BindView(R.id.input_email_signup)
    EditText        email_signup;
    @BindView(R.id.input_pass_signup)
    EditText        pass_signup;
    @BindView(R.id.input_name_signup)
    EditText        name;
    @BindView(R.id.ok_login)
    FlatButton      ok_login;
    @BindView(R.id.ok_signup)
    FlatButton      ok_signup;

    private SharedPreferences mSharedPreferences;
    private MaterialDialog mDialog;
    private Handler mHandler;

    private final LoginPresenter mLoginPresenter = new LoginPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        mLoginPresenter.bind(this);
        ButterKnife.bind(this);

        // Initialization
        mHandler = new Handler(Looper.getMainLooper());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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
        mLoginPresenter.unbind();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Open signup
            case R.id.signup :
                mLoginPresenter.signUp();
                break;
            // Open login
            case R.id.login :
                mLoginPresenter.login();
                break;
            // Call login
            case R.id.ok_login :
                String emailString = email_login.getText().toString();
                String passString = pass_login.getText().toString();
                mLoginPresenter.ok_login(emailString, passString, mHandler);
                break;
            // Call signup
            case R.id.ok_signup :
                emailString = email_signup.getText().toString();
                passString = pass_signup.getText().toString();
                String nameString = name.getText().toString();
                mLoginPresenter.ok_signUp(nameString, emailString, passString, mHandler);
                break;
        }
    }

    @Override
    public void rememberUserInfo(String token, String email) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.putString(USER_EMAIL, email);
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
        Toast.makeText(this, R.string.toast_invalid_username_or_password, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showLoadingDialog() {
        mDialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.progress_wait)
                .progress(true, 0)
                .show();
    }

    @Override
    public void dismissLoadingDialog() {
        mDialog.dismiss();
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
        if (mSharedPreferences.getString(USER_TOKEN, null) != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
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

    public void setLoginEmail(String email) {
        email_login.setText(email);
    }
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
                .show();
    }
}
