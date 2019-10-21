// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HotelsActivity$HotelsAdapter$HotelsViewHolder_ViewBinding implements Unbinder {
  private HotelsActivity.HotelsAdapter.HotelsViewHolder target;

  @UiThread
  public HotelsActivity$HotelsAdapter$HotelsViewHolder_ViewBinding(HotelsActivity.HotelsAdapter.HotelsViewHolder target,
      View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.hotel_name, "field 'title'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.hotel_address, "field 'description'", TextView.class);
    target.call = Utils.findRequiredViewAsType(source, R.id.call, "field 'call'", RelativeLayout.class);
    target.map = Utils.findRequiredViewAsType(source, R.id.map, "field 'map'", RelativeLayout.class);
    target.book = Utils.findRequiredViewAsType(source, R.id.book, "field 'book'", RelativeLayout.class);
    target.detailsLayout = Utils.findRequiredViewAsType(source, R.id.more_details, "field 'detailsLayout'", LinearLayout.class);
    target.expandCollapse = Utils.findRequiredViewAsType(source, R.id.expand_collapse, "field 'expandCollapse'", ImageView.class);
    target.distance = Utils.findRequiredViewAsType(source, R.id.distance, "field 'distance'", TextView.class);
    target.expand_more_details = Utils.findRequiredViewAsType(source, R.id.expand_more_details, "field 'expand_more_details'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HotelsActivity.HotelsAdapter.HotelsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.description = null;
    target.call = null;
    target.map = null;
    target.book = null;
    target.detailsLayout = null;
    target.expandCollapse = null;
    target.distance = null;
    target.expand_more_details = null;
  }
}
