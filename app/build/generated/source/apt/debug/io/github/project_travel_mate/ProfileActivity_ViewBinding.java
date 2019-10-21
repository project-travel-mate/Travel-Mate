// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import java.lang.IllegalStateException;
import java.lang.Override;
import utils.CircleImageView;

public class ProfileActivity_ViewBinding implements Unbinder {
  private ProfileActivity target;

  @UiThread
  public ProfileActivity_ViewBinding(ProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProfileActivity_ViewBinding(ProfileActivity target, View source) {
    this.target = target;

    target.horizontalProgressBar = Utils.findRequiredViewAsType(source, R.id.horizontalProgressBar, "field 'horizontalProgressBar'", ProgressBar.class);
    target.displayImage = Utils.findRequiredViewAsType(source, R.id.display_image, "field 'displayImage'", CircleImageView.class);
    target.changeImage = Utils.findRequiredViewAsType(source, R.id.change_image, "field 'changeImage'", CircleImageView.class);
    target.displayName = Utils.findRequiredViewAsType(source, R.id.display_name, "field 'displayName'", EditText.class);
    target.emailId = Utils.findRequiredViewAsType(source, R.id.display_email, "field 'emailId'", TextView.class);
    target.isVerified = Utils.findRequiredViewAsType(source, R.id.is_email_verified, "field 'isVerified'", ImageView.class);
    target.joiningDate = Utils.findRequiredViewAsType(source, R.id.display_joining_date, "field 'joiningDate'", TextView.class);
    target.displayStatus = Utils.findRequiredViewAsType(source, R.id.display_status, "field 'displayStatus'", EditText.class);
    target.editDisplayName = Utils.findRequiredViewAsType(source, R.id.ib_edit_display_name, "field 'editDisplayName'", ImageButton.class);
    target.editDisplayStatus = Utils.findRequiredViewAsType(source, R.id.ib_edit_display_status, "field 'editDisplayStatus'", ImageButton.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.statusProgressBar = Utils.findRequiredViewAsType(source, R.id.status_progress_bar, "field 'statusProgressBar'", ProgressBar.class);
    target.nameProgressBar = Utils.findRequiredViewAsType(source, R.id.name_progress_bar, "field 'nameProgressBar'", ProgressBar.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", ConstraintLayout.class);
    target.characterCount = Utils.findRequiredViewAsType(source, R.id.status_character_count, "field 'characterCount'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.citiesTravelledHeading = Utils.findRequiredViewAsType(source, R.id.cities_travelled_text, "field 'citiesTravelledHeading'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.horizontalProgressBar = null;
    target.displayImage = null;
    target.changeImage = null;
    target.displayName = null;
    target.emailId = null;
    target.isVerified = null;
    target.joiningDate = null;
    target.displayStatus = null;
    target.editDisplayName = null;
    target.editDisplayStatus = null;
    target.animationView = null;
    target.statusProgressBar = null;
    target.nameProgressBar = null;
    target.layout = null;
    target.characterCount = null;
    target.recyclerView = null;
    target.citiesTravelledHeading = null;
  }
}
