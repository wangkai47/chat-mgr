package com.ld.chat.controller;

import cn.hutool.core.util.StrUtil;
import com.ld.chat.dto.ChatDialogReq;
import com.ld.chat.service.IChatService;
import com.ld.common.core.controller.BaseController;
import com.ld.common.core.domain.AjaxResult;
import com.ld.common.utils.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;


@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController {
    @Autowired
    private IChatService chatService;

    @GetMapping("/create")
    public AjaxResult create() {
        //chatService.create(SecurityUtils.getLoginUser());
        return success();
    }

    @PostMapping(value = "/dialog", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiImplicitParam(name = "dialogReq", value = "对话请求", required = true)
    public ResponseBodyEmitter dialog(@RequestBody ChatDialogReq dialogReq) {
        Assert.isTrue(StringUtils.isNotBlank(dialogReq.getQuestion()), "问题不能为空");
        Assert.isTrue(StrUtil.isNotBlank(dialogReq.getQuestion()), "问题不能为空");
        Assert.isTrue(StrUtil.length(dialogReq.getQuestion())<=1000,"问题最大长度不能超过1000个字符");
        return chatService.dialog(dialogReq);
    }

    @GetMapping("/over")
    public AjaxResult over(String sessionId) {
        chatService.over(sessionId);
        return success();
    }
}
