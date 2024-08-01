package com.imooc.bilbil.service.util;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

//发送信息
public class RocketMQUtil {
    //同步发送消息
    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        SendResult result = producer.send(msg);
        System.out.println(result);
    }
    //异步
    public static void asyncSendMsg(DefaultMQProducer producer,Message msg) throws RemotingException, InterruptedException, MQClientException {
        int messageCount=2;//发送两次 计数器为2
        CountDownLatch2 countDownLatch=new CountDownLatch2(messageCount);
        for(int i=0;i<messageCount;i++){
            producer.send(msg, new SendCallback() {
//                成功
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();//减一次
                    System.out.println(sendResult.getMsgId());
                }
//失败
                @Override
                public void onException(Throwable throwable) {
                    countDownLatch.countDown();

                    System.out.println("发送消息时,出现错误" + throwable);
                    throwable.printStackTrace();;
                }
            });
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
    }
}
