package cn.compusshare.weshare.service;

import cn.compusshare.weshare.repository.entity.School;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LZing
 * @Date: 2019/3/4
 * service接口示例
 */
@Service
public interface HelloService {

     List<School> dataBaseTest(int count);
}
