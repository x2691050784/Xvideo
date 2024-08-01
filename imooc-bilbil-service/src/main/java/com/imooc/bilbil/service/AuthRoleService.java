package com.imooc.bilbil.service;

import com.imooc.bilbil.dao.AuthRoleDao;
import com.imooc.bilbil.domain.auth.AuthRole;
import com.imooc.bilbil.domain.auth.AuthRoleElementOperation;
import com.imooc.bilbil.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleService {
    @Autowired
    private AuthRoleElementOperationService authRoleElementOperationService;
    @Autowired
    private AuthRoleMenuService authRoleMenuService;
    @Autowired
    private AuthRoleDao authRoleDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationService.getRoleElementOperationByRoleIds(roleIdSet);

    }


    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuService.getAuthRoleMeunsByIds(roleIdSet);
    }

    public AuthRole getRoleByCode(String code) {
        return authRoleDao.getRoleByCode(code);
    }
}
