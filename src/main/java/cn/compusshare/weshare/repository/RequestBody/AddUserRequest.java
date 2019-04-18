package cn.compusshare.weshare.repository.RequestBody;

import lombok.Data;

/**
 * @Author: LZing
 * @Date: 2019/3/11
 */
@Data
public class AddUserRequest {
    //学校名称
    private String schoolName;
    //昵称
    private String nickname;
    //头像url
    private String avatarUrl;
}
