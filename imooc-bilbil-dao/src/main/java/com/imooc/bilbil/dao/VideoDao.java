package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface VideoDao {
    //添加视频
    Integer addVideos(Video video);
    //添加视频标签
    Integer batchAddVideoTags(List<VideoTag> videoTagList);
    /*-------*/
//    分页查询
    Integer pageCountVideos(Map<String, Object> params);

    List<Video> pageListVideos(Map<String, Object> params);

    Video getVideoById(Long id);

    VideoLike getVideoLikeByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

    Integer addVideoLike(VideoLike videoLike);

    Integer deleteVideoLike(@Param("videoId") Long videoId,
                            @Param("userId") Long userId);

    Long getVideoLikes(Long videoId);

    Integer deleteVideoCollection(@Param("videoId") Long videoId,
                                  @Param("userId") Long userId);

    Integer addVideoCollection(VideoCollection videoCollection);



    void updateVideoCollection(VideoCollection videoCollection);

    Long getVideoCollections(Long videoId);

    VideoCollection getVideoCollectionByVideoIdAndUserId(Long videoId, Long userId);
    VideoCoin getVideoCoinByVideoIdAndUserId(@Param("videoId") Long videoId,
                                             @Param("userId") Long userId);

    Integer addVideoCoin(VideoCoin videoCoin);
    Integer updateVideoCoin(VideoCoin videoCoin);
    Long getVideoCoinsAmount(Long videoId);
    Integer addVideoComment(VideoComment videoComment);
    Integer pageCountVideoComments(Map<String, Object> params);
    List<VideoComment> pageListVideoComments(Map<String, Object> params);
    List<VideoComment> batchGetVideoCommentsByRootIds(@Param("rootIdList") List<Long> rootIdList);
/*---------*/
    Video getVideoDetails(Long videoId);
    VideoView getVideoView(Map<String, Object> params);
    Integer addVideoView(VideoView videoView);
    Integer getVideoViewCounts(Long videoId);
    List<UserPreference> getAllUserPreference();
    List<Video> batchGetVideosByIds(@Param("idList") List<Long> idList);

/*--------*/
List<VideoTag> getVideoTagsByVideoId(Long videoId);
    Integer deleteVideoTags(@Param("tagIdList") List<Long> tagIdList,
                            @Param("videoId") Long videoId);

    Integer batchAddVideoBinaryPictures(@Param("pictureList") List<VideoBinaryPicture> pictureList);

    List<VideoViewCount> getVideoViewCountByVideoIds(Set<Long> videoIds);

    List<VideoDanmuCount> getVideoDanmuCountByVideoIds(Set<Long> videoIds);
}
