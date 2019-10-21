// Generated code from Butter Knife. Do not modify!
package adapters;

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

public class CardViewOptionsAdapter$UtilityOptionsViewHolder_ViewBinding implements Unbinder {
  private CardViewOptionsAdapter.UtilityOptionsViewHolder target;

  @UiThread
  public CardViewOptionsAdapter$UtilityOptionsViewHolder_ViewBinding(CardViewOptionsAdapter.UtilityOptionsViewHolder target,
      View source) {
    this.target = target;

    target.optionImage = Utils.findRequiredViewAsType(source, R.id.image, "field 'optionImage'", ImageView.class);
    target.optionName = Utils.findRequiredViewAsType(source, R.id.text, "field 'optionName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CardViewOptionsAdapter.UtilityOptionsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.optionImage = null;
    target.optionName = null;
  }
}
