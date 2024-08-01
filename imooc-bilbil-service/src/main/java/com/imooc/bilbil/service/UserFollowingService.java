package com.imooc.bilbil.service;

import com.imooc.bilbil.dao.UserFollowingDao;
import com.imooc.bilbil.domain.FollowingGroup;
import com.imooc.bilbil.domain.User;
import com.imooc.bilbil.domain.UserFollowing;
import com.imooc.bilbil.domain.UserInfo;
import com.imooc.bilbil.domain.constant.UserConstant;
import com.imooc.bilbil.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingService {
    @Autowired
    private UserFollowingDao userFollowingDao; //依赖注入

    @Autowired
    private UserService userService;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Transactional //事务处理 保证失败的时候进行回滚
    public void addUserFollowings(UserFollowing userFollowing){

        Long groupId=userFollowing.getGroupId();
        //默认分组
        if(groupId == null){
            FollowingGroup followingGroup= followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            //给用户添加一个默认分组
            userFollowing.setGroupId(followingGroup.getId());
        }else{
            //存在groupID
            //相当于用户选择一个分组传给我们
            FollowingGroup followingGroup= followingGroupService.getById(groupId);
            if(followingGroup==null){
                throw new ConditionException("关注分组不存在");
            }
        }
        //判断关注的用户是否存在
        Long followingId=userFollowing.getFollowingId();
        User user =userService.getUserById(followingId);
        if(user==null){
            throw new ConditionException("关注的用户不存在");
        }

        //先删除关联关系,再进行关联
        //同时取代更新操作
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(),userFollowing.getFollowingId());
        //关联关系
        //新增
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);

    }
    //获取关注用户列表
    //根据关注用列表的id查询关注用户的基本信息
    //将关注用户按关注分组进行分类
    public List<FollowingGroup> getUserFollowings(Long userId){
        //根据关注的列表获取用户的基本信息
        List<UserFollowing>list=userFollowingDao.getUserFollowing(userId);
        //jdk1.8 List中的对象转换为Set集合。假设list是一个包含UserFollowing对象的List，每个UserFollowing对象有一个属性followingId，表示关注的用户的ID。
        Set<Long> followingSet=list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List< UserInfo> userInfoList =new ArrayList<>();
        if(!followingSet.isEmpty()){
            userInfoList=userService.getUserInfoByUserIds(followingSet);
        }
        for(UserFollowing userFollowing:list){
            for(UserInfo userInfo:userInfoList){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }

        //3
        List<FollowingGroup> groupList=followingGroupService.getUserById(userId);//将关注分租全查出来
        FollowingGroup allGroup=new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result=new ArrayList<>();
        result.add(allGroup);
        for(FollowingGroup group:groupList){
            List <UserInfo> infoList=new ArrayList<>();
            for(UserFollowing userFollowing:list){
                if(group.getId().equals(userFollowing.getGroupId())){
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;

    }
    //获取当前用户的粉丝列表
    //根据粉丝的用户id查询基本信息
    //查询当前用户是否已经关注了该粉丝
    public List<UserFollowing> getUserFans(Long userId){
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);

        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());

        List<UserInfo> userInfoList = new ArrayList<>();
        if(fanIdSet.size() > 0){
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }

        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId);

        for(UserFollowing fan : fanList){
            for(UserInfo userInfo : userInfoList){
                if(fan.getUserId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }

            for(UserFollowing following : followingList){

                if(following.getFollowingId().equals(fan.getUserId())){

                    fan.getUserInfo().setFollowed(true);

                }
            }

        }
        System.out.println("getUserFans"+fanList);
        return fanList;
    }

    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());//创建时间
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);//用户自创的分组所以使用3
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroup(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);

    }

    public List<UserInfo> checkFollowingStatus(List<UserInfo> list, Long userId) {
        //查询当前用户已经关注了哪些用户
        List<UserFollowing> userFollowingList=userFollowingDao.getUserFollowing(userId);
        for(UserInfo userInfo :list){
            userInfo.setFollowed(false);
            for(UserFollowing userFollowing:userFollowingList){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(true);
                }
            }
        }
        return list;
    }
}
