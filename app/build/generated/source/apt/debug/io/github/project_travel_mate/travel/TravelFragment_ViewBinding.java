// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TravelFragment_ViewBinding implements Unbinder {
  private TravelFragment target;

  @UiThread
  public TravelFragment_ViewBinding(TravelFragment target, View source) {
    this.target = target;

    target.mTravelOptionsRecycleView = Utils.findRequiredViewAsType(source, R.id.travel_options_recycle_view, "field 'mTravelOptionsRecycleView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TravelFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTravelOptionsRecycleView = null;
  }
}
