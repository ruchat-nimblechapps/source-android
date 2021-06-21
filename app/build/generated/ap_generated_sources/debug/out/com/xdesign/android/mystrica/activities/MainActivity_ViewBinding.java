// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import com.xdesign.android.mystrica.R;
import com.xdesign.android.mystrica.views.ControlView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view7f09011d;

  private View view7f09011e;

  private View view7f09011a;

  private View view7f09011f;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.myst_group_device, "field 'deviceGroup' and method 'deviceClicked'");
    target.deviceGroup = view;
    view7f09011d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deviceClicked();
      }
    });
    target.deviceTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_device, "field 'deviceTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.myst_group_duration, "field 'durationGroup' and method 'durationClicked'");
    target.durationGroup = view;
    view7f09011e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.durationClicked();
      }
    });
    target.durationImageView = Utils.findRequiredViewAsType(source, R.id.myst_image_view_duration, "field 'durationImageView'", ImageView.class);
    target.tabLayout = Utils.findRequiredViewAsType(source, R.id.myst_view_tabs, "field 'tabLayout'", TabLayout.class);
    target.fragmentPager = Utils.findOptionalViewAsType(source, R.id.myst_view_pager, "field 'fragmentPager'", ViewPager.class);
    target.controlView = Utils.findRequiredViewAsType(source, R.id.myst_view_control, "field 'controlView'", ControlView.class);
    view = Utils.findRequiredView(source, R.id.myst_group_about, "method 'aboutClicked'");
    view7f09011a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.aboutClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_group_frequency, "method 'frequencyClicked'");
    view7f09011f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.frequencyClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.deviceGroup = null;
    target.deviceTextView = null;
    target.durationGroup = null;
    target.durationImageView = null;
    target.tabLayout = null;
    target.fragmentPager = null;
    target.controlView = null;

    view7f09011d.setOnClickListener(null);
    view7f09011d = null;
    view7f09011e.setOnClickListener(null);
    view7f09011e = null;
    view7f09011a.setOnClickListener(null);
    view7f09011a = null;
    view7f09011f.setOnClickListener(null);
    view7f09011f = null;
  }
}
