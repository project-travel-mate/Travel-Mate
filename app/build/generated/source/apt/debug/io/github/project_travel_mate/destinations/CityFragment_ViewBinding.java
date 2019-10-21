// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CityFragment_ViewBinding implements Unbinder {
  private CityFragment target;

  @UiThread
  public CityFragment_ViewBinding(CityFragment target, View source) {
    this.target = target;

    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.lv = Utils.findRequiredViewAsType(source, R.id.cities_list, "field 'lv'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CityFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.lv = null;
  }
}
