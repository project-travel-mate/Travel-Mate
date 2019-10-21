// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AboutUsFragment_ViewBinding implements Unbinder {
  private AboutUsFragment target;

  private View view2131361947;

  private View view2131361948;

  private View view2131361953;

  private View view2131361946;

  private View view2131361950;

  private View view2131361949;

  private View view2131361951;

  private View view2131361945;

  private View view2131362481;

  @UiThread
  public AboutUsFragment_ViewBinding(final AboutUsFragment target, View source) {
    this.target = target;

    View view;
    target.mVersionCode = Utils.findRequiredViewAsType(source, R.id.tv_version_code, "field 'mVersionCode'", TextView.class);
    view = Utils.findRequiredView(source, R.id.cv_fork, "method 'onForkClicked'");
    view2131361947 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onForkClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_privacy_policy, "method 'onPrivacyPolicyClicked'");
    view2131361948 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPrivacyPolicyClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_website, "method 'onWebsiteClicked'");
    view2131361953 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onWebsiteClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_contact_us, "method 'onContactUsClicked'");
    view2131361946 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onContactUsClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_share, "method 'onShareClicked'");
    view2131361950 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onShareClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_report_bug, "method 'onReportBugClicked'");
    view2131361949 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onReportBugClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_slack, "method 'onSlackClicked'");
    view2131361951 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSlackClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.cv_bmc, "method 'onBuyMeACoffeeClicked'");
    view2131361945 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBuyMeACoffeeClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.view_contributors, "method 'onViewContributorsClicked'");
    view2131362481 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewContributorsClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutUsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mVersionCode = null;

    view2131361947.setOnClickListener(null);
    view2131361947 = null;
    view2131361948.setOnClickListener(null);
    view2131361948 = null;
    view2131361953.setOnClickListener(null);
    view2131361953 = null;
    view2131361946.setOnClickListener(null);
    view2131361946 = null;
    view2131361950.setOnClickListener(null);
    view2131361950 = null;
    view2131361949.setOnClickListener(null);
    view2131361949 = null;
    view2131361951.setOnClickListener(null);
    view2131361951 = null;
    view2131361945.setOnClickListener(null);
    view2131361945 = null;
    view2131362481.setOnClickListener(null);
    view2131362481 = null;
  }
}
