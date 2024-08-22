package com.imooc.bilbil.dao;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilbil.domain.RefreshTokenDetail;
import com.imooc.bilbil.domain.User;
import com.imooc.bilbil.domain.UserInfo;
import com.imooc.bilbil.domain.VideoBinaryPicture;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper  //可以将注解函数与mybatis的xml进行关联
public interface UserDao {


    Integer addUserInfo(UserInfo userInfo);
    User getUserByPhone(String phone);

    Integer addUser(User user);

    User getUserById(Long id);

    UserInfo getUserInfoById(Long userId);

    Integer updateUsers(User user);

    User getUserByPhoneOrEmail(String phoneOrEmail);

    Integer updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);
//    JSONObject可以用map替换
//    好处:在使用相关xml或者mybits的时候,传入参数可以直接写java.map,不要用JSON传入一大堆标签
//    比较省时间
    Integer pageCountUserInfos(Map<String,Object> params);

    List<UserInfo> pageListUserInfos(JSONObject params);

    Integer deleteRefreToken(@Param("refreshToken") String refreshToken, @Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken, @Param("userId") Long userId, @Param("createTime") Date date);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);
    List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList);


}
