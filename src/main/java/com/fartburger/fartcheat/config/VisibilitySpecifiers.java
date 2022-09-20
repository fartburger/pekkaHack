package com.fartburger.fartcheat.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface VisibilitySpecifiers {
    VisibilitySpecifier[] value();
}
