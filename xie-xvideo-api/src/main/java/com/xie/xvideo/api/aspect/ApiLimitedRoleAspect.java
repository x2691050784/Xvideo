package com.xie.xvideo.api.aspect;

import com.xie.xvideo.api.support.UserSupport;
import com.xie.xvideo.domain.anotation.ApiLimitedRole;
import com.xie.xvideo.domain.auth.UserRole;
import com.xie.xvideo.domain.exception.ConditionException;
import com.xie.xvideo.service.UserRoleService;
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

public class ApiLimitedRoleAspect {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserRoleService userRoleService;

    //切点
    @Pointcut("@annotation(com.xie.xvideo.domain.anotation.ApiLimitedRole)")
    public void check() {
    }
    //在节点之前
    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole){
        Long userId =userSupport.getCurrentUserId();

        List<UserRole> userRoleList =userRoleService.getUserRoleByUserId(userId);
        String[] limitedRoleCodeList=apiLimitedRole.limitedRoleCodeList();
        Set<String> limitedRoleCodeSet= Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet=userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        //将两个取交集
        roleCodeSet.retainAll(limitedRoleCodeSet);
//        如果交集不等与0表示权限不足
        if(!roleCodeSet.isEmpty()){
            throw new ConditionException("权限不足");
        }
    }


}
