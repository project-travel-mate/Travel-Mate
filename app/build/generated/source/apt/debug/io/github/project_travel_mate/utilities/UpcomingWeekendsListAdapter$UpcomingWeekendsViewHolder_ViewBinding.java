// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.vipulasri.timelineview.TimelineView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UpcomingWeekendsListAdapter$UpcomingWeekendsViewHolder_ViewBinding implements Unbinder {
  private UpcomingWeekendsListAdapter.UpcomingWeekendsViewHolder target;

  @UiThread
  public UpcomingWeekendsListAdapter$UpcomingWeekendsViewHolder_ViewBinding(UpcomingWeekendsListAdapter.UpcomingWeekendsViewHolder target,
      View source) {
    this.target = target;

    target.name = Utils.findRequiredViewAsType(source, R.id.upcoming_weekends_item_name, "field 'name'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.upcoming_weekends_item_date, "field 'date'", TextView.class);
    target.timelineView = Utils.findRequiredViewAsType(source, R.id.timeline_view, "field 'timelineView'", TimelineView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UpcomingWeekendsListAdapter.UpcomingWeekendsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.name = null;
    target.date = null;
    target.timelineView = null;
  }
}
