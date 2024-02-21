package com.zea.cloud.common.repeat;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForbidRepeatClick {

    int time() default 3000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    String message() default "Forbid repeat click";
}
