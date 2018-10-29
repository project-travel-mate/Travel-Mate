package io.github.project_travel_mate;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.USER_EMAIL;
import static utils.Constants.USER_TOKEN;
import static utils.Constants.VERIFICATION_REQUEST_CODE;

public class VerifyEmailActivity extends AppCompatActivity implements OnOtpCompletionListener {

    @BindView(R.id.code_sent_alert)
    TextView mCodeSentAlert;

    @BindView(R.id.resend_code)
    TextView resendCode;

    @BindView(R.id.otp_code)
    OtpView resetCode;

    private String mToken;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);

        resendCode.setOnClickListener(view -> verifyEmail(null, false));
        resetCode.setOtpCompletionListener(this);

        String mUserEmail = mSharedPreferences.getString(USER_EMAIL, null);
        mCodeSentAlert.setText(String.format(getString(R.string.text_otp_code_sent_alert), mUserEmail));
    }

    @Override
    public void onOtpCompleted(String otp) {
        if (otp.trim().length() == 6) {
            verifyEmail(otp.trim(), true);
        }
    }


    public void verifyEmail(String code, boolean verify) {
        String uri;

        ProgressDialog progressDialog = new ProgressDialog(VerifyEmailActivity.this);
        progressDialog.setTitle(R.string.app_name);
        progressDialog.setCancelable(false);


        //if user entered 6 digit otp else send otp
        if (verify && code != null) {
            progressDialog.setMessage("Verifying OTP");
            uri = API_LINK_V2 + "confirm-verification-code/" + code;
        } else {
            progressDialog.setMessage("Sending OTP");
            uri = API_LINK_V2 + "generate-verification-code";
        }
        Log.v("EXECUTING", uri);
        progressDialog.show();

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
                mHandler.post(() -> Toast.makeText(VerifyEmailActivity.this,
                        "Server error. Please, try again !", Toast.LENGTH_SHORT).show());
                progressDialog.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mHandler.post(() -> {
                    progressDialog.dismiss();
                    if (response.body() != null) {
                        try {
                            final String res = Objects.requireNonNull(response.body()).string();
                            if (verify) {
                                if (res.equals("\"Unable to confirm verification code\"")) {
                                    Toast.makeText(VerifyEmailActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                } else if (res.equals("\"Verification code confirmed\"")) {
                                    Toast.makeText(VerifyEmailActivity.this,
                                            "Email Verified !", Toast.LENGTH_SHORT).show();
                                    setResult(VERIFICATION_REQUEST_CODE);
                                    finish();
                                }

                            } else {
                                if (res.equals("\"Verification code sent\"")) {
                                    Toast.makeText(VerifyEmailActivity.this,
                                            "OTP sent on registered email !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VerifyEmailActivity.this,
                                            "There was some error !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(VerifyEmailActivity.this,
                                    "There was some error. Please, try again !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}
