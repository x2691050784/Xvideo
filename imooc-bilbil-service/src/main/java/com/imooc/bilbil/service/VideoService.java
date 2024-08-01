package com.imooc.bilbil.service;

import com.imooc.bilbil.dao.VideoDao;
import com.imooc.bilbil.domain.*;
import com.imooc.bilbil.domain.exception.ConditionException;
import com.imooc.bilbil.service.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.HandshakeResponse;
import java.util.*;

@Service
public class VideoService {
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private FastDFSUtil fastDFSUtil;

//    两次添加需要事物的处理
    @Transactional
    public void addVideo(Video video) {
        Date now=new Date();
        video.setCreateTime(new Date());
        videoDao.addVideos(video);
        Long videoId=video.getId();
        List<VideoTag> tags=video.getVideoTagList();
        tags.forEach(item->{
            item.setCreateTime(new Date());
            item.setVideoId(videoId);
        });
        videoDao.batchAddVideoTags(tags);
    }

    public PageResult<Video> pageListService(Integer size, Integer no, String area) {
        if(size ==null||no==null){


            throw new ConditionException("参数异常");
        }
            Map<String,Object>params=new HashMap<>();
            params.put("start",(no-1)*size);
            params.put("limit",size);
            params.put("area",area);
            List<Video>list =new ArrayList<>();
            Integer total=videoDao.pageCountVideos(params);
       if(total>0){
           list=videoDao.pageListVideos(params);
       }
       return new PageResult<>(total,list);
    }

    public void viewVideoOnlineSlices(HttpServletRequest request, HttpServletResponse response, String url) throws Exception {
        fastDFSUtil.viewVideoOnlineSlices(request,response,url);
    }
//点赞
    public void addVideoLike(Long videoId, Long userId) {
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if(videoLike != null){
            throw new ConditionException("已经赞过！");
        }
        videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());
        videoDao.addVideoLike(videoLike);
    }
//    删除点赞
    public void deleteVideoLike(Long videoId, Long userId) {
        videoDao.deleteVideoLike(videoId, userId);
    }

    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        Long count = videoDao.getVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        boolean like = videoLike != null;
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;
    }

    @Transactional
    public void addVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if(videoId == null || groupId == null){
            throw new ConditionException("参数异常！");
        }
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        //更新操作
        //删除原有视频收藏
        videoDao.deleteVideoCollection(videoId, userId);
        //添加新的视频收藏
        videoCollection.setUserId(userId);
        videoCollection.setCreateTime(new Date());
        videoDao.addVideoCollection(videoCollection);
    }

    @Transactional
    public void updateVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if(videoId == null || groupId == null){
            throw new ConditionException("参数异常！");
        }
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("非法视频！");
        }
        videoCollection.setUserId(userId);
        videoDao.updateVideoCollection(videoCollection);
    }

    public void deleteVideoCollection(Long videoId, Long userId) {
        videoDao.deleteVideoCollection(videoId, userId);
    }

    public Map<String, Object> getVideoCollections(Long videoId, Long userId) {
        Long count = videoDao.getVideoCollections(videoId);
        VideoCollection videoCollection = videoDao.getVideoCollectionByVideoIdAndUserId(videoId, userId);
        boolean like = videoCollection != null;
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;
    }

}
