package com.imooc.bilbil.api;

import com.imooc.bilbil.api.support.UserSupport;
import com.imooc.bilbil.domain.JsonResponse;
import com.imooc.bilbil.domain.PageResult;
import com.imooc.bilbil.domain.Video;
import com.imooc.bilbil.domain.VideoCollection;
import com.imooc.bilbil.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import javax.websocket.HandshakeResponse;
import java.util.Map;

@RestController
public class VideoApi {
@Autowired
private VideoService videoService;
@Autowired
private UserSupport userSupport;
@PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
    Long userId=userSupport.getCurrentUserId();
    video.setUserId(userId);
    videoService.addVideo(video);
    return JsonResponse.success();
}
@GetMapping("videos")
    public JsonResponse<PageResult<Video>>pageListVideos(Integer size,Integer no,String area){
    PageResult<Video> result=videoService.pageListService(size,no,area);
    return new JsonResponse<>(result);
}
//在线播放
@GetMapping("/video-slices")
    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
    videoService.viewVideoOnlineSlices(request, response, url);
}
    @PostMapping("/video-likes")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(videoId, userId);
        return JsonResponse.success();
    }
//    点赞取消
@DeleteMapping("/video-likes")
public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId){
    Long userId = userSupport.getCurrentUserId();
    videoService.deleteVideoLike(videoId, userId);
    return JsonResponse.success();
}

//视频点赞数量
    @GetMapping("/video-likes")
    public JsonResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}
        Map<String, Object> result = videoService.getVideoLikes(videoId, userId);
        return new JsonResponse<>(result);
    }


    //收藏

    /**
     * 收藏视频
     */
    @PostMapping("/video-collections")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCollection(videoCollection, userId);
        return JsonResponse.success();
    }

    /**
     * 更新收藏视频
     */
    @PutMapping("/video-collections")
    public JsonResponse<String> updateVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoService.updateVideoCollection(videoCollection, userId);
        return JsonResponse.success();
    }

    /**
     * 取消收藏视频
     */
    @DeleteMapping("/video-collections")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId, userId);
        return JsonResponse.success();
    }

    /**
     * 查询视频收藏数量
     */
    @GetMapping("/video-collections")
    public JsonResponse<Map<String, Object>> getVideoCollections(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch (Exception ignored){}
        Map<String, Object> result = videoService.getVideoCollections(videoId, userId);
        return new JsonResponse<>(result);
    }



}
