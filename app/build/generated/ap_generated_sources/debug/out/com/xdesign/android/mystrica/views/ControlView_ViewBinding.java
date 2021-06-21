// Generated code from Butter Knife. Do not modify!
package com.xdesign.android.mystrica.views;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.xdesign.android.mystrica.R;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ControlView_ViewBinding implements Unbinder {
  private ControlView target;

  private View view7f09012e;

  private View view7f09012d;

  private View view7f09012c;

  private View view7f09012f;

  private View view7f09012b;

  private View view7f09010d;

  private View view7f090116;

  private TextWatcher view7f090116TextWatcher;

  private View view7f090114;

  private View view7f090124;

  private View view7f090112;

  private View view7f090113;

  private View view7f09010e;

  @UiThread
  public ControlView_ViewBinding(ControlView target) {
    this(target, target);
  }

  @UiThread
  public ControlView_ViewBinding(final ControlView target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.myst_radio_button_r, "field 'rRadioButton' and method 'rRadioButtonChanged'");
    target.rRadioButton = Utils.castView(view, R.id.myst_radio_button_r, "field 'rRadioButton'", RadioButton.class);
    view7f09012e = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.rRadioButtonChanged(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_radio_button_g, "field 'gRadioButton' and method 'gRadioButtonChanged'");
    target.gRadioButton = Utils.castView(view, R.id.myst_radio_button_g, "field 'gRadioButton'", RadioButton.class);
    view7f09012d = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.gRadioButtonChanged(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_radio_button_b, "field 'bRadioButton' and method 'bRadioButtonChanged'");
    target.bRadioButton = Utils.castView(view, R.id.myst_radio_button_b, "field 'bRadioButton'", RadioButton.class);
    view7f09012c = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.bRadioButtonChanged(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_radio_button_t, "field 'tRadioButton' and method 'tRadioButtonChanged'");
    target.tRadioButton = Utils.castView(view, R.id.myst_radio_button_t, "field 'tRadioButton'", RadioButton.class);
    view7f09012f = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.tRadioButtonChanged(p1);
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_radio_button_a, "field 'aRadioButton' and method 'aRadioButtonChanged'");
    target.aRadioButton = Utils.castView(view, R.id.myst_radio_button_a, "field 'aRadioButton'", RadioButton.class);
    view7f09012b = view;
    ((CompoundButton) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton p0, boolean p1) {
        target.aRadioButtonChanged(p1);
      }
    });
    target.readingTextView = Utils.findRequiredViewAsType(source, R.id.myst_text_view_reading, "field 'readingTextView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.myst_button_calibrate, "field 'calibrateButton' and method 'calibrateClicked'");
    target.calibrateButton = Utils.castView(view, R.id.myst_button_calibrate, "field 'calibrateButton'", TextView.class);
    view7f09010d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.calibrateClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_edit_text_note, "field 'viewNote' and method 'noteChanged'");
    target.viewNote = Utils.castView(view, R.id.myst_edit_text_note, "field 'viewNote'", EditText.class);
    view7f090116 = view;
    view7f090116TextWatcher = new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.noteChanged(p0);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    };
    ((TextView) view).addTextChangedListener(view7f090116TextWatcher);
    view = Utils.findRequiredView(source, R.id.myst_button_single_capture, "field 'singleCaptureButton' and method 'singleCaptureClicked'");
    target.singleCaptureButton = Utils.castView(view, R.id.myst_button_single_capture, "field 'singleCaptureButton'", ImageView.class);
    view7f090114 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.singleCaptureClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_image_view_start_stop, "field 'startStopButton' and method 'stopStartClicked'");
    target.startStopButton = Utils.castView(view, R.id.myst_image_view_start_stop, "field 'startStopButton'", ImageView.class);
    view7f090124 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.stopStartClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.myst_button_save, "field 'viewSave' and method 'saveClicked'");
    target.viewSave = view;
    view7f090112 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.saveClicked();
      }
    });
    target.textViewSave = Utils.findRequiredViewAsType(source, R.id.myst_text_view_save, "field 'textViewSave'", TextView.class);
    view = Utils.findRequiredView(source, R.id.myst_button_sessions, "field 'viewSessions' and method 'sessionsClicked'");
    target.viewSessions = view;
    view7f090113 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sessionsClicked();
      }
    });
    target.textViewSessions = Utils.findRequiredViewAsType(source, R.id.myst_text_view_sessions, "field 'textViewSessions'", TextView.class);
    view = Utils.findRequiredView(source, R.id.myst_button_clear, "field 'viewClear' and method 'clearClicked'");
    target.viewClear = view;
    view7f09010e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clearClicked();
      }
    });
    target.textViewClear = Utils.findRequiredViewAsType(source, R.id.myst_text_view_clear, "field 'textViewClear'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ControlView target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rRadioButton = null;
    target.gRadioButton = null;
    target.bRadioButton = null;
    target.tRadioButton = null;
    target.aRadioButton = null;
    target.readingTextView = null;
    target.calibrateButton = null;
    target.viewNote = null;
    target.singleCaptureButton = null;
    target.startStopButton = null;
    target.viewSave = null;
    target.textViewSave = null;
    target.viewSessions = null;
    target.textViewSessions = null;
    target.viewClear = null;
    target.textViewClear = null;

    ((CompoundButton) view7f09012e).setOnCheckedChangeListener(null);
    view7f09012e = null;
    ((CompoundButton) view7f09012d).setOnCheckedChangeListener(null);
    view7f09012d = null;
    ((CompoundButton) view7f09012c).setOnCheckedChangeListener(null);
    view7f09012c = null;
    ((CompoundButton) view7f09012f).setOnCheckedChangeListener(null);
    view7f09012f = null;
    ((CompoundButton) view7f09012b).setOnCheckedChangeListener(null);
    view7f09012b = null;
    view7f09010d.setOnClickListener(null);
    view7f09010d = null;
    ((TextView) view7f090116).removeTextChangedListener(view7f090116TextWatcher);
    view7f090116TextWatcher = null;
    view7f090116 = null;
    view7f090114.setOnClickListener(null);
    view7f090114 = null;
    view7f090124.setOnClickListener(null);
    view7f090124 = null;
    view7f090112.setOnClickListener(null);
    view7f090112 = null;
    view7f090113.setOnClickListener(null);
    view7f090113 = null;
    view7f09010e.setOnClickListener(null);
    view7f09010e = null;
  }
}
