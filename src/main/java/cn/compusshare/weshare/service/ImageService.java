package cn.compusshare.weshare.service;

import cn.compusshare.weshare.utils.ResultResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    ResultResponse uploadImage(MultipartFile[] file);
}
