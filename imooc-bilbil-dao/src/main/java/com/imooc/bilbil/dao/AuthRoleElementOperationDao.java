package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.auth.AuthRoleElementOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleElementOperationDao {

    //指定列表
    List<AuthRoleElementOperation> getRoleElementOperationByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);
}
