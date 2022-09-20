package com.fartburger.fartcheat.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventListener {
    EventType value();

    int prio() default 0;
}
