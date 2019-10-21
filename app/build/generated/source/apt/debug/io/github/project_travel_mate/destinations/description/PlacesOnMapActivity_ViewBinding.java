// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlacesOnMapActivity_ViewBinding implements Unbinder {
  private PlacesOnMapActivity target;

  @UiThread
  public PlacesOnMapActivity_ViewBinding(PlacesOnMapActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlacesOnMapActivity_ViewBinding(PlacesOnMapActivity target, View source) {
    this.target = target;

    target.editTextSearch = Utils.findRequiredViewAsType(source, R.id.editTextSearch, "field 'editTextSearch'", EditText.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.lv, "field 'recyclerView'", RecyclerView.class);
    target.textViewNoItems = Utils.findRequiredViewAsType(source, R.id.textViewNoItems, "field 'textViewNoItems'", TextView.class);
    target.selectedItemName = Utils.findRequiredViewAsType(source, R.id.place_name, "field 'selectedItemName'", TextView.class);
    target.selectedItemAddress = Utils.findRequiredViewAsType(source, R.id.place_address, "field 'selectedItemAddress'", TextView.class);
    target.linearLayout = Utils.findRequiredViewAsType(source, R.id.item_info, "field 'linearLayout'", LinearLayout.class);
    target.layoutBottomSheet = Utils.findRequiredViewAsType(source, R.id.bottom_sheet, "field 'layoutBottomSheet'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PlacesOnMapActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editTextSearch = null;
    target.recyclerView = null;
    target.textViewNoItems = null;
    target.selectedItemName = null;
    target.selectedItemAddress = null;
    target.linearLayout = null;
    target.layoutBottomSheet = null;
  }
}
