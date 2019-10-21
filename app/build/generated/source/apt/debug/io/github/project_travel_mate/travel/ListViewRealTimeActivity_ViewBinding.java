// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ListViewRealTimeActivity_ViewBinding implements Unbinder {
  private ListViewRealTimeActivity target;

  @UiThread
  public ListViewRealTimeActivity_ViewBinding(ListViewRealTimeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ListViewRealTimeActivity_ViewBinding(ListViewRealTimeActivity target, View source) {
    this.target = target;

    target.tabLayout = Utils.findRequiredViewAsType(source, R.id.tabs, "field 'tabLayout'", TabLayout.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewpager, "field 'viewPager'", ViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ListViewRealTimeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tabLayout = null;
    target.viewPager = null;
  }
}
