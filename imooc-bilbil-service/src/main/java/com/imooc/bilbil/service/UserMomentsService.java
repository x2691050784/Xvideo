package com.imooc.bilbil.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imooc.bilbil.dao.UserMomentsDao;
import com.imooc.bilbil.domain.UserMoment;
import com.imooc.bilbil.domain.constant.UserMomentsConstant;
import com.imooc.bilbil.service.util.RocketMQUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.Date;
import java.util.List;

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
}
