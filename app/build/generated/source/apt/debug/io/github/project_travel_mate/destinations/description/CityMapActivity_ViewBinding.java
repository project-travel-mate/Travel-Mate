// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import org.osmdroid.views.MapView;

public class CityMapActivity_ViewBinding implements Unbinder {
  private CityMapActivity target;

  @UiThread
  public CityMapActivity_ViewBinding(CityMapActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CityMapActivity_ViewBinding(CityMapActivity target, View source) {
    this.target = target;

    target.cityMap = Utils.findRequiredViewAsType(source, R.id.mv_city_map, "field 'cityMap'", MapView.class);
    target.saveMap = Utils.findRequiredViewAsType(source, R.id.fab_save_map, "field 'saveMap'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CityMapActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cityMap = null;
    target.saveMap = null;
  }
}
