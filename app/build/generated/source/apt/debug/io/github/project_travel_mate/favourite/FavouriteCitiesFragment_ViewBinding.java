// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.favourite;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FavouriteCitiesFragment_ViewBinding implements Unbinder {
  private FavouriteCitiesFragment target;

  @UiThread
  public FavouriteCitiesFragment_ViewBinding(FavouriteCitiesFragment target, View source) {
    this.target = target;

    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.fav_cities_lv = Utils.findRequiredViewAsType(source, R.id.fav_cities_list, "field 'fav_cities_lv'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FavouriteCitiesFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.fav_cities_lv = null;
  }
}
