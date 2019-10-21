// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MapViewRealTimeActivity_ViewBinding implements Unbinder {
  private MapViewRealTimeActivity target;

  @UiThread
  public MapViewRealTimeActivity_ViewBinding(MapViewRealTimeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MapViewRealTimeActivity_ViewBinding(MapViewRealTimeActivity target, View source) {
    this.target = target;

    target.scrollView = Utils.findRequiredViewAsType(source, R.id.data, "field 'scrollView'", ScrollView.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation, "field 'animationView'", LottieAnimationView.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MapViewRealTimeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.scrollView = null;
    target.animationView = null;
    target.layout = null;
  }
}
