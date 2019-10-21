// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.mytrips;

import adapters.NestedListView;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyTripInfoActivity_ViewBinding implements Unbinder {
  private MyTripInfoActivity target;

  private View view2131361903;

  private View view2131361846;

  private View view2131362041;

  private TextWatcher view2131362041TextWatcher;

  @UiThread
  public MyTripInfoActivity_ViewBinding(MyTripInfoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyTripInfoActivity_ViewBinding(final MyTripInfoActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.city_image, "field 'cityImageView' and method 'cityImageClicked'");
    target.cityImageView = Utils.castView(view, R.id.city_image, "field 'cityImageView'", ImageView.class);
    view2131361903 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.cityImageClicked();
      }
    });
    target.cityName = Utils.findRequiredViewAsType(source, R.id.city_name, "field 'cityName'", TextView.class);
    target.tripDate = Utils.findRequiredViewAsType(source, R.id.trip_start_date, "field 'tripDate'", TextView.class);
    view = Utils.findRequiredView(source, R.id.add_new_friend, "field 'addNewFriend' and method 'onClick'");
    target.addNewFriend = Utils.castView(view, R.id.add_new_friend, "field 'addNewFriend'", TextView.class);
    view2131361846 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.listView = Utils.findRequiredViewAsType(source, R.id.friend_list, "field 'listView'", NestedListView.class);
    view = Utils.findRequiredView(source, R.id.friend_email, "field 'friendEmail' and method 'onTextChanged'");
    target.friendEmail = Utils.castView(view, R.id.friend_email, "field 'friendEmail'", AutoCompleteTextView.class);
    view2131362041 = view;
    view2131362041TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.onTextChanged();
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view2131362041TextWatcher);
    target.tripName = Utils.findRequiredViewAsType(source, R.id.trip_name, "field 'tripName'", EditText.class);
    target.friendTitle = Utils.findRequiredViewAsType(source, R.id.friend_title, "field 'friendTitle'", TextView.class);
    target.showIcon = Utils.findRequiredViewAsType(source, R.id.plus_icon, "field 'showIcon'", ImageView.class);
    target.editTrip = Utils.findRequiredViewAsType(source, R.id.edit_trip_icon, "field 'editTrip'", ImageView.class);
    target.tripPublicMessage = Utils.findRequiredViewAsType(source, R.id.public_trip_message, "field 'tripPublicMessage'", TextView.class);
    target.publicToggleButton = Utils.findRequiredViewAsType(source, R.id.ispublic_toggleButton, "field 'publicToggleButton'", ToggleButton.class);
    target.details = Utils.findRequiredViewAsType(source, R.id.know_more, "field 'details'", FloatingActionButton.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", LinearLayout.class);
    target.noFriendTitle = Utils.findRequiredViewAsType(source, R.id.no_friend_title, "field 'noFriendTitle'", TextView.class);
    target.tripNameProgressBar = Utils.findRequiredViewAsType(source, R.id.trip_name_progress_bar, "field 'tripNameProgressBar'", ProgressBar.class);
    target.addMeToTrip = Utils.findRequiredViewAsType(source, R.id.addme_to_trip, "field 'addMeToTrip'", LinearLayout.class);
    target.publicPrivateInfo = Utils.findRequiredViewAsType(source, R.id.public_trip_layout, "field 'publicPrivateInfo'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyTripInfoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cityImageView = null;
    target.cityName = null;
    target.tripDate = null;
    target.addNewFriend = null;
    target.listView = null;
    target.friendEmail = null;
    target.tripName = null;
    target.friendTitle = null;
    target.showIcon = null;
    target.editTrip = null;
    target.tripPublicMessage = null;
    target.publicToggleButton = null;
    target.details = null;
    target.animationView = null;
    target.layout = null;
    target.noFriendTitle = null;
    target.tripNameProgressBar = null;
    target.addMeToTrip = null;
    target.publicPrivateInfo = null;

    view2131361903.setOnClickListener(null);
    view2131361903 = null;
    view2131361846.setOnClickListener(null);
    view2131361846 = null;
    ((TextView) view2131362041).removeTextChangedListener(view2131362041TextWatcher);
    view2131362041TextWatcher = null;
    view2131362041 = null;
  }
}
