// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BugReportAdapter$ViewHolder_ViewBinding implements Unbinder {
  private BugReportAdapter.ViewHolder target;

  @UiThread
  public BugReportAdapter$ViewHolder_ViewBinding(BugReportAdapter.ViewHolder target, View source) {
    this.target = target;

    target.issueType = Utils.findRequiredViewAsType(source, R.id.issue_type, "field 'issueType'", TextView.class);
    target.issueText = Utils.findRequiredViewAsType(source, R.id.issue_text, "field 'issueText'", TextView.class);
    target.issueDate = Utils.findRequiredViewAsType(source, R.id.issue_date, "field 'issueDate'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BugReportAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.issueType = null;
    target.issueText = null;
    target.issueDate = null;
  }
}
