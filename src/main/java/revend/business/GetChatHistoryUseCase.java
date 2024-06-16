package revend.business;

import revend.domain.Message;

import java.util.List;

public interface GetChatHistoryUseCase {
    List<Message> getChatHistory(Long conversationId);
}
