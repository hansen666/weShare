package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.Message;
import cn.compusshare.weshare.repository.responsebody.ChatListInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    Set<String> selectSender(String userID);

    Set<String> selectReceiver(String userID);

    ChatListInfo selectChatList(@Param("userId") String userId, @Param("anotherUserId") String anotherUserId);

    ChatListInfo selectChatListSingle(@Param("userId") String userId, @Param("anotherUserId") String anotherUserId);

    int updateMessageStatus(@Param("userId") String userId, @Param("currentUserId") String currentUserId);

    List<Message> selectMessageRecord(@Param("userId") String userId, @Param("anotherUserId") String anotherUserId);
}