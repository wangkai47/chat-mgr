package com.ld.chat.mapper;

import com.ld.chat.domain.ChatTurn;

import java.util.List;

/**
* @author wangkai
* @description 针对表【chat_turn(轮次管理表)】的数据库操作Mapper
* @createDate 2024-06-05 14:31:02
* @Entity com.ld.chat.domain.ChatTurn
*/
public interface ChatTurnMapper {
    public List<ChatTurn> selectChatTurnListBySessionId(String sessionId);
    public ChatTurn selectChatTurnById(Long id);
    public int insertChatTurn(ChatTurn chatTurn);
    public int updateChatTurn(ChatTurn chatTurn);
    public int deleteChatTurnById(Long id);
    public int deleteChatTurnBySessionId(String sessionId);
}




