package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.entity.Message;
import cn.compusshare.weshare.repository.mapper.MessageMapper;
import cn.compusshare.weshare.repository.mapper.UserMapper;
import cn.compusshare.weshare.repository.responsebody.ChatListInfo;
import cn.compusshare.weshare.repository.responsebody.MessageSegment;
import cn.compusshare.weshare.repository.responsebody.SimpleMessage;
import cn.compusshare.weshare.service.ChatService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.service.common.CacheService;
import cn.compusshare.weshare.utils.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: LZing
 * @Date: 2019/4/1
 */
@Component
public class ChatServiceImpl implements ChatService {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Environment environment;

    @Value("customerServiceTokenUrl")
    private String customerServiceTokenUrl;

    @Value("customerServiceMsgUrl")
    private String customerServiceMsgUrl;

    /**
     * 获取消息列表
     *
     * @param token
     * @return
     */
    @Override
    public ResultResponse messageList(String token) {
        String openID = loginService.getOpenIDFromToken(token);
        Set<String> senders = messageMapper.selectSender(openID);
        Set<String> receivers = messageMapper.selectReceiver(openID);
        Set<String> common = new HashSet<>();
        for (String sender : senders) {
            if (receivers.contains(sender)) {
                common.add(sender);
            }
        }
        ArrayList<ChatListInfo> result = new ArrayList<>();
        for (String userID : common) {
            senders.remove(userID);
            receivers.remove(userID);
            ChatListInfo chatListInfo = messageMapper.selectChatList(openID, userID);
            Map<String, String> userMap = userMapper.selectNicknameAndAvatar(userID);
            if (openID.equals(chatListInfo.getUserId())) {
                //已读
                chatListInfo.setRead((byte) 1);
            }
            chatListInfo.setUserId(EncryptionUtil.AESEncryptToString(userID, environment.getProperty("AESKey")));
            chatListInfo.setNickname(userMap.get("nickname"));
            chatListInfo.setAvatarUrl(userMap.get("avatarUrl"));
            chatListInfo.setPubTime(CommonUtil.timeFromNow(chatListInfo.getCreateTime()));
            result.add(chatListInfo);
        }
        result.addAll(singleMessageList(openID, receivers, 0));
        result.addAll(singleMessageList(openID, senders, 1));
        Collections.sort(result);

        return ResultUtil.success(result);
    }

    /**
     * 获取部分消息列表
     *
     * @param currentUserId
     * @param userIds
     * @param flag
     * @return
     */
    private ArrayList<ChatListInfo> singleMessageList(String currentUserId, Set<String> userIds, int flag) {
        ArrayList<ChatListInfo> result = new ArrayList<>();
        for (String userID : userIds) {
            ChatListInfo chatListInfo;
            if (flag == 0) {
                //currentUser为信息发送者
                chatListInfo = messageMapper.selectChatListSingle(currentUserId, userID);
                //已读
                chatListInfo.setRead((byte) 1);
            } else {
                //currentUser为信息接收者
                chatListInfo = messageMapper.selectChatListSingle(userID, currentUserId);
            }
            if (null != chatListInfo) {
                Map<String, String> userMap = userMapper.selectNicknameAndAvatar(userID);
                chatListInfo.setUserId(EncryptionUtil.AESEncryptToString(userID, environment.getProperty("AESKey")));
                chatListInfo.setNickname(userMap.get("nickname"));
                chatListInfo.setAvatarUrl(userMap.get("avatarUrl"));
                chatListInfo.setPubTime(CommonUtil.timeFromNow(chatListInfo.getCreateTime()));
                result.add(chatListInfo);
            }
        }
        return result;
    }

