package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.repository.mapper.PublishGoodsMapper;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.repository.mapper.UserMapper;
import cn.compusshare.weshare.repository.responsebody.SchoolResponse;
import cn.compusshare.weshare.service.HomePageService;
import cn.compusshare.weshare.service.LoginService;
import cn.compusshare.weshare.utils.CommonUtil;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HomePageServiceImpl implements HomePageService {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private PublishGoodsMapper publishGoodsMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<SchoolResponse> selectAllSchool() {
        Map<String, List<String>> result = new HashMap<>();
        List<School> allSchool = schoolMapper.selectAll();
        List<SchoolResponse> schoolResponses = new ArrayList<>();
        String province;
        int size = allSchool.size();
        for (int i = 0; i < size; i++) {
            province = allSchool.get(i).getProvince();
            if (!result.containsKey(province)) {
                List<String> schoolName = new ArrayList<>();
                result.put(province, schoolName);
            }
            result.get(province).add(allSchool.get(i).getName());
        }
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            SchoolResponse schoolResponse = SchoolResponse.builder()
                    .province(entry.getKey())
                    .schoolName(entry.getValue())
                    .build();
            schoolResponses.add(schoolResponse);
        }
        return schoolResponses;
    }

    @Override
    public List<String> allSchoolName() {

        return schoolMapper.selectAllName();
    }


    @Override
    public ResultResponse showGoods(String token, int pageIndex, Byte label, String keyword) {
        String publisherId = loginService.getOpenIDFromToken(token);
        String key = CommonUtil.isEmpty(keyword) ? null : keyword.trim();
        try {
            List<HashMap<String, Object>> goodsList = publishGoodsMapper.selectShowGoods(publisherId,
                    6 * pageIndex, label, key, userMapper.selectByPrimaryKey(publisherId).getSchoolName());
            goodsList.forEach(t -> t.put("pubTime", CommonUtil.timeFromNow((Date) t.get("pubTime"))));
            return ResultUtil.success(goodsList);
        } catch (Exception e) {
            logger.info("首页商品数据库查询失败" + e.getMessage());
            return ResultUtil.fail(-1, "首页商品数据库查询失败" + e.getMessage());
        }
    }
}
