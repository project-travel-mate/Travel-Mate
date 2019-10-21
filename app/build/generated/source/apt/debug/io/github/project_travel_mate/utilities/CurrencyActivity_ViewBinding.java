// Generated code from Butter Knife. Do not modify!
package io.github.project_travel_mate.utilities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.charts.LineChart;
import io.github.project_travel_mate.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CurrencyActivity_ViewBinding implements Unbinder {
  private CurrencyActivity target;

  private View view2131361891;

  private View view2131362048;

  private View view2131362434;

  private View view2131361877;

  @UiThread
  public CurrencyActivity_ViewBinding(CurrencyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CurrencyActivity_ViewBinding(final CurrencyActivity target, View source) {
    this.target = target;

    View view;
    target.animationView = Utils.findRequiredViewAsType(source, R.id.animation_view, "field 'animationView'", LottieAnimationView.class);
    target.actual_layout = Utils.findRequiredView(source, R.id.actual_layout, "field 'actual_layout'");
    target.from_image = Utils.findRequiredViewAsType(source, R.id.first_country_image, "field 'from_image'", ImageView.class);
    target.to_image = Utils.findRequiredViewAsType(source, R.id.second_country_flag, "field 'to_image'", ImageView.class);
    target.from_edittext = Utils.findRequiredViewAsType(source, R.id.first_country_edittext, "field 'from_edittext'", EditText.class);
    target.result_textview = Utils.findRequiredViewAsType(source, R.id.text_result, "field 'result_textview'", TextView.class);
    target.from_country_name = Utils.findRequiredViewAsType(source, R.id.first_country_name, "field 'from_country_name'", TextView.class);
    target.to_country_name = Utils.findRequiredViewAsType(source, R.id.second_country_name, "field 'to_country_name'", TextView.class);
    target.graph = Utils.findRequiredViewAsType(source, R.id.graph, "field 'graph'", LineChart.class);
    view = Utils.findRequiredView(source, R.id.chart_duration_spinner, "field 'chart_duration_spinner' and method 'onChartDurationSpinnerClicked'");
    target.chart_duration_spinner = Utils.castView(view, R.id.chart_duration_spinner, "field 'chart_duration_spinner'", Spinner.class);
    view2131361891 = view;
    ((AdapterView<?>) view).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> p0, View p1, int p2, long p3) {
        target.onChartDurationSpinnerClicked();
      }

      @Override
      public void onNothingSelected(AdapterView<?> p0) {
      }
    });
    view = Utils.findRequiredView(source, R.id.from_field, "method 'fromSelected'");
    view2131362048 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.fromSelected();
      }
    });
    view = Utils.findRequiredView(source, R.id.to_field, "method 'toSelected'");
    view2131362434 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.toSelected();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_convert, "method 'onConvertclicked'");
    view2131361877 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onConvertclicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CurrencyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.animationView = null;
    target.actual_layout = null;
    target.from_image = null;
    target.to_image = null;
    target.from_edittext = null;
    target.result_textview = null;
    target.from_country_name = null;
    target.to_country_name = null;
    target.graph = null;
    target.chart_duration_spinner = null;

    ((AdapterView<?>) view2131361891).setOnItemSelectedListener(null);
    view2131361891 = null;
    view2131362048.setOnClickListener(null);
    view2131362048 = null;
    view2131362434.setOnClickListener(null);
    view2131362434 = null;
    view2131361877.setOnClickListener(null);
    view2131361877 = null;
  }
}
