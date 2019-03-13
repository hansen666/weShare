package cn.compusshare.weshare.repository.RequestBody;

import lombok.Data;

@Data
public class GoodsRequest {


    private String goodsName;

    private Byte label;

    private String picUrl;

    private String description;

    private float price;

    private String phone;

    private float longitude;

    private float latitude;
}
