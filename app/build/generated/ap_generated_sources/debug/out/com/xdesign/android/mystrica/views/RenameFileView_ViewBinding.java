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

public class RenameFileView_ViewBinding implements Unbinder {
  private RenameFileView target;

  @UiThread
  public RenameFileView_ViewBinding(RenameFileView target) {
    this(target, target);
  }

  @UiThread
  public RenameFileView_ViewBinding(RenameFileView target, View source) {
    this.target = target;

    target.nameEditText = Utils.findRequiredViewAsType(source, R.id.myst_edit_text_name, "field 'nameEditText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RenameFileView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nameEditText = null;
  }
}
