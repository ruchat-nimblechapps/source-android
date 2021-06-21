// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.views;

import android.view.View;
import android.widget.NumberPicker;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DurationPickerView_ViewBinding implements Unbinder {
  private DurationPickerView target;

  @UiThread
  public DurationPickerView_ViewBinding(DurationPickerView target) {
    this(target, target);
  }

  @UiThread
  public DurationPickerView_ViewBinding(DurationPickerView target, View source) {
    this.target = target;

    target.hoursPicker = Utils.findRequiredViewAsType(source, R.id.myst_number_picker_hour, "field 'hoursPicker'", NumberPicker.class);
    target.minutesPicker = Utils.findRequiredViewAsType(source, R.id.myst_number_picker_minute, "field 'minutesPicker'", NumberPicker.class);
    target.secondsPicker = Utils.findRequiredViewAsType(source, R.id.myst_number_picker_second, "field 'secondsPicker'", NumberPicker.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DurationPickerView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.hoursPicker = null;
    target.minutesPicker = null;
    target.secondsPicker = null;
  }
}
