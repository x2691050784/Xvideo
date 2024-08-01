package com.imooc.bilbil.api;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.dao.UserFollowingDao;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.PageResult;
import com.imooc.bilbil.domain.User;
import com.imooc.bilbil.domain.UserInfo;
import com.imooc.bilbil.service.UserFollowingService;
import com.imooc.bilbil.service.UserService;
import com.imooc.bilbil.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
/*
*   @RestController 和 @Controller 是 Spring Framework 中用于定义控制器的注解
*
* */
public class UserApi {
    @Autowired
    private UserService userService;
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserFollowingService userFollowingService;//添加依赖

@GetMapping("/users")
public JsonResponse<User> getUserInfo(){
    Long userId=userSupport.getCurrentUserId();
    User user =userService.getUserInfo(userId); //通过userid获取user信息
    return new JsonResponse<>(user);
}

//获取公钥接口
    @GetMapping("/rea-pks")
    public JsonResponse<String> getRsaPublicKey(){
        String pk= RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }
    @PostMapping("/users")
    public JsonResponse<String> addUser(@RequestBody User user){ //@RequestBody 就是将接受的数据类型返回成Json类型
        userService.addUser(user);
        return JsonResponse.success();
    }
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token =userService.login(user);
        return new JsonResponse<>(token);
    }
//更新用户信息
    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
    Long userId=userSupport.getCurrentUserId();
    user.setId(userId);
    userService.updateUsers(user);
    return JsonResponse.success();
    }
//    @PutMapping("/user-info")
//    public JsonResponse<String> upfda
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo){
    Long userId=userSupport.getCurrentUserId();
    userInfo.setUserId(userId);
    userService.updateUserInfos(userInfo);
    return JsonResponse.success();
    }

    ///搜索用户--分页查询
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer no,@RequestParam Integer size, String nick){
        Long userId=userSupport.getCurrentUserId();
        JSONObject params=new JSONObject();
//        JSONObject只是一种数据结构，可以理解为JSON格式的数据结构（key-value 结构），
//        可以使用put方法给json对象添加元素。JSONObject可以很方便的转换成字符串，
//        也可以很方便的把其他对象转换成JSONObject对象。
        params.put("no",no);
        params.put("size",size);
        params.put("nick",nick);
        params.put("userId",userId);
        PageResult<UserInfo> result= userService.pageListInfos(params);
//        判断是否已经关注
        if(result.getTotal()>0){
//            主要添加followed是否为真
            List<UserInfo> checkUserInfoList=userFollowingService.checkFollowingStatus(result.getList(),userId);
            result.setList(checkUserInfoList);
        }
        return new JsonResponse<>(result);
    }

    @PostMapping("/user-dts")//    返回双token
    public JsonResponse<Map<String, Object>>loginForDts(@RequestBody User user) throws Exception {
    Map<String,Object>map=userService.loginForDts(user);
    return new JsonResponse<>(map);
    }
    @DeleteMapping("/refresh-tokens")
    public JsonResponse<String> logout(HttpServletRequest request){
    String refreshToken =request.getHeader("refreshToken");
    Long userId= userSupport.getCurrentUserId();
    userService.logout(refreshToken,userId);
    return JsonResponse.success();
    }
    @PostMapping("/access-token")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String  refreshToken=request.getHeader("refreshToken");
        String accessToken=userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }

}
