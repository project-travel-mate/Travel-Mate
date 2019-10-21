// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CityAdapter$CitiesHolder_ViewBinding implements Unbinder {
  private CityAdapter.CitiesHolder target;

  @UiThread
  public CityAdapter$CitiesHolder_ViewBinding(CityAdapter.CitiesHolder target, View source) {
    this.target = target;

    target.mLeftView = Utils.findRequiredView(source, R.id.left_side, "field 'mLeftView'");
    target.mRightView = Utils.findRequiredView(source, R.id.right_side, "field 'mRightView'");
    target.leftAvatar = Utils.findRequiredViewAsType(source, R.id.first, "field 'leftAvatar'", ImageView.class);
    target.rightAvatar = Utils.findRequiredViewAsType(source, R.id.second, "field 'rightAvatar'", ImageView.class);
    target.left = Utils.findRequiredViewAsType(source, R.id.name1, "field 'left'", TextView.class);
    target.right = Utils.findRequiredViewAsType(source, R.id.name2, "field 'right'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CityAdapter.CitiesHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mLeftView = null;
    target.mRightView = null;
    target.leftAvatar = null;
    target.rightAvatar = null;
    target.left = null;
    target.right = null;
  }
}
