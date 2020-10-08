package com.util.annotations;

import com.util.enums.MethodType;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyServiceAnnotation {

    String name();

    String path();

    MethodType type();

    String[] permissionRoles();
}
