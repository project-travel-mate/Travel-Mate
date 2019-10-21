// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WeatherForecastActivity_ViewBinding implements Unbinder {
  private WeatherForecastActivity target;

  @UiThread
  public WeatherForecastActivity_ViewBinding(WeatherForecastActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WeatherForecastActivity_ViewBinding(WeatherForecastActivity target, View source) {
    this.target = target;

    target.selectCity = Utils.findRequiredViewAsType(source, R.id.select_city, "field 'selectCity'", Button.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WeatherForecastActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.selectCity = null;
    target.animationView = null;
  }
}
