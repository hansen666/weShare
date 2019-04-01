package cn.compusshare.weshare.repository.RequestBody;


import lombok.Data;

import java.io.Serializable;

@Data
public class MessageBody implements Serializable {
    String id;

    String message;

}
