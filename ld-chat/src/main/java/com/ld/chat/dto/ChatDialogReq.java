package com.ld.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Classname ChatDialogReq
 * @Description TODO
 * @Date 2024/6/6 11:21
 * @Author wk
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDialogReq {
    private String conversationId;
    private String parentMessageId;
    @NotNull("问题不能为空")
    private String question;
}
