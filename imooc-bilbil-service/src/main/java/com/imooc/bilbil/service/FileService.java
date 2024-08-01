package com.imooc.bilbil.service;

import com.imooc.bilbil.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {
    @Autowired
    private FastDFSUtil fastDFSUtil;


    public String uploadFileBySlices(MultipartFile slice, String fileMD5, Integer fileNo, Integer totalSliceNo) throws IOException {
        return fastDFSUtil.uploadFileBySlices(slice, fileMD5, fileNo, totalSliceNo);
    }


}
