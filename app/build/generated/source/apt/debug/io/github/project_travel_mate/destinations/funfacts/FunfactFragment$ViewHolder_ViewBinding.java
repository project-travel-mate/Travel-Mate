// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.funfacts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FunfactFragment$ViewHolder_ViewBinding implements Unbinder {
  private FunfactFragment.ViewHolder target;

  @UiThread
  public FunfactFragment$ViewHolder_ViewBinding(FunfactFragment.ViewHolder target, View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.desc = Utils.findRequiredViewAsType(source, R.id.desc, "field 'desc'", TextView.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.imag, "field 'image'", ImageView.class);
    target.source = Utils.findRequiredViewAsType(source, R.id.source, "field 'source'", TextView.class);
    target.text_source = Utils.findRequiredViewAsType(source, R.id.text_source, "field 'text_source'", TextView.class);
    target.share = Utils.findRequiredViewAsType(source, R.id.fab, "field 'share'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FunfactFragment.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.desc = null;
    target.image = null;
    target.source = null;
    target.text_source = null;
    target.share = null;
  }
}
