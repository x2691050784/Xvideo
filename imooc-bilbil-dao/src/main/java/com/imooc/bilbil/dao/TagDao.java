package com.imooc.bilbil.dao;
import com.imooc.bilbil.domain.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagDao {
    void addTag(Tag tag);

}
