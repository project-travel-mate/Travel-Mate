package io.github.project_travel_mate.login;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static utils.Constants.API_LINK_V2;

/**
 * Created by el on 5/4/17.
 */

class LoginPresenter {
    private LoginView mView;

    public void bind(LoginView view) {
        this.mView = view;
    }

    public void unbind() {
        mView = null;
    }

    public void signUp() {
        mView.openSignUp();
    }


    /**
     * Calls Signup API
     *
     * @param firstname user's first name
     * @param lastname  user's last name
     * @param email     user's email id
     * @param pass      password user entered
     * @param mhandler  handler
     */
    public void ok_signUp(final String firstname, final String lastname, final String email,
                          String pass, final Handler mhandler) {

        mView.showLoadingDialog();

        String uri = API_LINK_V2 + "sign-up";

        //Set up client
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", pass)
                .addFormDataPart("firstname", firstname)
                .addFormDataPart("lastname", lastname)
                .build();

        //Execute request
        final Request request = new Request.Builder()
                .url(uri)
                .post(requestBody)
                .build();

        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandler.post(() -> {
                    mView.showError();
                    mView.dismissLoadingDialog();
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = Objects.requireNonNull(response.body()).string();
                final int responseCode = response.code();
                mhandler.post(() -> {
                    try {
                        String successfulMessage = "\"Successfully registered\"";
                        if (responseCode == HttpsURLConnection.HTTP_CREATED && res.equals(successfulMessage)) {
                            //if successful redirect to login
                            mView.openLogin(false);
                            mView.setLoginEmail(email);
                            mView.showMessage("signup succeeded! please login");
                        } else {
                            // show error message
                            mView.showMessage(res);
                        }
                        mView.dismissLoadingDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mView.showError();
                    }
                });
            }
        });
    }

    public void login(boolean showToastMessage) {
        mView.openLogin(showToastMessage);
    }

    /**
     * Calls Login API and checks for validity of credentials
     * If yes, transfer to MainActivity
     *
     * @param email user's email id
     * @param pass  password user entered
     */
    public void ok_login(final String email, String pass, final Handler mhandler) {

        mView.showLoadingDialog();

        String uri = API_LINK_V2 + "sign-in";
        Log.v("EXECUTING", uri);

        //Set up client
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", email)
                .addFormDataPart("password", pass)
                .build();

        //Execute request
        Request request = new Request.Builder()
                .url(uri)
                .post(requestBody)
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mhandler.post(() -> {
                    if (mView != null) {
                        mView.showError();
                        mView.dismissLoadingDialog();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();
                mhandler.post(() -> {
                            try {
                                if (response.isSuccessful()) {
                                    JSONObject responeJsonObject = new JSONObject(res);
                                    String token = responeJsonObject.getString("token");
                                    mView.rememberUserInfo(token, email);
                                    mView.startMainActivity();
                                } else {
                                    mView.showError();
                                }
                                mView.dismissLoadingDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                );
            }
        });
    }

    public void forgotPassword() {
        mView.forgotPassword();
    }

    /**
     * called when a user submits his/her e-mail address for which the password needs to be reset
     *
     * @param email user's email address
     */
    public void ok_password_reset_request(String email, Handler mHandler) {
        //TODO validate email address, verify if it's a registered user, send 4- digit otp to email
        mHandler.post(() -> mView.showLoadingDialog());
        String uri = API_LINK_V2 + "forgot-password-email-code/" + email;
        //Set up client
        OkHttpClient client = new OkHttpClient();

        //Execute request
        Request request = new Request.Builder()
                .url(uri).get()
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> {
                    mView.dismissLoadingDialog();
                    mView.showError();
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {
                    mView.dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        mView.showMessage(res);
                        //if email is verified as a registered one
                        mView.openResetPin(email);
                    } else if (response.code() == 404) {
                        mView.showMessage("Invalid username");
                    } else {
                        mView.showError();
                    }
                });
            }
        });
    }

    /**
     * triggers a request to resend a 4-digit code for validation password reset request
     *
     * @param email user's email address to where the code has to be sent
     */
    public void resendResetCode(String email, Handler mHandler) {
        ok_password_reset_request(email, mHandler);
        ok_password_reset_confirm(email);
        mView.resendResetCode();
    }

    public void newPasswordInput() {
        mView.newPasswordInput();
    }

    public void ok_password_reset_confirm(String email) {
        mView.confirmPasswordReset(email);
    }

    /**
     * takes in the newly entered password and updates it against the user's email address
     *
     * @param email       user's email address
     * @param newPassword new password
     */
    public void ok_password_reset(String email, String code, String newPassword, Handler mHandler) {
        //TODO update the password for the corresponding email address
        mHandler.post(() -> mView.showLoadingDialog());
        String uri = API_LINK_V2 + "forgot-password-verify-code/" + email
                + "/" + code + "/" + newPassword;
        //Set up client
        OkHttpClient client = new OkHttpClient();
        //Execute request
        Request request = new Request.Builder()
                .url(uri).get()
                .build();
        //Setup callback
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> {
                    mView.dismissLoadingDialog();
                    mView.showError();
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = Objects.requireNonNull(response.body()).string();

                mHandler.post(() -> {
                    mView.dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        //display the login UI to login with the new password
                        mView.openLogin(true);
                    } else if (response.code() == 400) {
                        mView.showMessage("Wrong 4-digit code or wrong password format");
                    } else if (response.code() == 404) {
                        mView.showMessage("Wrong email");
                    } else {
                        mView.showError();
                    }
                });
            }
        });

    }
}