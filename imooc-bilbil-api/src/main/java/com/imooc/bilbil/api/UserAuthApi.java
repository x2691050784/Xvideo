package com.imooc.bilbil.api;

import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.auth.UserAuthorities;
import com.imooc.bilbil.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthApi {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserAuthService userAuthService;
    //数据权限控制
    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId= userSupport.getCurrentUserId();
        UserAuthorities userAuthorities=userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
//    接口权限控制

}
