// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.mytrips;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyTripFriendNameAdapter$ViewHolder_ViewBinding implements Unbinder {
  private MyTripFriendNameAdapter.ViewHolder target;

  @UiThread
  public MyTripFriendNameAdapter$ViewHolder_ViewBinding(MyTripFriendNameAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.name = Utils.findRequiredViewAsType(source, R.id.friend_name, "field 'name'", TextView.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.friend_profile_picture, "field 'imageView'", ImageView.class);
    target.removeFriend = Utils.findRequiredViewAsType(source, R.id.remove_friend, "field 'removeFriend'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyTripFriendNameAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.name = null;
    target.imageView = null;
    target.removeFriend = null;
  }
}
