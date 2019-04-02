package cn.compusshare.weshare.repository.responsebody;

import lombok.Data;

import java.util.Date;

/**
 * @Author: LZing
 * @Date: 2019/4/2
 *
 * 聊天列表返回信息体
 */
@Data
public class ChatListInfo implements Comparable{

    //TODO 加密
    private String userId;

    private String nickname;

    private String avatarUrl;

    private String content;

    private Byte type;

    private Byte read;

    private Date createTime;

    private String pubTime;

    @Override
    public int compareTo(Object o) {
        return ((ChatListInfo) o).createTime.compareTo(this.createTime);
    }
}
