package com.xdesign.android.mystrica.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.xdesign.android.mystrica.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author keithkirk
 */
public class RenameFileView extends LinearLayout {

    @BindView(R.id.myst_edit_text_name)
    protected EditText nameEditText;

    public RenameFileView(Context context) {
        this(context, null);
    }

    public RenameFileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RenameFileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RenameFileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.myst_view_rename_file, this);

        ButterKnife.bind(this);
    }

    public void setNameHint(String hint) {
        nameEditText.setHint(hint);
    }

    public String getName() {
        return nameEditText.getText().toString();
    }
}
