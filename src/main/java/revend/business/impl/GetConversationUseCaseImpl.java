package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetConversationUseCase;
import revend.business.exception.ConversationNotFoundException;
import revend.domain.Conversation;
import revend.persistence.ConversationRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetConversationUseCaseImpl implements GetConversationUseCase {
    private final ConversationRepository conversationRepository;

    @Override
    public Optional<Conversation> getConversation(Long id) {
        return conversationRepository.findById(id)
                .map(ConversationConverter::convert)
                .map(Optional::of)
                .orElseThrow(() -> new ConversationNotFoundException(id));
    }
}
