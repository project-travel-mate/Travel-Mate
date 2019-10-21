// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlacesOnMapActivity$PlacesOnMapAdapter$ViewHolder_ViewBinding implements Unbinder {
  private PlacesOnMapActivity.PlacesOnMapAdapter.ViewHolder target;

  @UiThread
  public PlacesOnMapActivity$PlacesOnMapAdapter$ViewHolder_ViewBinding(PlacesOnMapActivity.PlacesOnMapAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.item_name, "field 'title'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.item_address, "field 'description'", TextView.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.image, "field 'imageView'", ImageView.class);
    target.onMap = Utils.findRequiredViewAsType(source, R.id.map, "field 'onMap'", LinearLayout.class);
    target.linearLayout = Utils.findRequiredViewAsType(source, R.id.know_more_layout, "field 'linearLayout'", LinearLayout.class);
    target.completeLayout = Utils.findRequiredViewAsType(source, R.id.city_info_item_layout, "field 'completeLayout'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlacesOnMapActivity.PlacesOnMapAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.description = null;
    target.imageView = null;
    target.onMap = null;
    target.linearLayout = null;
    target.completeLayout = null;
  }
}
