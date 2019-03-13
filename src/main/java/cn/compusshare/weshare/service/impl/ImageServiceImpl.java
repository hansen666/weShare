package cn.compusshare.weshare.service.impl;

import cn.compusshare.weshare.service.ImageService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    private Environment environment;

    @Override
    public ResultResponse uploadImage(MultipartFile[] files) {
        String filePath = environment.getProperty("image.path");
        Random rand = new Random();
        String result = "";

        for (MultipartFile file : files) {
            String originName = file.getOriginalFilename();
            String typeName = originName.substring(originName.lastIndexOf("."));
            long time = System.currentTimeMillis();
            String nowTimeStamp = String.valueOf(time / 1000);
            int randNum = rand.nextInt(899) + 100;
            String fileName = filePath + nowTimeStamp + randNum + typeName;
            result += fileName + ",";
            File newFile = new File(fileName);
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultUtil.fail();
            }
        }

        return ResultUtil.success(result.substring(0, result.length() - 1));
    }


}
