package com.imooc.bilbil.service.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration //表示这是一个配置文件
public class JsonHttpMessageConverterConfig {

    @Bean  //通过Bean注解,向StringBoot里面注入实体类
    @Primary //优先级
    //将数据做转换 并 放回给bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter=new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonContainer=new FastJsonConfig();
        fastJsonContainer.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonContainer.setSerializerFeatures(
                SerializerFeature.PrettyFormat, //Json格式化输出
                SerializerFeature.WriteNullStringAsEmpty, //Json把控数据转为空字符串.
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.MapSortField,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect    //禁止循环引用
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonContainer);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}
