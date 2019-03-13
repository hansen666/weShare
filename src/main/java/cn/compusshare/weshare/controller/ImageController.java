package cn.compusshare.weshare.controller;


import cn.compusshare.weshare.service.ImageService;
import cn.compusshare.weshare.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/image")
public class ImageController {

    private final static Logger logger= LoggerFactory.getLogger(Logger.class);

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResultResponse upload(@RequestParam("files")MultipartFile[] files){
        logger.info("ImageController.upload(),传入图片数量={}",files.length);
        return imageService.uploadImage(files);
    }
}
