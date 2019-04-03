package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.config.MyEndPointConfigure;
import cn.compusshare.weshare.repository.RequestBody.MessageBody;
import cn.compusshare.weshare.repository.RequestBody.MessageDecoder;
import cn.compusshare.weshare.repository.RequestBody.MessageEncoder;
import cn.compusshare.weshare.repository.entity.Message;
import cn.compusshare.weshare.repository.mapper.MessageMapper;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
@ServerEndpoint(value = "/chat", configurator = MyEndPointConfigure.class,decoders = {MessageDecoder.class }, encoders = { MessageEncoder.class })
public class WebSocketTask {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private Environment environment;

    private Session session;

    private String userID;

    private static CopyOnWriteArraySet<WebSocketTask> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        String token=session.getRequestParameterMap().get("token").get(0);
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
    public void onMessage(MessageBody message) throws Exception {
        //userId解密
        message.setUserId(EncryptionUtil.AESDecrypt(message.getUserId(),environment.getProperty("AESKey")));
        System.out.println("来自客户端的消息："+message.toString());
        //数据表消息记录实体类
        Message userMessage = new Message();
        userMessage.setSenderId(this.userID);
        userMessage.setReceiverId(message.getUserId());
        userMessage.setContent(message.getContent());
        userMessage.setType(message.getType());
        userMessage.setFirstMessage(message.getIsFirstMessage());
        for (WebSocketTask item : webSocketSet) {
            try {
                if (item.userID.equals(message.getUserId())) {
                    item.sendMessage(item.userID + ":" + message.getContent());
                    //设为已读
                    userMessage.setRead((byte) 1);
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        messageMapper.insertSelective(userMessage);

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
