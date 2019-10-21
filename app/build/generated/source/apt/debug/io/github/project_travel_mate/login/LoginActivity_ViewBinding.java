// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.login;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.dd.processbutton.FlatButton;
import com.mukesh.OtpView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.signup = Utils.findRequiredViewAsType(source, R.id.signup, "field 'signup'", TextView.class);
    target.login = Utils.findRequiredViewAsType(source, R.id.login, "field 'login'", TextView.class);
    target.mBackToLogin = Utils.findRequiredViewAsType(source, R.id.back_to_login, "field 'mBackToLogin'", TextView.class);
    target.sig = Utils.findRequiredViewAsType(source, R.id.signup_layout, "field 'sig'", LinearLayout.class);
    target.log = Utils.findRequiredViewAsType(source, R.id.loginlayout, "field 'log'", LinearLayout.class);
    target.mForgotPasswordLayout = Utils.findRequiredViewAsType(source, R.id.forgot_password_layout, "field 'mForgotPasswordLayout'", LinearLayout.class);
    target.mResetCodeLayout = Utils.findRequiredViewAsType(source, R.id.reset_code_layout, "field 'mResetCodeLayout'", LinearLayout.class);
    target.mNewPasswordLayout = Utils.findRequiredViewAsType(source, R.id.new_password_layout, "field 'mNewPasswordLayout'", LinearLayout.class);
    target.email_login = Utils.findRequiredViewAsType(source, R.id.input_email_login, "field 'email_login'", EditText.class);
    target.pass_login = Utils.findRequiredViewAsType(source, R.id.input_pass_login, "field 'pass_login'", EditText.class);
    target.email_signup = Utils.findRequiredViewAsType(source, R.id.input_email_signup, "field 'email_signup'", EditText.class);
    target.pass_signup = Utils.findRequiredViewAsType(source, R.id.input_pass_signup, "field 'pass_signup'", EditText.class);
    target.confirm_pass_signup = Utils.findRequiredViewAsType(source, R.id.input_confirm_pass_signup, "field 'confirm_pass_signup'", EditText.class);
    target.firstName = Utils.findRequiredViewAsType(source, R.id.input_firstname_signup, "field 'firstName'", EditText.class);
    target.lastName = Utils.findRequiredViewAsType(source, R.id.input_lastname_signup, "field 'lastName'", EditText.class);
    target.mEmailForgotPassword = Utils.findRequiredViewAsType(source, R.id.input_email_forgot_password, "field 'mEmailForgotPassword'", EditText.class);
    target.mNewPasswordReset = Utils.findRequiredViewAsType(source, R.id.input_pass_reset, "field 'mNewPasswordReset'", EditText.class);
    target.mConfirmNewPasswordReset = Utils.findRequiredViewAsType(source, R.id.input_confirm_pass_reset, "field 'mConfirmNewPasswordReset'", EditText.class);
    target.ok_login = Utils.findRequiredViewAsType(source, R.id.ok_login, "field 'ok_login'", FlatButton.class);
    target.ok_signup = Utils.findRequiredViewAsType(source, R.id.ok_signup, "field 'ok_signup'", FlatButton.class);
    target.mOkSubmitReset = Utils.findRequiredViewAsType(source, R.id.ok_submit_pass_reset, "field 'mOkSubmitReset'", FlatButton.class);
    target.mOkConfirmReset = Utils.findRequiredViewAsType(source, R.id.ok_confirm_pass_reset, "field 'mOkConfirmReset'", FlatButton.class);
    target.mForgotPasswordText = Utils.findRequiredViewAsType(source, R.id.forgot_password, "field 'mForgotPasswordText'", TextView.class);
    target.mCodeSentAlert = Utils.findRequiredViewAsType(source, R.id.code_sent_alert, "field 'mCodeSentAlert'", TextView.class);
    target.mResendCodeText = Utils.findRequiredViewAsType(source, R.id.resend_code, "field 'mResendCodeText'", TextView.class);
    target.mResetCode = Utils.findRequiredViewAsType(source, R.id.reset_code, "field 'mResetCode'", OtpView.class);
    target.mInputLayoutEmailForgotPassword = Utils.findRequiredViewAsType(source, R.id.input_layout_email_forgot_password, "field 'mInputLayoutEmailForgotPassword'", TextInputLayout.class);
    target.mInputLayoutFirstNameSignup = Utils.findRequiredViewAsType(source, R.id.input_layout_firstname_signup, "field 'mInputLayoutFirstNameSignup'", TextInputLayout.class);
    target.mInputLayoutLastNameSignup = Utils.findRequiredViewAsType(source, R.id.input_layout_lastname_signup, "field 'mInputLayoutLastNameSignup'", TextInputLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.signup = null;
    target.login = null;
    target.mBackToLogin = null;
    target.sig = null;
    target.log = null;
    target.mForgotPasswordLayout = null;
    target.mResetCodeLayout = null;
    target.mNewPasswordLayout = null;
    target.email_login = null;
    target.pass_login = null;
    target.email_signup = null;
    target.pass_signup = null;
    target.confirm_pass_signup = null;
    target.firstName = null;
    target.lastName = null;
    target.mEmailForgotPassword = null;
    target.mNewPasswordReset = null;
    target.mConfirmNewPasswordReset = null;
    target.ok_login = null;
    target.ok_signup = null;
    target.mOkSubmitReset = null;
    target.mOkConfirmReset = null;
    target.mForgotPasswordText = null;
    target.mCodeSentAlert = null;
    target.mResendCodeText = null;
    target.mResetCode = null;
    target.mInputLayoutEmailForgotPassword = null;
    target.mInputLayoutFirstNameSignup = null;
    target.mInputLayoutLastNameSignup = null;
  }
}
