package com.ld.chat.service;

import com.ld.chat.entity.ChatTurn;

import java.util.List;

/**
* @author wangkai
* @description 针对表【chat_turn(轮次管理表)】的数据库操作Service
* @createDate 2024-06-05 14:31:02
*/
public interface IChatTurnService {
    public List<ChatTurn> selectChatTurnListBySessionId(String sessionId);
    public ChatTurn selectChatTurnById(Long id);
    public int insertChatTurn(ChatTurn chatTurn);
    public int updateChatTurn(ChatTurn chatTurn);
    public int deleteChatTurnById(Long id);
    public int deleteChatTurnBySessionId(String sessionId);
}
