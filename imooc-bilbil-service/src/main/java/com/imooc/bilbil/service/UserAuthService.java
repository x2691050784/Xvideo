package com.imooc.bilbil.service;

import com.imooc.bilbil.domain.auth.*;
import com.imooc.bilbil.domain.constant.AuthRoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthService {
    @Autowired
    private UserRoleService userRoleService;//用户角色
    @Autowired
    private AuthRoleService authRoleService;

    public UserAuthorities getUserAuthorities(Long userId) {
//        通过用户ID查询用户的等级
        List<UserRole> userRoleList =userRoleService.getUserRoleByUserId(userId);
//        查多个用户权限，需要多个用户id；查出来的是Long类型等于UserRole中的getRoleId的方法，最后转化成Set类型
        Set<Long> roleIdSet=userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
//        获取用户权限通过角色的元素操作权限
        List<AuthRoleElementOperation> roleElementOperationList=authRoleService.getRoleElementOperationsByRoleIds(roleIdSet);
//        获取页面菜单权限信息
        List<AuthRoleMenu> authRoleMenuList=authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);

        UserAuthorities userAuthorities=new UserAuthorities();
        userAuthorities.setRoleElementOperationList(roleElementOperationList);
        userAuthorities.setRoleMenuList(authRoleMenuList);
        return userAuthorities;

    }

//    写在UAS里面主要是因为UAS里面已经引入了userRoleService与authRoleService
    public void addUserDefualtRole(Long id) {
        UserRole userRole=new UserRole();
        AuthRole role =authRoleService.getRoleByCode(AuthRoleConstant.ROLE_CODE_LV0);
        userRole.setUserId(id);
        userRole.setRoleId(role.getId());
        userRoleService.addUserRole(userRole);

    }
}
