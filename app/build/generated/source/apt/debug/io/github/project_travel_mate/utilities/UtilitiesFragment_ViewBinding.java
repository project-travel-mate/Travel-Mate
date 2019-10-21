// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UtilitiesFragment_ViewBinding implements Unbinder {
  private UtilitiesFragment target;

  @UiThread
  public UtilitiesFragment_ViewBinding(UtilitiesFragment target, View source) {
    this.target = target;

    target.mUtilityOptionsRecycleView = Utils.findRequiredViewAsType(source, R.id.utility_options_recycle_view, "field 'mUtilityOptionsRecycleView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UtilitiesFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mUtilityOptionsRecycleView = null;
  }
}