    /**
     * 获取两个中户之间的聊天记录
     * @param token
     * @param userId
     */
    @Override
    public Map<String,List<MessageSegment>> getMessageRecord(String token, String userId) {
        String currentUserId = loginService.getOpenIDFromToken(token);
        try {
            userId = EncryptionUtil.AESDecrypt(userId.replace(' ','+'), environment.getProperty("AESKey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageMapper.updateMessageStatus(userId,currentUserId);
        List<MessageSegment> messageRecords = new ArrayList<>();
        //数据表中取出来的消息记录
        List<Message> messageList = messageMapper.selectMessageRecord(currentUserId, userId);
        //消息分段处理
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getFirstMessage() == 1) {
                //一段聊天记录
                MessageSegment record = new MessageSegment();
                //一段消息的开始时间
                record.setTime(CommonUtil.getFormatDate(messageList.get(i).getCreateTime()));
                //一段消息的内容
                List<SimpleMessage> simpleMessageList = new ArrayList<>();
                simpleMessageList.add(generatorSimpleMessage(messageList.get(i),currentUserId));
                i++;
                while (i < messageList.size() && messageList.get(i).getFirstMessage() != 1){
                    simpleMessageList.add(generatorSimpleMessage(messageList.get(i),currentUserId));
                    i++;
                }
                record.setMessageSegment(simpleMessageList);
                //加入一段聊天记录
                messageRecords.add(record);
                i--;
            }

        }
        Map<String,List<MessageSegment>> result = new HashMap<>(1);
        result.put("messageList",messageRecords);
        return result;
    }

    /**
     * 生成目标消息格式
     * @param message
     * @param currentUserId
     * @return
     */
    private SimpleMessage generatorSimpleMessage(Message message, String currentUserId) {
        SimpleMessage simpleMessage = new SimpleMessage();
        simpleMessage.setContent(message.getContent());
        simpleMessage.setType(message.getType());
        if (message.getSenderId().equals(currentUserId)) {
            simpleMessage.setFrom((byte) 0);
        }else {
            simpleMessage.setFrom((byte) 1);
        }
        return simpleMessage;
    }

    /**
     * 客服消息签名验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @Override
    public String validate(String signature, String timestamp, String nonce, String echostr) {
        String[] strArr = {environment.getProperty("customerService"), timestamp, nonce};
        Arrays.sort(strArr);
        String str = strArr[0] + strArr[1] + strArr[2];
        str = DigestUtils.sha1Hex(str);
        if (str.equals(signature)) {
            return echostr;
        }
        return "fail";
    }

    /**
     * 调用客服消息推送接口
     * @param param
     */
    @Override
    public void customerService(Map<String, Object> param){
        String userId = (String) param.get("FromUserName");
        String token = (String) cacheService.get(userId+"cus");
        if (CommonUtil.isEmpty(token)) {
            try {
                JSONObject jsonObject = (JSONObject) JSONObject.parse(HttpUtil.requestByGet(customerServiceTokenUrl));
                if ((int) jsonObject.get("errcode") != 0) {
                    logger.error(jsonObject.getString("errmsg"));
                    return;
                }
                token = jsonObject.getString("access_token");
                //把token放到redis缓存中，并设置过期时限为100分钟
                cacheService.set(userId+"cus",token,100, TimeUnit.MINUTES);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        //推送接口的请求参数
        Map<String,Object> msgBody = new HashMap<>(3);
        //用户openID
        msgBody.put("touser",param.get("FromUserName"));
        //消息类型，这里默认为文本
        msgBody.put("msgtype","text");
        Map<String,Object> tempMap = new HashMap<>(1);
        String msgType = (String) param.get("MsgType");
        //如果为event，表示是进入可是界面的事件，发固定推送
        if (msgType.equals("event")) {
            tempMap.put("content","欢迎来撩客服有小姐姐!\n1、第一个问题\n2、第二个问题");
        }else {
            String content = (String) param.get("Content");
            if (content.equals("1")) {
                tempMap.put("content", "第一条问题的回答");
            } else if (content.equals("2")) {
                tempMap.put("content", "第二条问题的回答");
            } else {
                tempMap.put("content", "没有这个选项");
            }
        }
        msgBody.put("text",tempMap);
        JSONObject msgJson = (JSONObject) JSONObject.toJSON(msgBody);
        customerServiceMsgUrl = customerServiceTokenUrl + token;
        String result = HttpUtil.requestByPost(customerServiceMsgUrl,msgJson);
        logger.info("customerService httpPost:"+result);
    }

}
