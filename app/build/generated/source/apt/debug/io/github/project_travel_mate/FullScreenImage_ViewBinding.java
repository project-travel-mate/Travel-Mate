// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import utils.TouchImageView;

public class FullScreenImage_ViewBinding implements Unbinder {
  private FullScreenImage target;

  @UiThread
  public FullScreenImage_ViewBinding(FullScreenImage target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FullScreenImage_ViewBinding(FullScreenImage target, View source) {
    this.target = target;

    target.fullProfileImage = Utils.findRequiredViewAsType(source, R.id.full_profile_image, "field 'fullProfileImage'", TouchImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FullScreenImage target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fullProfileImage = null;
  }
}
