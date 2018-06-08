package io.github.project_travel_mate.login;

/**
 * Created by el on 5/4/17.
 */

interface LoginView {

    void rememberUserInfo(String id, String name, String num);

    void startMainActivity();

    void showError();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void getRunTimePermissions();

    void checkUserSession();

    void openSignUp();

    void openLogin();

}
