// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.friend;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MutualTripsAdapter$ViewHolder_ViewBinding implements Unbinder {
  private MutualTripsAdapter.ViewHolder target;

  @UiThread
  public MutualTripsAdapter$ViewHolder_ViewBinding(MutualTripsAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.cityImage = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'cityImage'", ImageView.class);
    target.cityName = Utils.findRequiredViewAsType(source, R.id.tv, "field 'cityName'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.date, "field 'date'", TextView.class);
    target.linearLayout = Utils.findRequiredViewAsType(source, R.id.trip_linear_layout, "field 'linearLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MutualTripsAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cityImage = null;
    target.cityName = null;
    target.date = null;
    target.linearLayout = null;
  }
}
