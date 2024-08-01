package com.imooc.bilbil.service;

import com.imooc.bilbil.dao.AuthRoleElementOperationDao;
import com.imooc.bilbil.domain.auth.AuthRoleElementOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleElementOperationService {
    @Autowired
    private AuthRoleElementOperationDao authRoleElementOperationDao;
    public List<AuthRoleElementOperation> getRoleElementOperationByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationDao.getRoleElementOperationByRoleIds(roleIdSet);

    }
}
