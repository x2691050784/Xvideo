package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.Content;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentDao {
    Long addContent(Content content);
}
