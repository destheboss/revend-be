package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetAllConversationsUseCase;
import revend.domain.Conversation;
import revend.persistence.ConversationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllConversationsUseCaseImpl implements GetAllConversationsUseCase {
    private final ConversationRepository conversationRepository;

    @Override
    public List<Conversation> getAllConversations(long userId) {
        return conversationRepository.findAllByUser1IdOrUser2Id(userId, userId).stream()
                .map(ConversationConverter::convert)
                .toList();
    }
}
