package com.imooc.bilbil.service;


import com.imooc.bilbil.dao.TagDao;
import com.imooc.bilbil.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    public Long addTag(Tag tag) {
        tagDao.addTag(tag);
        return tag.getId();
    }
}
