package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowingDao {

    Integer deleteUserFollowing(@Param("userId") Long userId,@Param("followingId") Long followingId); //自动识别相关的名称

    Integer addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowing(Long userId);

    List<UserFollowing> getUserFans(Long userId);

    List<UserFollowing> getUserFollowings(Long userId);
}
