// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.searchcitydialog;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CitySearchBottomSheetDialogFragment_ViewBinding implements Unbinder {
  private CitySearchBottomSheetDialogFragment target;

  @UiThread
  public CitySearchBottomSheetDialogFragment_ViewBinding(CitySearchBottomSheetDialogFragment target,
      View source) {
    this.target = target;

    target.rvCities = Utils.findRequiredViewAsType(source, R.id.rv_cities, "field 'rvCities'", RecyclerView.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'tvTitle'", TextView.class);
    target.etQuery = Utils.findRequiredViewAsType(source, R.id.et_query, "field 'etQuery'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CitySearchBottomSheetDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvCities = null;
    target.tvTitle = null;
    target.etQuery = null;
  }
}
