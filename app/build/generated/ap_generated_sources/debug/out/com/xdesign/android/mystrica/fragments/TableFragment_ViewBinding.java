// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TableFragment_ViewBinding implements Unbinder {
  private TableFragment target;

  private View view7f090127;

  @UiThread
  public TableFragment_ViewBinding(final TableFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.myst_list_view_results, "field 'resultsListView' and method 'onItemClicked'");
    target.resultsListView = Utils.castView(view, R.id.myst_list_view_results, "field 'resultsListView'", ListView.class);
    view7f090127 = view;
    ((AdapterView<?>) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> p0, View p1, int p2, long p3) {
        target.onItemClicked(p2);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    TableFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.resultsListView = null;

    ((AdapterView<?>) view7f090127).setOnItemClickListener(null);
    view7f090127 = null;
  }
}
