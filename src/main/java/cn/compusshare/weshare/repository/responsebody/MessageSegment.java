package cn.compusshare.weshare.repository.responsebody;

import lombok.Data;

import java.util.List;

/**
 * @Author: LZing
 * @Date: 2019/4/2
 */
@Data
public class MessageSegment {

    private String time;

    private List<SimpleMessage> messageSegment;
}
