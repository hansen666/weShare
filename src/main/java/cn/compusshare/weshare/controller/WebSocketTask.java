package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.config.MyEndPointConfigure;
import cn.compusshare.weshare.repository.RequestBody.MessageBody;
import cn.compusshare.weshare.repository.RequestBody.MessageDecoder;
import cn.compusshare.weshare.repository.RequestBody.MessageEncoder;
import cn.compusshare.weshare.repository.entity.Message;
import cn.compusshare.weshare.repository.mapper.MessageMapper;
import cn.compusshare.weshare.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: LZing
 * @Date: 2019/4/1
 */
@Component
@ServerEndpoint(value = "/chat/{token}", configurator = MyEndPointConfigure.class,decoders = {MessageDecoder.class }, encoders = { MessageEncoder.class })
public class WebSocketTask {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MessageMapper messageMapper;

    private Session session;

    private String userID;

    private static CopyOnWriteArraySet<WebSocketTask> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(@PathParam("token") String token, Session session) {
        this.userID = loginService.getOpenIDFromToken(token);
        this.session = session;
        webSocketSet.add(this);
        System.out.println(userID+"连接成功");
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        System.out.println(userID+"关闭连接");
    }

    @OnMessage
    public void onMessage(MessageBody message) {
        System.out.println("来自客户端的消息："+message);
        for (WebSocketTask item : webSocketSet) {
            try {
                if (item.userID.equals(message.getUserId())) {
                    item.sendMessage(item.userID + ":" + message);
                    Message userMessage = new Message();
                    userMessage.setSenderId(this.userID);
                    userMessage.setReceiverId(message.getUserId());
                    userMessage.setContent(message.getContent());
                    userMessage.setType(message.getType());
                    userMessage.setFirstMessage(message.getIsFirstMessage());
                    messageMapper.insertSelective(userMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

    }

    @OnError
    public void onError( Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}
