package com.imooc.bilbil.api;

import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.UserMoment;
import com.imooc.bilbil.domain.anotation.ApiLimitedRole;
import com.imooc.bilbil.domain.anotation.DataLimited;
import com.imooc.bilbil.domain.constant.AuthRoleConstant;
import com.imooc.bilbil.domain.constant.UserMomentsConstant;
import com.imooc.bilbil.service.UserMomentsService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//用户动态提醒
@RestController
public class UserMomentApi
{
    @Autowired
    private UserMomentsService userMomentsService;
    @Autowired
    private UserSupport userSupport;


//    发布动态
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_CODE_LV0})//lv0不可以发布动态
    @DataLimited//数据权限
    @PostMapping("user-moments")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        /*
            接口权限:
            1.(简单)如果没有这个权限是不可以调用这个接口,如果可以放行继续执行下面的操作--臃肿复杂
            2.选择切面在每个节点

        */

        Long userId =userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentsService.addUserMoments(userMoment);
        return JsonResponse.success();
    }


//    获取订阅的消息
    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId=userSupport.getCurrentUserId();
        List<UserMoment> list=userMomentsService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }
}
