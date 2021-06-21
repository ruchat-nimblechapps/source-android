// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.viewholders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LogRowViewHolder_ViewBinding implements Unbinder {
  private LogRowViewHolder target;

  @UiThread
  public LogRowViewHolder_ViewBinding(LogRowViewHolder target, View source) {
    this.target = target;

    target.background = Utils.findRequiredView(source, R.id.myst_view_background, "field 'background'");
    target.columnOneTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_column_one, "field 'columnOneTextView'", TextView.class);
    target.columnTwoTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_column_two, "field 'columnTwoTextView'", TextView.class);
    target.columnThreeTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_column_three, "field 'columnThreeTextView'", TextView.class);
    target.columnFourTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_column_four, "field 'columnFourTextView'", TextView.class);
    target.selectionLayer = Utils.findRequiredView(source, R.id.myst_layout_selection, "field 'selectionLayer'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LogRowViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.background = null;
    target.columnOneTextView = null;
    target.columnTwoTextView = null;
    target.columnThreeTextView = null;
    target.columnFourTextView = null;
    target.selectionLayer = null;
  }
}
