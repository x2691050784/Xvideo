package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface UserMomentsDao {


    Integer addUserMoments(UserMoment userMoment);
}
