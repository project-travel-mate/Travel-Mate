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

public class TweetsDescriptionActivity_ViewBinding implements Unbinder {
  private TweetsDescriptionActivity target;

  @UiThread
  public TweetsDescriptionActivity_ViewBinding(TweetsDescriptionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TweetsDescriptionActivity_ViewBinding(TweetsDescriptionActivity target, View source) {
    this.target = target;

    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.tweets_recycler_view, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TweetsDescriptionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.recyclerView = null;
  }
}
