package com.xie.xvideo.dao;

import com.xie.xvideo.domain.Content;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentDao {
    Long addContent(Content content);
}
