// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.travel;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShoppingCurrentCityActivity_ViewBinding implements Unbinder {
  private ShoppingCurrentCityActivity target;

  private View view2131362052;

  @UiThread
  public ShoppingCurrentCityActivity_ViewBinding(ShoppingCurrentCityActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShoppingCurrentCityActivity_ViewBinding(final ShoppingCurrentCityActivity target,
      View source) {
    this.target = target;

    View view;
    target.lv = Utils.findRequiredViewAsType(source, R.id.shopping_list, "field 'lv'", ListView.class);
    target.q = Utils.findRequiredViewAsType(source, R.id.query, "field 'q'", EditText.class);
    view = Utils.findRequiredView(source, R.id.go, "field 'ok' and method 'onClick'");
    target.ok = Utils.castView(view, R.id.go, "field 'ok'", Button.class);
    view2131362052 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.textView = Utils.findRequiredViewAsType(source, R.id.text_view, "field 'textView'", TextView.class);
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", LinearLayout.class);
    target.showCityName = Utils.findRequiredViewAsType(source, R.id.show_city_name, "field 'showCityName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShoppingCurrentCityActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.lv = null;
    target.q = null;
    target.ok = null;
    target.textView = null;
    target.animationView = null;
    target.layout = null;
    target.showCityName = null;

    view2131362052.setOnClickListener(null);
    view2131362052 = null;
  }
}
