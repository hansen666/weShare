package cn.compusshare.weshare.repository.RequestBody;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PublishGoodsRequest {

    private String userId;

    private String goodsName;

    private Byte lable;

    private String picUrl;

    private String description;

    private float price;

    private String phone;

    private float longitude;

    private float latitude;
}
