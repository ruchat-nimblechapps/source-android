package com.xdesign.android.common.lib.exceptions;


import androidx.annotation.StringRes;

/**
 * {@link Exception} to handle input validation errors.
 *
 * @author Alex Macrae
 */
public class ValidationException extends Exception {

    private static final long serialVersionUID = -2275616912324223938L;

    private static final String MESSAGE = "Message resource ";

    @StringRes
    private final int message;

    public ValidationException() {
        this(0);
    }

    public ValidationException(@StringRes int message) {
        super(MESSAGE + message);

        this.message = message;
    }

    @StringRes
    public final int getMessageRes() {
        return message;
    }
}
