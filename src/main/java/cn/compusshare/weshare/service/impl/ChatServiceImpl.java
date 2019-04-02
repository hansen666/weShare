package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.entity.Message;
import cn.compusshare.weshare.repository.mapper.MessageMapper;
import cn.compusshare.weshare.repository.mapper.UserMapper;
import cn.compusshare.weshare.repository.responsebody.ChatListInfo;
import cn.compusshare.weshare.repository.responsebody.MessageSegment;
import cn.compusshare.weshare.repository.responsebody.SimpleMessage;
import cn.compusshare.weshare.service.ChatService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.EncryptionUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: LZing
 * @Date: 2019/4/1
 */
@Component
public class ChatServiceImpl implements ChatService {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Environment environment;


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

}
