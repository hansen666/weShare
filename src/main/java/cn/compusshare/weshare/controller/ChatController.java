package cn.compusshare.weshare.controller;

import cn.compusshare.weshare.service.ChatService;
import cn.compusshare.weshare.utils.ResultResponse;
import cn.compusshare.weshare.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: LZing
 * @Date: 2019/4/1
 * 聊天controller
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final static Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @GetMapping("/messageList")
    public ResultResponse messageList(@RequestHeader String token) {
        logger.info("ChatController.messageList(),入参：token={}", token);
        return chatService.messageList(token);
    }

    @GetMapping("/messageRecord")
    public ResultResponse messageRecord(@RequestHeader String token, @RequestParam String userId) {
        logger.info("ChatController.messageRecord(),入参：token={}, userId={}", token, userId);
        return ResultUtil.success(chatService.getMessageRecord(token,userId));
    }

    /**
     * 客服消息推送
     * @param param
     */
    @PostMapping("/customerService")
    public void customerService(@RequestBody Map<String,Object> param) {
        logger.info("ChatController.customerService(),入参：param={}", param.toString());
        chatService.customerService(param);
    }
}
