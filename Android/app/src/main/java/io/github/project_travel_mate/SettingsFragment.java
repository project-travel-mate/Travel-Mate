package io.github.project_travel_mate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.airbnb.lottie.LottieAnimationView;
import com.dd.processbutton.iml.ActionProcessButton;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.TravelmateSnackbars;

import static utils.Constants.API_LINK_V2;
import static utils.Constants.QUOTES_SHOW_DAILY;
import static utils.Constants.READ_NOTIF_STATUS;
import static utils.Constants.USER_TOKEN;

public class SettingsFragment extends Fragment {

    @BindView(R.id.switch_notification)
    Switch notificationSwitch;
    @BindView(R.id.switch_quotes)
    Switch quoteSwitch;
    @BindView(R.id.old_password)
    EditText oldPasswordText;
    @BindView(R.id.new_password)
    EditText newPasswordText;
    @BindView(R.id.connfirm_password)
    EditText confirmPasswordText;
    @BindView(R.id.done_button)
    ActionProcessButton doneButton;
    @BindView(R.id.delete_button)
    ActionProcessButton deleteButton;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.text_input_old_password)
    TextInputLayout oldPasswordInput;
    @BindView(R.id.text_input_new_password)
    TextInputLayout newPasswordInput;
    @BindView(R.id.text_input_confirm_password)
    TextInputLayout confirmPasswordInput;


    private String mToken;
    private Handler mHandler;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;
    private View mView;
    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();

    public SettingsFragment() {
        //required public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, mView);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean readNotifStatus = mSharedPreferences.getBoolean(READ_NOTIF_STATUS, true);
        boolean showDailyQuote = mSharedPreferences.getBoolean(QUOTES_SHOW_DAILY, true);
        mToken = mSharedPreferences.getString(USER_TOKEN, null);
        mHandler = new Handler(Looper.getMainLooper());

        doneButton.setMode(ActionProcessButton.Mode.ENDLESS);
        doneButton.setOnClickListener(v -> {
            hideKeyboard();
            if (checkEmptyText())
                checkPasswordMatch();
        });

        deleteButton.setOnClickListener(v -> {
            deleteAccount();
        });

        if (readNotifStatus) {
            notificationSwitch.setChecked(true);
        }
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                mSharedPreferences.edit().putBoolean(READ_NOTIF_STATUS, false).apply();
            } else {
                mSharedPreferences.edit().putBoolean(READ_NOTIF_STATUS, true).apply();
            }
        });

        if (showDailyQuote) {
            quoteSwitch.setChecked(true);
        }
        quoteSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mSharedPreferences.edit().putBoolean(QUOTES_SHOW_DAILY, true).apply();
            } else {
                mSharedPreferences.edit().putBoolean(QUOTES_SHOW_DAILY, false).apply();
            }
        });
        return mView;
    }

    /**
     * Checks if any editText is left empty by user
     * @return true if all fields are filled
     */
    private boolean checkEmptyText() {
        if (oldPasswordText.getText().toString().equals("")) {
            oldPasswordInput.setError(getString(R.string.required_error));
            oldPasswordText.requestFocus();
            return false;
        } else if (newPasswordText.getText().toString().equals("")) {
            newPasswordInput.setError(getString(R.string.required_error));
            newPasswordText.requestFocus();
            return false;
        } else if (confirmPasswordText.getText().toString().equals("")) {
            confirmPasswordInput.setError(getString(R.string.required_error));
            confirmPasswordText.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Checks if newPassword and confirmPassword match
     */
    private void checkPasswordMatch() {
        String oldPassword = oldPasswordText.getText().toString();
        String newPassword = newPasswordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
        if (validatePassword(newPassword)) {
            if (newPassword.equals(confirmPassword)) {
                changePassword(newPassword, oldPassword);
            } else {
                Snackbar snackbar = Snackbar
                        .make(mActivity.findViewById(android.R.id.content),
                                R.string.passwords_check, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    /**
     * Validates the given password, checks if given password proper format as standard password string
     * @param passString password string to be validate
     * @return Boolean returns true if email format is correct, otherwise false
     */
    public boolean validatePassword(String passString) {
        if (passString.length() >= 8) {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(passString);

            if (matcher.matches()) {
                return true;
            } else {
                Snackbar snackbar = Snackbar
                        .make(mActivity.findViewById(android.R.id.content),
                                R.string.invalid_password, Snackbar.LENGTH_LONG);
                snackbar.show();
                return false;
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(mActivity.findViewById(android.R.id.content),
                            R.string.password_length, Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
    }

    //TODO :: Update API and check its functionality
    public void changePassword(String newPassword, String oldPassword) {

        doneButton.setProgress(1);
        String uri = API_LINK_V2 + "update-password";
        Log.v("EXECUTING", uri);
        //Set up client
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("old_password", oldPassword)
                .addFormDataPart("new_password", newPassword)
                .build();

        // Create a http request object.
        Request request = new Request.Builder()
                .header("Authorization", "Token " + mToken)
                .url(uri)
                .post(requestBody)
                .build();

        // Create a new Call object with post method.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Request Failed", "Message : " + e.getMessage());
                mHandler.post(() -> networkError());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                doneButton.setProgress(0);
                mHandler.post(() -> {
                    if (response.isSuccessful()) {
                        clearText();
                        TravelmateSnackbars.createSnackBar(mActivity.findViewById(android.R.id.content)
                                , res, Snackbar.LENGTH_LONG).show();
                    } else {
                        TravelmateSnackbars.createSnackBar(mActivity.findViewById(android.R.id.content)
                                , res, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void deleteAccount() {
        //set AlertDialog before delete account
        ContextThemeWrapper crt = new ContextThemeWrapper(mActivity, R.style.AlertDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(crt);
        builder.setMessage(R.string.delete_account)
                .setPositiveButton(R.string.positive_button,
                        (dialog, which) -> {

                            String uri = API_LINK_V2 + "delete-profile";
                            //Set up client
                            OkHttpClient client = new OkHttpClient();

                            // Create a http request object.
                            Request request = new Request.Builder()
                                    .header("Authorization", "Token " + mToken)
                                    .url(uri)
                                    .build();

                            // Create a new Call object with post method.
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("Request Failed", "Message : " + e.getMessage());
                                    mHandler.post(() -> networkError());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String res = Objects.requireNonNull(response.body()).string();
                                    mHandler.post(() -> {
                                        if (response.isSuccessful()) {
                                            mSharedPreferences
                                                    .edit()
                                                    .putString(USER_TOKEN, null)
                                                    .apply();

                                            Intent intent = LoginActivity.getStartIntent(mActivity);
                                            mActivity.startActivity(intent);
                                            mActivity.finish();
                                        }
                                    });

                                }
                            });

                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {

                        });
        builder.create().show();
    }

    /**
     * Plays the network lost animation in the view
     */
    private void networkError() {
        layout.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation(R.raw.network_lost);
        animationView.playAnimation();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

    private void clearText() {
        oldPasswordText.setText("");
        newPasswordText.setText("");
        confirmPasswordText.setText("");
    }

}
