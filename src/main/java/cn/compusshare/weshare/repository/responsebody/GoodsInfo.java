package cn.compusshare.weshare.repository.responsebody;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: LZing
 * @Date: 2019/3/13
 * 返回给前端的物品信息
 */
@Data
@Builder
public class GoodsInfo {

    //物品ID
    private String ID;
    //名称
    private String name;
    //标签
    private Byte label;
    //图片URL
    private List<String> picUrl;
    //详情
    private String description;
    //价格
    private Float price;
    //手机号
    private String phone;
    //距离
    private String distance;
    //发布时间、卖出时间都用此字段表示
    private String time;

}
