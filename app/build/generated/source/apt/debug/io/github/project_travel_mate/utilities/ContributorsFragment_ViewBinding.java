// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import utils.ExpandableHeightGridView;

public class ContributorsFragment_ViewBinding implements Unbinder {
  private ContributorsFragment target;

  private View view2131361927;

  @UiThread
  public ContributorsFragment_ViewBinding(final ContributorsFragment target, View source) {
    this.target = target;

    View view;
    target.android_contributors_gv = Utils.findRequiredViewAsType(source, R.id.android_contributors_gv, "field 'android_contributors_gv'", ExpandableHeightGridView.class);
    target.server_contributors_gv = Utils.findRequiredViewAsType(source, R.id.server_contributors_gv, "field 'server_contributors_gv'", ExpandableHeightGridView.class);
    view = Utils.findRequiredView(source, R.id.contributors_footer, "method 'github_project_cardview_clicked'");
    view2131361927 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.github_project_cardview_clicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ContributorsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.android_contributors_gv = null;
    target.server_contributors_gv = null;

    view2131361927.setOnClickListener(null);
    view2131361927 = null;
  }
}
