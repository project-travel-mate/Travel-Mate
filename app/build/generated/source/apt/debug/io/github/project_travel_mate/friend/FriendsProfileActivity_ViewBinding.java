// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.friend;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FriendsProfileActivity_ViewBinding implements Unbinder {
  private FriendsProfileActivity target;

  @UiThread
  public FriendsProfileActivity_ViewBinding(FriendsProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FriendsProfileActivity_ViewBinding(FriendsProfileActivity target, View source) {
    this.target = target;

    target.friendDisplayImage = Utils.findRequiredViewAsType(source, R.id.display_image, "field 'friendDisplayImage'", ImageView.class);
    target.friendsEmail = Utils.findRequiredViewAsType(source, R.id.display_email, "field 'friendsEmail'", TextView.class);
    target.friendUserName = Utils.findRequiredViewAsType(source, R.id.display_name, "field 'friendUserName'", TextView.class);
    target.friendJoiningDate = Utils.findRequiredViewAsType(source, R.id.display_joining_date, "field 'friendJoiningDate'", TextView.class);
    target.displayStatus = Utils.findRequiredViewAsType(source, R.id.display_status, "field 'displayStatus'", TextView.class);
    target.statusIcon = Utils.findRequiredViewAsType(source, R.id.status_icon, "field 'statusIcon'", ImageView.class);
    target.profileIcon = Utils.findRequiredViewAsType(source, R.id.profile_icon, "field 'profileIcon'", ImageView.class);
    target.dateJoinedIcon = Utils.findRequiredViewAsType(source, R.id.date_joined_icon, "field 'dateJoinedIcon'", ImageView.class);
    target.emailIcon = Utils.findRequiredViewAsType(source, R.id.email_icon, "field 'emailIcon'", ImageView.class);
    target.tripsTogetherLayout = Utils.findRequiredViewAsType(source, R.id.trips_together_layout, "field 'tripsTogetherLayout'", RelativeLayout.class);
    target.dateJoinedLayout = Utils.findRequiredViewAsType(source, R.id.date_joined_layout, "field 'dateJoinedLayout'", LinearLayout.class);
    target.mutualTripsText = Utils.findRequiredViewAsType(source, R.id.display_mutual_trips, "field 'mutualTripsText'", TextView.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.cityRecycler = Utils.findRequiredViewAsType(source, R.id.friend_city_list_recycler, "field 'cityRecycler'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FriendsProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.friendDisplayImage = null;
    target.friendsEmail = null;
    target.friendUserName = null;
    target.friendJoiningDate = null;
    target.displayStatus = null;
    target.statusIcon = null;
    target.profileIcon = null;
    target.dateJoinedIcon = null;
    target.emailIcon = null;
    target.tripsTogetherLayout = null;
    target.dateJoinedLayout = null;
    target.mutualTripsText = null;
    target.animationView = null;
    target.recyclerView = null;
    target.cityRecycler = null;
  }
}
