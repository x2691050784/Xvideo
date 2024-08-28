package com.imooc.bilbil.api;

import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.UserInfo;
import com.imooc.bilbil.domain.Video;
import com.imooc.bilbil.service.DemoService;
import com.imooc.bilbil.service.ElasticSearchService;
import com.imooc.bilbil.service.util.FastDFSUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DemoApi {

    @Autowired
    private DemoService demoService;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Autowired
    private ElasticSearchService elasticSearchService;


    @GetMapping("/slices")
    public void slices(MultipartFile file) throws Exception {
        fastDFSUtil.convertFileToSlices(file);
    }

    @GetMapping("/es-videos")
    public JsonResponse<Video> getEsVideos(@RequestParam String keyword){
       Video video = elasticSearchService.getVideos(keyword);
       return new JsonResponse<>(video);
    }

    @PostMapping("/es-videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
        elasticSearchService.addVideo(video);
        return JsonResponse.success();
    }

    @DeleteMapping("/es-videos")
    public JsonResponse<String> deleteVideos(){
        elasticSearchService.deleteAllVideos();
        return JsonResponse.success();
    }

    @PostMapping("/es-users")
    public JsonResponse<String> addUsers(@RequestBody UserInfo userInfo){
        elasticSearchService.addUserInfo(userInfo);
        return JsonResponse.success();
    }

}
