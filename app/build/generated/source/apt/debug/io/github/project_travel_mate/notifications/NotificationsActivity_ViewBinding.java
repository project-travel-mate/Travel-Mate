// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.notifications;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NotificationsActivity_ViewBinding implements Unbinder {
  private NotificationsActivity target;

  @UiThread
  public NotificationsActivity_ViewBinding(NotificationsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NotificationsActivity_ViewBinding(NotificationsActivity target, View source) {
    this.target = target;

    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.listView = Utils.findRequiredViewAsType(source, R.id.notification_list, "field 'listView'", ListView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NotificationsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.listView = null;
    target.swipeRefreshLayout = null;
  }
}
