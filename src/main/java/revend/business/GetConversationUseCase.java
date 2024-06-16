package revend.business;

import revend.domain.Conversation;

import java.util.Optional;

public interface GetConversationUseCase {
    Optional<Conversation> getConversation(Long id);
}
