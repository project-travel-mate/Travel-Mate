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

//    void checkUserSession();

    void showNoNetwork();

    void openSignUp();

    void openLogin(boolean showToastMessage);

    void setLoginEmail(String email);

    void showMessage(String message);

    void forgotPassword();

    void openResetPin(String email);

    void resendResetCode();

    void newPasswordInput();

    void confirmPasswordReset(String email);
}
