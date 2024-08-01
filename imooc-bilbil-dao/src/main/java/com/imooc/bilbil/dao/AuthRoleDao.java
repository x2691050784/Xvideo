package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String code);
}
