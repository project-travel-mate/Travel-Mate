// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.mytrips;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.vipulasri.timelineview.TimelineView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TripsListAdapter$TimelineViewHolder_ViewBinding implements Unbinder {
  private TripsListAdapter.TimelineViewHolder target;

  @UiThread
  public TripsListAdapter$TimelineViewHolder_ViewBinding(TripsListAdapter.TimelineViewHolder target,
      View source) {
    this.target = target;

    target.city = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'city'", ImageView.class);
    target.cityname = Utils.findRequiredViewAsType(source, R.id.tv, "field 'cityname'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.date, "field 'date'", TextView.class);
    target.timelineView = Utils.findRequiredViewAsType(source, R.id.timeline_view, "field 'timelineView'", TimelineView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TripsListAdapter.TimelineViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.city = null;
    target.cityname = null;
    target.date = null;
    target.timelineView = null;
  }
}
