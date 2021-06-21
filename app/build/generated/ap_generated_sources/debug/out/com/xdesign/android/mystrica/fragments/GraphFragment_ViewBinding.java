// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.fragments;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GraphFragment_ViewBinding implements Unbinder {
  private GraphFragment target;

  @UiThread
  public GraphFragment_ViewBinding(GraphFragment target, View source) {
    this.target = target;

    target.bestFitTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_best_fit, "field 'bestFitTextView'", TextView.class);
    target.graph = Utils.findRequiredViewAsType(source, R.id.myst_line_graph, "field 'graph'", LineChart.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GraphFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bestFitTextView = null;
    target.graph = null;
  }
}
