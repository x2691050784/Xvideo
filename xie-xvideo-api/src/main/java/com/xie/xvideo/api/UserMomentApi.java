package com.xie.xvideo.api;

import com.xie.xvideo.api.support.UserSupport;
import com.xie.xvideo.domain.JsonResponse;
import com.xie.xvideo.domain.PageResult;
import com.xie.xvideo.domain.UserMoment;
import com.xie.xvideo.domain.anotation.ApiLimitedRole;
import com.xie.xvideo.domain.anotation.DataLimited;
import com.xie.xvideo.domain.constant.AuthRoleConstant;
import com.xie.xvideo.service.UserMomentsService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})//lv0不可以发布动态
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

    @GetMapping("/moments")
    public JsonResponse<PageResult<UserMoment>> pageListMoments(@RequestParam("size") Integer size,
                                                                @RequestParam("no") Integer no,
                                                                String type){
        Long userId = userSupport.getCurrentUserId();
        PageResult<UserMoment> list = userMomentsService.pageListMoments(size, no,
                userId, type);
        return new JsonResponse<>(list);
    }
}
