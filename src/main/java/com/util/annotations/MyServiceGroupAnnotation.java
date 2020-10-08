package com.util.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyServiceGroupAnnotation {

    String name();

    String path();
}
