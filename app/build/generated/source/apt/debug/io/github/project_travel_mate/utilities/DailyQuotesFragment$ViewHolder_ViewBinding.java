// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.dd.processbutton.FlatButton;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DailyQuotesFragment$ViewHolder_ViewBinding implements Unbinder {
  private DailyQuotesFragment.ViewHolder target;

  @UiThread
  public DailyQuotesFragment$ViewHolder_ViewBinding(DailyQuotesFragment.ViewHolder target,
      View source) {
    this.target = target;

    target.rootLayout = Utils.findRequiredViewAsType(source, R.id.root_layout, "field 'rootLayout'", LinearLayout.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.quoteTv = Utils.findRequiredViewAsType(source, R.id.quote_textView, "field 'quoteTv'", TextView.class);
    target.authorTv = Utils.findRequiredViewAsType(source, R.id.author_textView, "field 'authorTv'", TextView.class);
    target.share = Utils.findRequiredViewAsType(source, R.id.fab, "field 'share'", FloatingActionButton.class);
    target.continueButton = Utils.findRequiredViewAsType(source, R.id.continue_button, "field 'continueButton'", FlatButton.class);
    target.quotesCheckBox = Utils.findRequiredViewAsType(source, R.id.dont_show_quotes_checkBox, "field 'quotesCheckBox'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DailyQuotesFragment.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rootLayout = null;
    target.animationView = null;
    target.quoteTv = null;
    target.authorTv = null;
    target.share = null;
    target.continueButton = null;
    target.quotesCheckBox = null;
  }
}
