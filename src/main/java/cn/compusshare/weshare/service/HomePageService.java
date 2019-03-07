package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.entity.School;
import cn.compusshare.weshare.responsebody.SchoolResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface HomePageService {

    List<SchoolResponse> selectAllSchool();

    List<String> allSchoolName();

}
