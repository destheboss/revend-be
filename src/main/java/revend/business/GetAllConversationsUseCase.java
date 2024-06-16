package revend.business;

import revend.domain.Conversation;

import java.util.List;

public interface GetAllConversationsUseCase {
    List<Conversation> getAllConversations(long userId);
}
