package cn.compusshare.weshare.repository.mapper;

import cn.compusshare.weshare.repository.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int isUserExist(String userID);

    byte selectIdentifiedType(String userID);

    Map<String, Object> selectUserInfo(String userID);

    int certify(@Param("userID") String userID, @Param("college") String college, @Param("degree") String degree,
                @Param("department") String department, @Param("major") String major, @Param("type") int type);

    Map<String, String> selectNicknameAndAvatar(String userID);

    String selectAvatarUrl(String id);

    int updateAvatarUrl(@Param("id") String id, @Param("avatarUrl") String avatarUrl);

    List<Map<String, Object>> selectUserByType(@Param("nickname") String nickname, @Param("type") Integer type, @Param("currentPage") Integer currentPage);

    int userQueryCount(@Param("nickname") String nickname, @Param("type") Integer type);

    List<Map<String, Object>> monthlyQuantity(Integer year);

    List<Map<String, Object>> dailyQuantity(@Param("year") int year, @Param("month") int month);
}