package com.xie.xvideo.service.handler;

import com.xie.xvideo.domain.JsonResponse;
import com.xie.xvideo.domain.exception.ConditionException;
import  org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)//全局异常处理器,优先级最高
public class CommonGlobalExceptionHandler {
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request,Exception e){
        String errorMsg=e.getMessage();
        if (e instanceof ConditionException){
            String errorCode=((ConditionException)e).getCode();
            return new JsonResponse<>(errorCode,errorMsg);
        }else{
            return new JsonResponse<>("500",errorMsg);
        }
    }
}
