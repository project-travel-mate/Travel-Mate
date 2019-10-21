// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.card.MaterialCardView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeFragment_ViewBinding implements Unbinder {
  private HomeFragment target;

  @UiThread
  public HomeFragment_ViewBinding(HomeFragment target, View source) {
    this.target = target;

    target.mHotelBookingView = Utils.findRequiredViewAsType(source, R.id.materialCardView2, "field 'mHotelBookingView'", MaterialCardView.class);
    target.mFriendsView = Utils.findRequiredViewAsType(source, R.id.materialCardView21, "field 'mFriendsView'", MaterialCardView.class);
    target.mTripsView = Utils.findRequiredViewAsType(source, R.id.materialCardView3, "field 'mTripsView'", MaterialCardView.class);
    target.mCitiesView = Utils.findRequiredViewAsType(source, R.id.popular_cities_home, "field 'mCitiesView'", MaterialCardView.class);
    target.mUtilitiesView = Utils.findRequiredViewAsType(source, R.id.utilities_home, "field 'mUtilitiesView'", MaterialCardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mHotelBookingView = null;
    target.mFriendsView = null;
    target.mTripsView = null;
    target.mCitiesView = null;
    target.mUtilitiesView = null;
  }
}
