package com.imooc.bilbil.domain.auth;

import java.util.List;

public class UserAuthorities {

    List<AuthRoleElementOperation> roleElementOperationList;//操作权限列表

    List<AuthRoleMenu> roleMenuList;//页面菜单

    public List<AuthRoleElementOperation> getRoleElementOperationList() {
        return roleElementOperationList;
    }

    public void setRoleElementOperationList(List<AuthRoleElementOperation> roleElementOperationList) {
        this.roleElementOperationList = roleElementOperationList;
    }

    public List<AuthRoleMenu> getRoleMenuList() {
        return roleMenuList;
    }

    public void setRoleMenuList(List<AuthRoleMenu> roleMenuList) {
        this.roleMenuList = roleMenuList;
    }
}
