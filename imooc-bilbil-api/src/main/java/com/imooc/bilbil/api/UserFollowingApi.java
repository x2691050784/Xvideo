package com.imooc.bilbil.api;

import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.domain.FollowingGroup;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.UserFollowing;
import com.imooc.bilbil.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class UserFollowingApi {
    @Autowired
    private UserFollowingService userFollowingService;
    @Autowired
    private UserSupport userSupport;
    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing){
        Long userId=userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }
    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings(){
        Long userId=userSupport.getCurrentUserId();
        List<FollowingGroup> result=userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(result);
    }
    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans(){
        Long userId=userSupport.getCurrentUserId();
        List<UserFollowing> result=userFollowingService.getUserFans(userId);
        return new JsonResponse<>(result);
    }

    //添加用户分组
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroup(@RequestBody FollowingGroup followingGroup){//Long是为了范围值为用户关注组id
        Long userId=userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId=userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }

    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroup(){
        Long userId=userSupport.getCurrentUserId();
        List<FollowingGroup> list =userFollowingService.getUserFollowingGroup(userId);
        return new JsonResponse<>(list);
    }



}
