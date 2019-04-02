package cn.compusshare.weshare.repository.RequestBody;


import lombok.Data;

import java.io.Serializable;

@Data
public class MessageBody implements Serializable {
    String userId;  //对话用户ID

    Byte isFirstMessage; // 0-否，1-是

    String content;  //消息内容

    Byte type;  //消息类型

}
