package com.imooc.bilbil.service;

import com.imooc.bilbil.dao.AuthRoleMenuDao;
import com.imooc.bilbil.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleMenuService {
    @Autowired
    private AuthRoleMenuDao authRoleMenuDao;

    public List<AuthRoleMenu> getAuthRoleMeunsByIds(Set<Long> roleIdSet) {
        return authRoleMenuDao.getAuthRoleMenusByRoleIds(roleIdSet);
    }
}
