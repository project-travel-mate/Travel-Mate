// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.mytrips;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyTripsFragment_ViewBinding implements Unbinder {
  private MyTripsFragment target;

  private View view2131361847;

  @UiThread
  public MyTripsFragment_ViewBinding(final MyTripsFragment target, View source) {
    this.target = target;

    View view;
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv, "field 'mRecyclerView'", RecyclerView.class);
    target.my_trips_main_layout = Utils.findRequiredViewAsType(source, R.id.my_trips_main_layout, "field 'my_trips_main_layout'", RelativeLayout.class);
    target.noTrips = Utils.findRequiredViewAsType(source, R.id.my_trips_no_items, "field 'noTrips'", TextView.class);
    view = Utils.findRequiredView(source, R.id.add_trip, "method 'addTrip'");
    view2131361847 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addTrip();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MyTripsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.swipeRefreshLayout = null;
    target.mRecyclerView = null;
    target.my_trips_main_layout = null;
    target.noTrips = null;

    view2131361847.setOnClickListener(null);
    view2131361847 = null;
  }
}
