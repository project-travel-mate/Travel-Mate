// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel;

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

public class ShoppingAdapter$ViewHolder_ViewBinding implements Unbinder {
  private ShoppingAdapter.ViewHolder target;

  @UiThread
  public ShoppingAdapter$ViewHolder_ViewBinding(ShoppingAdapter.ViewHolder target, View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.VideoTitle, "field 'title'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.VideoDescription, "field 'description'", TextView.class);
    target.iv = Utils.findRequiredViewAsType(source, R.id.VideoThumbnail, "field 'iv'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShoppingAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.description = null;
    target.iv = null;
  }
}
