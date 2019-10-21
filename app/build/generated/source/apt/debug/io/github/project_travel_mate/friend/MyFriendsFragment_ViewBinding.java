// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.friend;

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

public class MyFriendsFragment_ViewBinding implements Unbinder {
  private MyFriendsFragment target;

  @UiThread
  public MyFriendsFragment_ViewBinding(MyFriendsFragment target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFriendsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.animationView = null;
  }
}
