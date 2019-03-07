package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.repository.mapper.SchoolMapper;
import cn.compusshare.weshare.responsebody.SchoolResponse;
import cn.compusshare.weshare.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private SchoolMapper schoolMapper;

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
//              SchoolResponse schoolResponse = new SchoolResponse();
//            schoolResponse.setProvince(entry.getKey());
//            schoolResponse.setSchoolName(entry.getValue());
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
}
