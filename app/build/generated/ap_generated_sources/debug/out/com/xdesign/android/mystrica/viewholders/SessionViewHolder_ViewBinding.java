// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.viewholders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SessionViewHolder_ViewBinding implements Unbinder {
  private SessionViewHolder target;

  private View view7f090110;

  private View view7f090111;

  private View view7f09010f;

  @UiThread
  public SessionViewHolder_ViewBinding(final SessionViewHolder target, View source) {
    this.target = target;

    View view;
    target.filenameTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_filename, "field 'filenameTextView'", TextView.class);
    target.modifiedTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_modfied, "field 'modifiedTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.myst_button_edit, "method 'editClicked'");
    view7f090110 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.editClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_button_email, "method 'emailClicked'");
    view7f090111 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.emailClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_button_delete, "method 'deleteClicked'");
    view7f09010f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deleteClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SessionViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.filenameTextView = null;
    target.modifiedTextView = null;

    view7f090110.setOnClickListener(null);
    view7f090110 = null;
    view7f090111.setOnClickListener(null);
    view7f090111 = null;
    view7f09010f.setOnClickListener(null);
    view7f09010f = null;
  }
}
