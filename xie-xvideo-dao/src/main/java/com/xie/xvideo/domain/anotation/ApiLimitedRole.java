package com.xie.xvideo.domain.anotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

//接口权限注解
@Retention(RetentionPolicy.RUNTIME)//运行阶段
@Target({ElementType.METHOD})//放在什么位置--方法
@Documented
@Component
public @interface ApiLimitedRole {

    String[] limitedRoleCodeList() default {};
}
