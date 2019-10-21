// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChecklistFragment_ViewBinding implements Unbinder {
  private ChecklistFragment target;

  private View view2131362137;

  @UiThread
  public ChecklistFragment_ViewBinding(final ChecklistFragment target, View source) {
    this.target = target;

    View view;
    target.mPendingRecycler = Utils.findRequiredViewAsType(source, R.id.recycler_pending, "field 'mPendingRecycler'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.layout_divider, "field 'mTickedBanner' and method 'onTickedBannerClick'");
    target.mTickedBanner = Utils.castView(view, R.id.layout_divider, "field 'mTickedBanner'", LinearLayout.class);
    view2131362137 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTickedBannerClick();
      }
    });
    target.mTickedCounter = Utils.findRequiredViewAsType(source, R.id.divider_tv_count, "field 'mTickedCounter'", TextView.class);
    target.mBannerArrow = Utils.findRequiredViewAsType(source, R.id.divider_ic_arrow, "field 'mBannerArrow'", ImageView.class);
    target.mFinishedRecycler = Utils.findRequiredViewAsType(source, R.id.recycler_finished, "field 'mFinishedRecycler'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChecklistFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mPendingRecycler = null;
    target.mTickedBanner = null;
    target.mTickedCounter = null;
    target.mBannerArrow = null;
    target.mFinishedRecycler = null;

    view2131362137.setOnClickListener(null);
    view2131362137 = null;
  }
}
