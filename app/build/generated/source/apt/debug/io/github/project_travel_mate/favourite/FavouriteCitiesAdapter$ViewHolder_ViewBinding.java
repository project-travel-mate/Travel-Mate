// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.favourite;

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

public class FavouriteCitiesAdapter$ViewHolder_ViewBinding implements Unbinder {
  private FavouriteCitiesAdapter.ViewHolder target;

  @UiThread
  public FavouriteCitiesAdapter$ViewHolder_ViewBinding(FavouriteCitiesAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.fav_name = Utils.findRequiredViewAsType(source, R.id.fav_name, "field 'fav_name'", TextView.class);
    target.fav_image = Utils.findRequiredViewAsType(source, R.id.fav_image, "field 'fav_image'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FavouriteCitiesAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fav_name = null;
    target.fav_image = null;
  }
}
