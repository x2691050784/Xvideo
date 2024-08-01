package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDao {

    Integer addFile(File file);

    java.io.File getFileByMD5(String md5);
}
