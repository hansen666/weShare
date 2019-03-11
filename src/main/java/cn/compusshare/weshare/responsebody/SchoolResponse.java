package cn.compusshare.weshare.responsebody;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SchoolResponse {

    private String province;

    private List<String> schoolName;

}
