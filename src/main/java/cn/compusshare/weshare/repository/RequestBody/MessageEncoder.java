package cn.compusshare.weshare.repository.RequestBody;

import com.alibaba.fastjson.JSON;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements javax.websocket.Encoder.Text<MessageBody> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(MessageBody message) throws EncodeException {
        return JSON.toJSONString(message);
    }

}
