package io.github.project_travel_mate.login;

/**
 * Created by el on 5/4/17.
 */

interface LoginView {

    void rememberUserInfo(String token, String email);

    void startMainActivity();

    void showError();

    void showLoadingDialog();

    void dismissLoadingDialog();

    void getRunTimePermissions();

    void checkUserSession();

    void openSignUp();

    void openLogin();

    void setLoginEmail(String email);

    void showMessage(String message);

}
