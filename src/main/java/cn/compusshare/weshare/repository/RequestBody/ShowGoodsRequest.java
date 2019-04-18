package cn.compusshare.weshare.repository.RequestBody;

import lombok.Data;

@Data
public class ShowGoodsRequest {
    //页码
    private int currentPage;
    //标签
    private Byte label;
    //关键字
    private String keyword;
    //时间
    private String currentTime;
}
