package com.xdesign.android.common.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import retrofit.http.RestMethod;

/**
 * Annotation to be used for annotating a Retrofit method which requires to do a Delete
 * operation whilst supplying a body.
 * <p>
 * This is not RFC-compliant, and as such OkHttpClient will need to be used as the
 * Retrofit client.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RestMethod(value = "DELETE", hasBody = true)
@SuppressWarnings("checkstyle:typename")
public @interface DELETE_WITH_BODY {
    String value();
}
