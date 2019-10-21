// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.friend;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyFriendsAdapter$MyFriendsViewHolder_ViewBinding implements Unbinder {
  private MyFriendsAdapter.MyFriendsViewHolder target;

  @UiThread
  public MyFriendsAdapter$MyFriendsViewHolder_ViewBinding(MyFriendsAdapter.MyFriendsViewHolder target,
      View source) {
    this.target = target;

    target.friendImage = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'friendImage'", ImageView.class);
    target.friendDisplayName = Utils.findRequiredViewAsType(source, R.id.friend_display_name, "field 'friendDisplayName'", TextView.class);
    target.my_friends_linear_layout = Utils.findRequiredViewAsType(source, R.id.my_friends_linear_layout, "field 'my_friends_linear_layout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFriendsAdapter.MyFriendsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.friendImage = null;
    target.friendDisplayName = null;
    target.my_friends_linear_layout = null;
  }
}
