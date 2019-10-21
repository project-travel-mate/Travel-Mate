// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.mytrips;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.dd.processbutton.FlatButton;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddNewTripActivity_ViewBinding implements Unbinder {
  private AddNewTripActivity target;

  @UiThread
  public AddNewTripActivity_ViewBinding(AddNewTripActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddNewTripActivity_ViewBinding(AddNewTripActivity target, View source) {
    this.target = target;

    target.cityName = Utils.findRequiredViewAsType(source, R.id.select_city_name, "field 'cityName'", TextView.class);
    target.tripStartDate = Utils.findRequiredViewAsType(source, R.id.sdate, "field 'tripStartDate'", TextView.class);
    target.ok = Utils.findRequiredViewAsType(source, R.id.ok, "field 'ok'", FlatButton.class);
    target.tripName = Utils.findRequiredViewAsType(source, R.id.tname, "field 'tripName'", EditText.class);
    target.mLinearLayout = Utils.findRequiredViewAsType(source, R.id.linear_layout, "field 'mLinearLayout'", LinearLayout.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AddNewTripActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cityName = null;
    target.tripStartDate = null;
    target.ok = null;
    target.tripName = null;
    target.mLinearLayout = null;
    target.animationView = null;
  }
}
