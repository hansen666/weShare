package cn.compusshare.weshare.controller;


import cn.compusshare.weshare.repository.RequestBody.ImageRequest;
import cn.compusshare.weshare.service.ImageService;
import cn.compusshare.weshare.utils.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/image")
public class ImageController {

    private final static Logger logger = LoggerFactory.getLogger(Logger.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    private Environment environment;

    @PostMapping("/upload")
    public ResultResponse upload(ImageRequest imageRequest) {
        String savePath = environment.getProperty("image.path") + imageRequest.getFilePath()+"\\";
        logger.info("ImageController.upload(),传入图片={}", imageRequest.getFile().getOriginalFilename());
        return imageService.uploadImage(imageRequest.getFile(), imageRequest.getId(), savePath);
    }
}
