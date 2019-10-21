// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

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

public class WeatherAdapter$WeatherHolder_ViewBinding implements Unbinder {
  private WeatherAdapter.WeatherHolder target;

  @UiThread
  public WeatherAdapter$WeatherHolder_ViewBinding(WeatherAdapter.WeatherHolder target,
      View source) {
    this.target = target;

    target.icon = Utils.findRequiredViewAsType(source, R.id.weather_icon, "field 'icon'", ImageView.class);
    target.dayOfWeek = Utils.findRequiredViewAsType(source, R.id.today, "field 'dayOfWeek'", TextView.class);
    target.minTemp = Utils.findRequiredViewAsType(source, R.id.min_temp, "field 'minTemp'", TextView.class);
    target.maxTemp = Utils.findRequiredViewAsType(source, R.id.max_temp, "field 'maxTemp'", TextView.class);
    target.date = Utils.findRequiredViewAsType(source, R.id.date, "field 'date'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WeatherAdapter.WeatherHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.icon = null;
    target.dayOfWeek = null;
    target.minTemp = null;
    target.maxTemp = null;
    target.date = null;
  }
}
