package com.xie.xvideo.service.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.xie.xvideo.domain.UserFollowing;
import com.xie.xvideo.domain.UserMoment;
import com.xie.xvideo.domain.constant.UserMomentsConstant;
import com.xie.xvideo.service.UserFollowingService;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//处理消息
@Configuration
public class RocketMQConfig {
    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private UserFollowingService userFollowingService;
    //redis工具类
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //生产者
    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws Exception{
        DefaultMQProducer producer =new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }
    //消费者
    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception{
        DefaultMQPushConsumer consumer=new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS,"*");
        //监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {

//            处理的结果进行返回
//            list 消息
//            context上下文存放处理的信息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg=list.get(0);
                if(msg==null){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;//处理成功
                }
                String bodyStr=new String(msg.getBody());
                UserMoment userMoment= JSONObject.toJavaObject(JSONObject.parseObject(bodyStr),UserMoment.class);
//                获取UserId,方便查找所有订阅UserId的用户
                Long userId=userMoment.getUserId();
                System.err.println("xzq1");

                List<UserFollowing>fanlist =userFollowingService.getUserFans(userId);
                System.err.println("xzq2");
                for(UserFollowing fan:fanlist){
//                    发送到Redis中

                    String key="subscribed-"+fan.getUserId();
                    String subscribedListStr=redisTemplate.opsForValue().get(key);//***********
                    List<UserMoment> subscribedList;
                    System.err.println("xzq3");
//                    如果为空
                    if(StringUtils.isNullOrEmpty(subscribedListStr)){
                        System.err.println("xzq7-null");
                        subscribedList=new ArrayList<>();

                    }else{
//                        如果不为空装换一下类型
                        System.err.println("xzq7_!null");
                        subscribedList= JSONArray.parseArray(subscribedListStr,UserMoment.class);//************

                    }
                    subscribedList.add(userMoment);
                    String json=JSONObject.toJSONString(subscribedList);
                   // System.err.println(json);
                    redisTemplate.opsForValue().set(key,json);//redis添加数据
                }
                System.err.println("xzq666");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;//返回消息处理成功
            }
        });
        consumer.start();
        return consumer;
    }
}
