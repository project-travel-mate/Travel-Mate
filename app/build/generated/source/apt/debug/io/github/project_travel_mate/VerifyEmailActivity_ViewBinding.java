// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.mukesh.OtpView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VerifyEmailActivity_ViewBinding implements Unbinder {
  private VerifyEmailActivity target;

  @UiThread
  public VerifyEmailActivity_ViewBinding(VerifyEmailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VerifyEmailActivity_ViewBinding(VerifyEmailActivity target, View source) {
    this.target = target;

    target.mCodeSentAlert = Utils.findRequiredViewAsType(source, R.id.code_sent_alert, "field 'mCodeSentAlert'", TextView.class);
    target.resendCode = Utils.findRequiredViewAsType(source, R.id.resend_code, "field 'resendCode'", TextView.class);
    target.resetCode = Utils.findRequiredViewAsType(source, R.id.otp_code, "field 'resetCode'", OtpView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    VerifyEmailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mCodeSentAlert = null;
    target.resendCode = null;
    target.resetCode = null;
  }
}
