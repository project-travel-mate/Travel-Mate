// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TweetsActivity_ViewBinding implements Unbinder {
  private TweetsActivity target;

  @UiThread
  public TweetsActivity_ViewBinding(TweetsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TweetsActivity_ViewBinding(TweetsActivity target, View source) {
    this.target = target;

    target.lv = Utils.findRequiredViewAsType(source, R.id.list, "field 'lv'", RecyclerView.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TweetsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.lv = null;
    target.animationView = null;
  }
}
