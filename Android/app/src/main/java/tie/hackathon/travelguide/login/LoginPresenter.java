package tie.hackathon.travelguide.login;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Util.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tie.hackathon.travelguide.Base.BasePresenter;

/**
 * Created by el on 5/4/17.
 */

public class LoginPresenter implements BasePresenter<LoginView>{
    private LoginView view;

    public void bind(LoginView view){
        this.view = view;
    }

    public void unbind(){
        view = null;
    }

    public void signUp(){
        view.openSignUp();
    }


    /**
     * Calls Signup API
     *
     * @param Name user's name
     * @param Num  user's phone number
     * @param Pass password user entered
     */
    public void ok_signUp(final String Name, final String Num, String Pass, final Handler mHandler){

        view.showLoadingDialog();

        String uri = Constants.apilink + "users/signup.php?name=" + Name + "&contact=" + Num + "&password=" + Pass;

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(res);
                            String id = ob.getString("user_id");
                            view.rememberUserInfo(id,Name,Num);
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

    public void login(){
        view.openLogin();
    }

    /**
     * Calls Login API and checks for validity of credentials
     * If yes, transfer to MainActivity
     *
     * @param Num  user's phone number
     * @param Pass password user entered
     */
    public void ok_login(final String Num, String Pass, final Handler mHandler){

        view.showLoadingDialog();

        String uri = Constants.apilink + "users/login.php?contact=" + Num + "&password=" + Pass;
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject ob = new JSONObject(res);
                            Boolean success = ob.getBoolean("success");
                            if (success) {
                                JSONObject o = ob.getJSONObject("user_id");
                                String id = o.getString("id");
                                String name = o.getString("name");
                                view.rememberUserInfo(id,name,Num);
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
