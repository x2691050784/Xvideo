package com.imooc.bilbil.api.aspect;


import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.domain.UserMoment;
import com.imooc.bilbil.domain.anotation.ApiLimitedRole;
import com.imooc.bilbil.domain.auth.UserRole;
import com.imooc.bilbil.domain.constant.AuthRoleConstant;
import com.imooc.bilbil.domain.exception.ConditionException;
import com.imooc.bilbil.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)//优先级
@Component //本质上就是让springboot能都扫描到这个方法
@Aspect//标注这是一个切面Class

public class DataLimitedAspect {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserRoleService userRoleService;

    //切点
    @Pointcut("@annotation(com.imooc.bilbil.domain.anotation.DataLimited)")
    public void check() {
    }
    //在节点之前
    @Before("check()")
    public void doBefore(JoinPoint joinPoint){
        Long userId =userSupport.getCurrentUserId();
        List<UserRole> userRoleList =userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet=userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for(Object arg:args){
            if(arg instanceof UserMoment){
                UserMoment userMoment =(UserMoment)  arg;
                String type =userMoment.getType();
                if (roleCodeSet.contains(AuthRoleConstant.ROLE_LV1) && !"0".equals(type)) {
                    throw new ConditionException("参数异常");
                }
            }
        }
    }


}
