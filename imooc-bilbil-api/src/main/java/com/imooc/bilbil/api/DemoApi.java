package com.imooc.bilbil.api;


import com.imooc.bilbil.service.DemoService;
import com.imooc.bilbil.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class DemoApi {

    @Autowired
    private DemoService demoService;

    @Autowired
    private FastDFSUtil fastDFSUtil;


    @GetMapping("/slices1")
    public void slice(MultipartFile file) throws Exception{
    fastDFSUtil.convertFileToSlices(file);
}

}
