package com.imooc.bilbil.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.imooc.bilbil.domain.Danmu;
import com.imooc.bilbil.service.DanmuService;
import com.imooc.bilbil.service.UserService;
import com.imooc.bilbil.service.util.TokenUtil;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/imserver/{token}")
public class WebSocketService {

    //日志记录
    private final Logger logger =LoggerFactory.getLogger(this.getClass());
    //长连接人数
    //AtomicInteger 原子性操作 线程安全
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    private static final ConcurrentHashMap<String,WebSocketService> webSocketServiceMap = new ConcurrentHashMap<>();
    private final UserService userService;
    private Session session;
    private String sessionId;
    private Long userId;
    private static ApplicationContext APPLICATION_CONTEXT;//通过AC获取bean

    public WebSocketService(UserService userService) {
        this.userService = userService;
    }

    //
    //链接成功后表示
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token) {
        try {
            this.userId= TokenUtil.verifyToken(token);
        }catch (Exception e){}

        sessionId = session.getId();
        this.session = session;
        if(webSocketServiceMap.containsKey(sessionId)){
            webSocketServiceMap.remove(sessionId);
        }else{
            webSocketServiceMap.put(sessionId,this);
            ONLINE_COUNT.getAndIncrement();//在线人数加一
        }
        logger.info("用户连接成功" + sessionId+"在线人数："+ONLINE_COUNT.get());
        try{
            this.sendMessage("0");//告诉前端连接成功
        }catch (Exception e){
            logger.error("连接异常");
        }
    }
    @OnClose
    public void closeConnection(Session session) {
        if(webSocketServiceMap.containsKey(sessionId)){
            webSocketServiceMap.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();

        }

    }
    //有消息时
    @OnMessage
    public void onMessage(String message) {
        logger.info("用户信息："+sessionId+"，报文："+message);
        if(!StringUtil.isNullOrEmpty(message)){
            try {
                for(Map.Entry<String,WebSocketService> entry:webSocketServiceMap.entrySet()){
                    WebSocketService webSocketService = entry.getValue();
                    if(webSocketService.session.isOpen()){
                        webSocketService.sendMessage(message);
                    }

                }
                if(userId!=null){
                    Danmu danmu= JSONObject.parseObject(message,Danmu.class);//将message解析为Danmu.class
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());
                    DanmuService danmuService=(DanmuService) APPLICATION_CONTEXT.getBean("danmuService");
                    danmuService.addDanmu(danmu);
//                    redis
                    danmuService.addDanmusToRedis(danmu);
                }

            }catch (Exception e){
                logger.error("弹幕接受异常");
                e.printStackTrace();
            }
        }

    }
    //发生错误
    @OnError
    public void onError(Throwable t) {

    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}
