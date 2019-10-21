// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel.swipefragmentrealtime;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MapListItemAdapter$ViewHolder_ViewBinding implements Unbinder {
  private MapListItemAdapter.ViewHolder target;

  @UiThread
  public MapListItemAdapter$ViewHolder_ViewBinding(MapListItemAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.item_title, "field 'title'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.item_description, "field 'description'", TextView.class);
    target.call = Utils.findRequiredViewAsType(source, R.id.call, "field 'call'", Button.class);
    target.book = Utils.findRequiredViewAsType(source, R.id.book, "field 'book'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MapListItemAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.description = null;
    target.call = null;
    target.book = null;
  }
}
