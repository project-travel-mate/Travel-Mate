// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TimezoneListViewActivity_ViewBinding implements Unbinder {
  private TimezoneListViewActivity target;

  @UiThread
  public TimezoneListViewActivity_ViewBinding(TimezoneListViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TimezoneListViewActivity_ViewBinding(TimezoneListViewActivity target, View source) {
    this.target = target;

    target.mListview = Utils.findRequiredViewAsType(source, R.id.listView, "field 'mListview'", RecyclerView.class);
    target.mTimezoneSearch = Utils.findRequiredViewAsType(source, R.id.timezoneSearch, "field 'mTimezoneSearch'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TimezoneListViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mListview = null;
    target.mTimezoneSearch = null;
  }
}
