// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.funfacts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FunFactsActivity_ViewBinding implements Unbinder {
  private FunFactsActivity target;

  @UiThread
  public FunFactsActivity_ViewBinding(FunFactsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FunFactsActivity_ViewBinding(FunFactsActivity target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.vp, "field 'viewPager'", ViewPager.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FunFactsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.animationView = null;
  }
}
