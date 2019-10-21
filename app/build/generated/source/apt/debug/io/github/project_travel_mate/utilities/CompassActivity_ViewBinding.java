// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CompassActivity_ViewBinding implements Unbinder {
  private CompassActivity target;

  @UiThread
  public CompassActivity_ViewBinding(CompassActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CompassActivity_ViewBinding(CompassActivity target, View source) {
    this.target = target;

    target.mArrowView = Utils.findRequiredViewAsType(source, R.id.compass_image_hands, "field 'mArrowView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CompassActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mArrowView = null;
  }
}
