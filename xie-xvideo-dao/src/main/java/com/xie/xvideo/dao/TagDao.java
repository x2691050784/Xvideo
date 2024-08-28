package com.xie.xvideo.dao;
import com.xie.xvideo.domain.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagDao {
    void addTag(Tag tag);

}
