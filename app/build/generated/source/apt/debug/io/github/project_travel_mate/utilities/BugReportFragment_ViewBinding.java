// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BugReportFragment_ViewBinding implements Unbinder {
  private BugReportFragment target;

  @UiThread
  public BugReportFragment_ViewBinding(BugReportFragment target, View source) {
    this.target = target;

    target.issuesList = Utils.findRequiredViewAsType(source, R.id.issues_list, "field 'issuesList'", ListView.class);
    target.addButton = Utils.findRequiredViewAsType(source, R.id.add_feedback, "field 'addButton'", FloatingActionButton.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.text_view, "field 'textView'", TextView.class);
    target.listViewTitle = Utils.findRequiredViewAsType(source, R.id.listview_title, "field 'listViewTitle'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BugReportFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.issuesList = null;
    target.addButton = null;
    target.animationView = null;
    target.textView = null;
    target.listViewTitle = null;
  }
}
