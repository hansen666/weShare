package cn.compusshare.weshare.repository.responsebody;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponse {

    private int id;

    private String fileName;
}
