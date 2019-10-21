// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.searchcitydialog;

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

public class CitySearchBottomSheetDialogFragment$ViewHolder_ViewBinding implements Unbinder {
  private CitySearchBottomSheetDialogFragment.ViewHolder target;

  @UiThread
  public CitySearchBottomSheetDialogFragment$ViewHolder_ViewBinding(CitySearchBottomSheetDialogFragment.ViewHolder target,
      View source) {
    this.target = target;

    target.civCityImage = Utils.findRequiredViewAsType(source, R.id.hotel_city_image, "field 'civCityImage'", CircleImageView.class);
    target.tvCityName = Utils.findRequiredViewAsType(source, R.id.hotel_city_name, "field 'tvCityName'", TextView.class);
    target.tvCityId = Utils.findRequiredViewAsType(source, R.id.hotel_city_id, "field 'tvCityId'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CitySearchBottomSheetDialogFragment.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.civCityImage = null;
    target.tvCityName = null;
    target.tvCityId = null;
  }
}
