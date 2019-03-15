package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.responsebody.SchoolResponse;
import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomePageService {

    List<SchoolResponse> selectAllSchool();

    List<String> allSchoolName();

    ResultResponse showGoods(String token, int pageIndex, Byte label, String keyword);

}
