// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.views;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RenameDeviceView_ViewBinding implements Unbinder {
  private RenameDeviceView target;

  @UiThread
  public RenameDeviceView_ViewBinding(RenameDeviceView target) {
    this(target, target);
  }

  @UiThread
  public RenameDeviceView_ViewBinding(RenameDeviceView target, View source) {
    this.target = target;

    target.nameEditText = Utils.findRequiredViewAsType(source, R.id.myst_edit_text_name, "field 'nameEditText'", EditText.class);
    target.passwordEditText = Utils.findRequiredViewAsType(source, R.id.myst_edit_text_password, "field 'passwordEditText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RenameDeviceView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nameEditText = null;
    target.passwordEditText = null;
  }
}
