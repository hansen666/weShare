package cn.compusshare.weshare.repository.RequestBody;

import lombok.Data;

/**
 * @Author: LZing
 * @Date: 2019/4/24
 * 管理台物品查询请求体
 */
@Data
public class AdminGoodsRequest {
    //物品名称
    private String goodsName;
    //用户昵称
    private String nickname;
    //标签
    private Byte label;
    //上架状态
    private Byte status;
    //学校名称
    private String schoolName;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;
    //查询标识，0-查发布表，1-查求购表
    private Integer flag = 0;
    //当前页码
    private Integer currentPage = 0;

}
