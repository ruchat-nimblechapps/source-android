// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.views;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AboutView_ViewBinding implements Unbinder {
  private AboutView target;

  @UiThread
  public AboutView_ViewBinding(AboutView target) {
    this(target, target);
  }

  @UiThread
  public AboutView_ViewBinding(AboutView target, View source) {
    this.target = target;

    target.buildTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_build, "field 'buildTextView'", TextView.class);
    target.licenseTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_licences, "field 'licenseTextView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.buildTextView = null;
    target.licenseTextView = null;
  }
}
