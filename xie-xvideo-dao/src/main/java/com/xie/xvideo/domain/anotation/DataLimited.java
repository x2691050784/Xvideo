package com.xie.xvideo.domain.anotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

//数据权限
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface DataLimited {
//只是标注作用无需参数
}
