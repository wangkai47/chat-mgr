package com.ld.chat.controller;

import cn.hutool.core.util.StrUtil;
import com.ld.chat.domain.ChatTurn;
import com.ld.chat.dto.ChatDialogReq;
import com.ld.chat.service.IChatService;
import com.ld.common.core.controller.BaseController;
import com.ld.common.core.domain.AjaxResult;
import com.ld.common.core.domain.model.LoginUser;
import com.ld.common.utils.SecurityUtils;
import com.ld.common.utils.StringUtils;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController {
    @Autowired
    private IChatService chatService;

    @GetMapping("/create")
    public AjaxResult create() {
        chatService.create(SecurityUtils.getLoginUser());
        return success();
    }

    //@PreAuthorize("@ss.hasPermi('chat:training:query')")
    @GetMapping("/training")
    public AjaxResult query(String query, String sessionId) {
        if (StringUtils.isEmpty(query)) {
            return error("请输入问题");
        }
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        ChatTurn chatTurn = chatService.chat(loginUser, sessionId, query);
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", chatTurn.getSessionId());
        map.put("turnId", chatTurn.getTurnId());
        map.put("answer", chatTurn.getAnswer());
        map.put("orderNo", chatTurn.getOrderNo());
        return success(map);
    }

    @PostMapping(value = "/dialog", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiImplicitParam(name = "dialogReq", value = "对话请求", required = true)
    public ResponseBodyEmitter dialog(@RequestBody ChatDialogReq dialogReq) {
        System.out.println("--dialog---------------------------------------------");
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
