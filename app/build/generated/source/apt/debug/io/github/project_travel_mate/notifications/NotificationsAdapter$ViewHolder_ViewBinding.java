// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.notifications;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NotificationsAdapter$ViewHolder_ViewBinding implements Unbinder {
  private NotificationsAdapter.ViewHolder target;

  @UiThread
  public NotificationsAdapter$ViewHolder_ViewBinding(NotificationsAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.name = Utils.findRequiredViewAsType(source, R.id.text, "field 'name'", TextView.class);
    target.readStatus = Utils.findRequiredView(source, R.id.read_status, "field 'readStatus'");
    target.notificationTime = Utils.findRequiredViewAsType(source, R.id.notification_time, "field 'notificationTime'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NotificationsAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.name = null;
    target.readStatus = null;
    target.notificationTime = null;
  }
}
