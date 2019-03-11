package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.responsebody.SchoolResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomePageService {

    List<SchoolResponse> selectAllSchool();

    List<String> allSchoolName();

}
