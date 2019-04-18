package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.responsebody.MessageSegment;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: LZing
 * @Date: 2019/4/1
 */
@Service
public interface ChatService {
    ResultResponse messageList(String token);

    Map<String,List<MessageSegment>> getMessageRecord(String token, String userId);

    String validate(String signature, String timestamp, String nonce, String echostr);

    void customerService(Map<String, Object> param);
}
