// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CitiesTravelledAdapter$ViewHolder_ViewBinding implements Unbinder {
  private CitiesTravelledAdapter.ViewHolder target;

  @UiThread
  public CitiesTravelledAdapter$ViewHolder_ViewBinding(CitiesTravelledAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.cityName = Utils.findRequiredViewAsType(source, R.id.city_name, "field 'cityName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CitiesTravelledAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cityName = null;
  }
}
