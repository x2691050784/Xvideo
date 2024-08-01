package com.imooc.bilbil.service;

import com.imooc.bilbil.dao.FollowingGroupDao;
import com.imooc.bilbil.dao.UserFollowingDao;
import com.imooc.bilbil.domain.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingGroupService {
    @Autowired
    private FollowingGroupDao followingGroupDao; //依赖注入
    public FollowingGroup getByType(String type){
        return followingGroupDao.getByType(type);
    }
    public FollowingGroup getById(Long id){
        return followingGroupDao.getById(id);
    }

    public List<FollowingGroup> getUserById(Long userId) {
        return followingGroupDao.getUserById(userId);
    }

    public void addFollowingGroup(FollowingGroup followingGroup) {
        followingGroupDao.addFollowingGroup(followingGroup);

    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupDao.getUserFollowingGroup(userId);
    }
}
