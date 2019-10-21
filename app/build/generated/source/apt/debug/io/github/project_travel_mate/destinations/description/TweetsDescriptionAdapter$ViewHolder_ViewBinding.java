// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import utils.CircleImageView;

public class TweetsDescriptionAdapter$ViewHolder_ViewBinding implements Unbinder {
  private TweetsDescriptionAdapter.ViewHolder target;

  @UiThread
  public TweetsDescriptionAdapter$ViewHolder_ViewBinding(TweetsDescriptionAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.profileImage = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'profileImage'", CircleImageView.class);
    target.username = Utils.findRequiredViewAsType(source, R.id.user_full_name, "field 'username'", TextView.class);
    target.userScreenName = Utils.findRequiredViewAsType(source, R.id.user_screen_name, "field 'userScreenName'", TextView.class);
    target.createdAt = Utils.findRequiredViewAsType(source, R.id.created_at, "field 'createdAt'", TextView.class);
    target.retweetsCount = Utils.findRequiredViewAsType(source, R.id.retweets_count, "field 'retweetsCount'", TextView.class);
    target.favCount = Utils.findRequiredViewAsType(source, R.id.fav_count, "field 'favCount'", TextView.class);
    target.tweetText = Utils.findRequiredViewAsType(source, R.id.tweet_text, "field 'tweetText'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TweetsDescriptionAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.profileImage = null;
    target.username = null;
    target.userScreenName = null;
    target.createdAt = null;
    target.retweetsCount = null;
    target.favCount = null;
    target.tweetText = null;
  }
}
