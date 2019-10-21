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
import utils.ExpandableTextView;

public class CityHistoryAdapter$ViewHolder_ViewBinding implements Unbinder {
  private CityHistoryAdapter.ViewHolder target;

  @UiThread
  public CityHistoryAdapter$ViewHolder_ViewBinding(CityHistoryAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.text = Utils.findRequiredViewAsType(source, R.id.text, "field 'text'", ExpandableTextView.class);
    target.heading = Utils.findRequiredViewAsType(source, R.id.heading, "field 'heading'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CityHistoryAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.text = null;
    target.heading = null;
  }
}
