// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.destinations.description;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WeatherActivity_ViewBinding implements Unbinder {
  private WeatherActivity target;

  @UiThread
  public WeatherActivity_ViewBinding(WeatherActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WeatherActivity_ViewBinding(WeatherActivity target, View source) {
    this.target = target;

    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.condition = Utils.findRequiredViewAsType(source, R.id.weather_condition, "field 'condition'", TextView.class);
    target.icon = Utils.findRequiredViewAsType(source, R.id.weather_icon, "field 'icon'", ImageView.class);
    target.temp = Utils.findRequiredViewAsType(source, R.id.temp, "field 'temp'", TextView.class);
    target.maxTemp = Utils.findRequiredViewAsType(source, R.id.min_temp, "field 'maxTemp'", TextView.class);
    target.minTemp = Utils.findRequiredViewAsType(source, R.id.max_temp, "field 'minTemp'", TextView.class);
    target.today = Utils.findRequiredViewAsType(source, R.id.today, "field 'today'", TextView.class);
    target.dayOfweek = Utils.findRequiredViewAsType(source, R.id.day_of_week, "field 'dayOfweek'", TextView.class);
    target.forecastList = Utils.findRequiredViewAsType(source, R.id.forecast_list, "field 'forecastList'", RecyclerView.class);
    target.emptyView = Utils.findRequiredViewAsType(source, R.id.empty_textview, "field 'emptyView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WeatherActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.condition = null;
    target.icon = null;
    target.temp = null;
    target.maxTemp = null;
    target.minTemp = null;
    target.today = null;
    target.dayOfweek = null;
    target.forecastList = null;
    target.emptyView = null;
  }
}
