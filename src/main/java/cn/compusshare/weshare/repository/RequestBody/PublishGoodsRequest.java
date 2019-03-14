package cn.compusshare.weshare.repository.RequestBody;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PublishGoodsRequest {


    private String goodsName;

    private Byte label;

    private String picUrl;

    private String description;

    private float price;

    private String phone;

    private double longitude;

    private double latitude;
}
