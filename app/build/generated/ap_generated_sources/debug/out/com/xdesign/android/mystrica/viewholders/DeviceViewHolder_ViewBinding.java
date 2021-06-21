// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DeviceViewHolder_ViewBinding implements Unbinder {
  private DeviceViewHolder target;

  private View view7f090123;

  @UiThread
  public DeviceViewHolder_ViewBinding(final DeviceViewHolder target, View source) {
    this.target = target;

    View view;
    target.deviceTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_device_name, "field 'deviceTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.myst_image_view_edit, "field 'editImageView' and method 'editClicked'");
    target.editImageView = Utils.castView(view, R.id.myst_image_view_edit, "field 'editImageView'", ImageView.class);
    view7f090123 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.editClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    DeviceViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.deviceTextView = null;
    target.editImageView = null;

    view7f090123.setOnClickListener(null);
    view7f090123 = null;
  }
}
