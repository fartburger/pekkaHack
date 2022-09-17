package com.fartburger.fartcheat.config;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(VisibilitySpecifiers.class)
public @interface VisibilitySpecifier {
    String value();
}
