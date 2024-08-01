package com.imooc.bilbil.api.support;

import com.imooc.bilbil.domain.exception.ConditionException;
import com.imooc.bilbil.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {
    public Long getCurrentUserId(){

        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();//spring中提供的的获取方法
        //获取token
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId= TokenUtil.verifyToken(token);//对token进行解密
        if(userId<0){
            throw new ConditionException("非法用户token");
        }
        return userId;
    }


}
