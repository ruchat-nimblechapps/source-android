package com.xdesign.android.common.harness.exceptions;

import android.support.test.runner.AndroidJUnit4;

import com.xdesign.android.common.harness.R;
import com.xdesign.android.common.lib.exceptions.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public final class ValidationExceptionTest {

    @Test
    public void getMessageRes() {
        assertEquals(0, new ValidationException().getMessageRes());
        assertEquals(R.string.test, new ValidationException(R.string.test).getMessageRes());
    }
}
