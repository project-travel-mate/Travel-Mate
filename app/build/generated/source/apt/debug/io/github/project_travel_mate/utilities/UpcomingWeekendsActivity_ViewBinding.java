// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UpcomingWeekendsActivity_ViewBinding implements Unbinder {
  private UpcomingWeekendsActivity target;

  @UiThread
  public UpcomingWeekendsActivity_ViewBinding(UpcomingWeekendsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UpcomingWeekendsActivity_ViewBinding(UpcomingWeekendsActivity target, View source) {
    this.target = target;

    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.upcoming_weekends_recycler, "field 'mRecyclerView'", RecyclerView.class);
    target.mLayout = Utils.findRequiredViewAsType(source, R.id.upcoming_weekends_activity, "field 'mLayout'", CoordinatorLayout.class);
    target.mNoItems = Utils.findRequiredViewAsType(source, R.id.upcoming_weekends_no_items, "field 'mNoItems'", TextView.class);
    target.mMainLayout = Utils.findRequiredViewAsType(source, R.id.upcoming_weekends_main_layout, "field 'mMainLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UpcomingWeekendsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.swipeRefreshLayout = null;
    target.mRecyclerView = null;
    target.mLayout = null;
    target.mNoItems = null;
    target.mMainLayout = null;
  }
}
