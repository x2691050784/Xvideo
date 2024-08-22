package com.imooc.bilbil.service;


import com.imooc.bilbil.dao.ContentDao;
import com.imooc.bilbil.domain.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    @Autowired
    private ContentDao contentDao;
    public Long addContent(Content content) {
        contentDao.addContent(content);
        return content.getId();
    }
}
