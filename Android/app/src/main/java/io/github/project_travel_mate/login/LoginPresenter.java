package io.github.project_travel_mate.login;

import android.os.Handler;

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
                            mView.openLogin();
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

    public void login() {
        mView.openLogin();
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
                    mView.showError();
                    mView.dismissLoadingDialog();
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

}