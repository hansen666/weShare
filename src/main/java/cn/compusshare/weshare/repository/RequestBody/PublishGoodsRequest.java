package cn.compusshare.weshare.repository.RequestBody;

import lombok.Data;

import java.util.Date;

@Data
public class PublishGoodsRequest {

    private String userId;

    private String goodsName;

    private String lable;

    private String picUrl;

    private String discription;

    private String phone;

    private Date pubTime;

    private float longitude;

    private float latitude;
}
