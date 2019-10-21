// Generated code from Butter Knife. Do not modify!
package adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RestaurantsCardViewAdapter$UtilityOptionsViewHolder_ViewBinding implements Unbinder {
  private RestaurantsCardViewAdapter.UtilityOptionsViewHolder target;

  @UiThread
  public RestaurantsCardViewAdapter$UtilityOptionsViewHolder_ViewBinding(RestaurantsCardViewAdapter.UtilityOptionsViewHolder target,
      View source) {
    this.target = target;

    target.content = Utils.findRequiredViewAsType(source, R.id.item_content, "field 'content'", LinearLayout.class);
    target.optionImage = Utils.findRequiredViewAsType(source, R.id.image, "field 'optionImage'", ImageView.class);
    target.optionName = Utils.findRequiredViewAsType(source, R.id.restaurant_name, "field 'optionName'", TextView.class);
    target.optionAddress = Utils.findRequiredViewAsType(source, R.id.restaurant_address, "field 'optionAddress'", TextView.class);
    target.optionRatings = Utils.findRequiredViewAsType(source, R.id.ratings, "field 'optionRatings'", TextView.class);
    target.optionAvgCost = Utils.findRequiredViewAsType(source, R.id.avg_cost, "field 'optionAvgCost'", TextView.class);
    target.optionVotes = Utils.findRequiredViewAsType(source, R.id.votes, "field 'optionVotes'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RestaurantsCardViewAdapter.UtilityOptionsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.content = null;
    target.optionImage = null;
    target.optionName = null;
    target.optionAddress = null;
    target.optionRatings = null;
    target.optionAvgCost = null;
    target.optionVotes = null;
  }
}
