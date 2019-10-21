// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddBugFragment_ViewBinding implements Unbinder {
  private AddBugFragment target;

  private View view2131361878;

  @UiThread
  public AddBugFragment_ViewBinding(final AddBugFragment target, View source) {
    this.target = target;

    View view;
    target.mBugTypeSpinner = Utils.findRequiredViewAsType(source, R.id.spinner_bug_type, "field 'mBugTypeSpinner'", Spinner.class);
    target.mDescriptionEditText = Utils.findRequiredViewAsType(source, R.id.edit_text_bugreport, "field 'mDescriptionEditText'", EditText.class);
    view = Utils.findRequiredView(source, R.id.button_report, "field 'mReportButton' and method 'onReportClicked'");
    target.mReportButton = Utils.castView(view, R.id.button_report, "field 'mReportButton'", Button.class);
    view2131361878 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onReportClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AddBugFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mBugTypeSpinner = null;
    target.mDescriptionEditText = null;
    target.mReportButton = null;

    view2131361878.setOnClickListener(null);
    view2131361878 = null;
  }
}
