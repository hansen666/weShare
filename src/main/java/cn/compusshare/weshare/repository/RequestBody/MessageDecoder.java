package cn.compusshare.weshare.repository.RequestBody;

import com.alibaba.fastjson.JSON;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements javax.websocket.Decoder.Text<MessageBody> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public MessageBody decode(String message) throws DecodeException {
        return JSON.parseObject(message, MessageBody.class);
    }

    @Override
    public boolean willDecode(String arg0) {
        return true;
    }

}
