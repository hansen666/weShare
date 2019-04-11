package cn.compusshare.weshare.repository.responsebody;

import lombok.Data;

/**
 * @Author: LZing
 * @Date: 2019/4/2
 * 消息记录部分信息
 */
@Data
public class SimpleMessage {

    //消息类型
    private Byte type;

    //发送方向
    private Byte from;

    //内容
    private String content;

    //音频长度
    private Integer audioLength;
}
