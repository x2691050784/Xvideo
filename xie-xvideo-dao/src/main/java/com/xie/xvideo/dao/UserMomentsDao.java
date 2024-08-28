package com.xie.xvideo.dao;

import com.xie.xvideo.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMomentsDao {


    Integer addUserMoments(UserMoment userMoment);
    /*-----*/
    Integer pageCountMoments(Map<String, Object> params);

    List<UserMoment> pageListMoments(Map<String, Object> params);

}
