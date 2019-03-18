package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.repository.responsebody.ImageResponse;
import cn.compusshare.weshare.service.ImageService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Component
public class ImageServiceImpl implements ImageService {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Override
    public ResultResponse uploadImage(MultipartFile file, int id, String filePath) {
        Random rand = new Random();
        String originName = file.getOriginalFilename();
        String typeName = originName.substring(originName.lastIndexOf("."));
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time / 1000);
        int randNum = rand.nextInt(899) + 100;
        String fileName = nowTimeStamp + randNum + typeName;
        File newFile = new File(filePath + fileName);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            logger.info("图片{}上传失败", originName);
            return ResultUtil.fail(-1, "图片" + originName + "上传失败");
        }
        ImageResponse imageResponse = ImageResponse.builder()
                .fileName(fileName)
                .id(id)
                .build();

        return ResultUtil.success(imageResponse);
    }


}
