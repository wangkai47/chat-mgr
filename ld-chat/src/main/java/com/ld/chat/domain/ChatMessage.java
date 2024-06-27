package com.ld.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 消息id
     */
    private String messageId;
    /**
     * 对话id
     */
    private String conversationId;

    /**
     * 消息类型  1-user, 2-bot
     */
    private Integer messageType;

    /**
     * 上文id
     */
    private String parentMessageId;
    /**
     * 问题描述
     */
    private String message;
    /**
     * 连续问题排序序号，第一条消息是1
     */
    private Integer messageOrder;

    /**
     * 提问人用户id
     */
    private Long userId;

    /**
     * 用户ip
     */
    private String ip;

    /**
     * 回答耗时
     */
    private Long costTime;
    /**
     * api类型
     */
    private String apiType;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
