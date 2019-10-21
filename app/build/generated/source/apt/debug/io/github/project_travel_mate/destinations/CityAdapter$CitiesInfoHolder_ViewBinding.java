// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CityAdapter$CitiesInfoHolder_ViewBinding implements Unbinder {
  private CityAdapter.CitiesInfoHolder target;

  @UiThread
  public CityAdapter$CitiesInfoHolder_ViewBinding(CityAdapter.CitiesInfoHolder target,
      View source) {
    this.target = target;

    target.nickName = Utils.findRequiredViewAsType(source, R.id.nickname, "field 'nickName'", TextView.class);
    target.fv1 = Utils.findRequiredViewAsType(source, R.id.interest_1, "field 'fv1'", TextView.class);
    target.fv2 = Utils.findRequiredViewAsType(source, R.id.interest_2, "field 'fv2'", TextView.class);
    target.fv3 = Utils.findRequiredViewAsType(source, R.id.interest_3, "field 'fv3'", TextView.class);
    target.fv4 = Utils.findRequiredViewAsType(source, R.id.interest_4, "field 'fv4'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CityAdapter.CitiesInfoHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nickName = null;
    target.fv1 = null;
    target.fv2 = null;
    target.fv3 = null;
    target.fv4 = null;
  }
}
