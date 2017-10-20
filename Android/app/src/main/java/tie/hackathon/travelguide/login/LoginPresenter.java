package tie.hackathon.travelguide.login;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by el on 5/4/17.
 */

public class LoginPresenter {
    private LoginView view;

    public void bind(LoginView view) {
        this.view = view;
    }

    public void unbind() {
        view = null;
    }

    public void signUp() {
        view.openSignUp();
    }


    /**
     * Calls Signup API
     *
     * @param name      user's name
     * @param num       user's phone number
     * @param pass      password user entered
     * @param mhandler  handler
     */
    public void ok_signUp(final String name, final String num, String pass, final Handler mhandler) {

        view.showLoadingDialog();

        String uri = Constants.apilink + "users/signup.php?name=" + name + "&contact=" + num + "&password=" + pass;

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
                view.showError();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String res = response.body().string();
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(res);
                            String id = ob.getString("user_id");
                            view.rememberUserInfo(id, name, num);
                            view.startMainActivity();
                            view.dismissLoadingDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            view.showError();
                        }
                    }
                });
            }
        });
    }

    public void login() {
        view.openLogin();
    }

    /**
     * Calls Login API and checks for validity of credentials
     * If yes, transfer to MainActivity
     *
     * @param num  user's phone number
     * @param pass password user entered
     */
    public void ok_login(final String num, String pass, final Handler mhandler) {

        view.showLoadingDialog();

        String uri = Constants.apilink + "users/login.php?contact=" + num + "&password=" + pass;
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
                view.showError();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String res = response.body().string();
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(res);
                            Boolean success = ob.getBoolean("success");
                            if (success) {
                                JSONObject o = ob.getJSONObject("user_id");
                                String id = o.getString("id");
                                String name = o.getString("name");
                                view.rememberUserInfo(id, name, num);
                                view.startMainActivity();
                                view.dismissLoadingDialog();
                            } else {
                                view.showError();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                );
            }
        });
    }
}
