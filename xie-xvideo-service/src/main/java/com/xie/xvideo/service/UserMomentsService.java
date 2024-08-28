package com.xie.xvideo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xie.xvideo.dao.UserMomentsDao;
import com.xie.xvideo.domain.*;
import com.xie.xvideo.domain.constant.UserMomentsConstant;
import com.xie.xvideo.service.util.RocketMQUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMomentsService {
    @Autowired
    private UserMomentsDao userMomentsDao;

//    引入依赖
//    引出Application主要是获取生产者消费者
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${fdfs.http.storage-addr}")
    private String fastdfsUrl;

//    新建用户动态
    public void addUserMoments(UserMoment userMoment) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        userMoment.setCreateTime(new Date());
        userMoment.setUpdateTime(new Date());
        userMomentsDao.addUserMoments(userMoment);
        DefaultMQProducer producer=(DefaultMQProducer)applicationContext.getBean("momentsProducer");//获取生产者的bean
//        发送消息
        Message msg=new Message(UserMomentsConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
//        同步发送消息
        RocketMQUtil.syncSendMsg(producer,msg);
    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key="subscribed-"+userId;
        System.err.println(key);
        String listStr=redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr,UserMoment.class);

    }

    @Autowired
    private UserService userService;
    public PageResult<UserMoment> pageListMoments(Integer size, Integer no,
                                                  Long userId, String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", (no-1)*size);
        params.put("limit", size);
        params.put("userId", userId);
        params.put("type", type);
        Integer total = userMomentsDao.pageCountMoments(params);
        List<UserMoment> list = new ArrayList<>();
        if(total > 0){
            list = userMomentsDao.pageListMoments(params);
            if(!list.isEmpty()){
                //处理不同类型的动态
                this.processVideoMoment(list.stream()
                        .filter(item -> UserMomentsConstant.TYPE_VIDEO
                                .equals(item.getType())).collect(Collectors.toList()));
                this.processImgMoment(list.stream()
                        .filter(item -> UserMomentsConstant.TYPE_IMG
                                .equals(item.getType())).collect(Collectors.toList()));
                //匹配对应用户信息
                Set<Long> userIdSet = list.stream()
                        .map(UserMoment :: getUserId).collect(Collectors.toSet());
                List<UserInfo> userInfoList = userService.getUserInfoByUserIds(userIdSet);
                list.forEach(moment -> userInfoList.forEach(userInfo -> {
                    if(moment.getUserId().equals(userInfo.getUserId())){
                        moment.setUserInfo(userInfo);
                    }
                }));
            }
        }
        return new PageResult<>(total, list);
    }

    @Autowired
    private VideoService videoService;
    private void processVideoMoment(List<UserMoment> list) {
        List<Video> videoList = list.stream()
                .map(UserMoment::getContent)
                .map(content -> content.getContentDetail().toJavaObject(Video.class))
                .collect(Collectors.toList());
        List<Video> newVideoList = videoService.getVideoCount(videoList);
        newVideoList.forEach(video -> video.setThumbnail(fastdfsUrl+video.getThumbnail()));
        list.forEach(moment -> newVideoList.forEach(video ->{
            if(video.getId().equals(moment.getContent().getContentDetail().getLong("id"))){
                JSONObject contentDetail = JSONObject.parseObject(JSONObject.toJSONString(video));
                moment.getContent().setContentDetail(contentDetail);
            }
        }));
    }

    private void processImgMoment(List<UserMoment> list) {
        list.forEach(moment -> {
            Content content = moment.getContent();
            ImgContent contentDetail = content.getContentDetail().toJavaObject(ImgContent.class);
            contentDetail.setImg(fastdfsUrl + contentDetail.getImg());
            content.setContentDetail(JSONObject.parseObject(JSONObject.toJSONString(contentDetail)));
            moment.setContent(content);
        });
    }
}
