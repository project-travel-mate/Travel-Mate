// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShareContactActivity_ViewBinding implements Unbinder {
  private ShareContactActivity target;

  @UiThread
  public ShareContactActivity_ViewBinding(ShareContactActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShareContactActivity_ViewBinding(ShareContactActivity target, View source) {
    this.target = target;

    target.scan = Utils.findRequiredViewAsType(source, R.id.scan, "field 'scan'", Button.class);
    target.qrCodeView = Utils.findRequiredViewAsType(source, R.id.im, "field 'qrCodeView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShareContactActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.scan = null;
    target.qrCodeView = null;
  }
}
