// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CurrencyConverterAdapter$ViewHolder_ViewBinding implements Unbinder {
  private CurrencyConverterAdapter.ViewHolder target;

  @UiThread
  public CurrencyConverterAdapter$ViewHolder_ViewBinding(CurrencyConverterAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.mShortTitle = Utils.findRequiredViewAsType(source, R.id.title, "field 'mShortTitle'", TextView.class);
    target.mImageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'mImageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CurrencyConverterAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mShortTitle = null;
    target.mImageView = null;
  }
}
