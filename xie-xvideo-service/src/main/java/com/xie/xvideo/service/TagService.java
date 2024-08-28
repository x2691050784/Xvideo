package com.xie.xvideo.service;


import com.xie.xvideo.dao.TagDao;
import com.xie.xvideo.domain.Tag;
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
