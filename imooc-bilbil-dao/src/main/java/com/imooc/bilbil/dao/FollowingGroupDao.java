package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowingGroupDao {
    FollowingGroup getByType(String type);

    FollowingGroup getById(Long id);

    List<FollowingGroup> getUserById(Long userId);

    Integer addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroup(Long userId);
}
