// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

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

public class TweetsRecyclerAdapter$ViewHolder_ViewBinding implements Unbinder {
  private TweetsRecyclerAdapter.ViewHolder target;

  @UiThread
  public TweetsRecyclerAdapter$ViewHolder_ViewBinding(TweetsRecyclerAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.name = Utils.findRequiredViewAsType(source, R.id.tagmain, "field 'name'", TextView.class);
    target.ivType = Utils.findRequiredViewAsType(source, R.id.iv_type, "field 'ivType'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TweetsRecyclerAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.name = null;
    target.ivType = null;
  }
}
