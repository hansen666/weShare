package cn.compusshare.weshare.repository.RequestBody;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageRequest {

    private MultipartFile file;

    private int id;

    private String filePath;
}
