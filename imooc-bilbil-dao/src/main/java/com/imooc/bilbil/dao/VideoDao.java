package com.imooc.bilbil.dao;

import com.imooc.bilbil.domain.Video;
import com.imooc.bilbil.domain.VideoCollection;
import com.imooc.bilbil.domain.VideoLike;
import com.imooc.bilbil.domain.VideoTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface VideoDao {
    Integer addVideos(Video video);

    Integer batchAddVideoTags(List<VideoTag> videoTagList);

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
}
