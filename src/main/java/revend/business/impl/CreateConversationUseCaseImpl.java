package revend.business.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.CreateConversationUseCase;
import revend.business.exception.InvalidUserException;
import revend.domain.CreateConversationRequest;
import revend.domain.CreateConversationResponse;
import revend.persistence.ConversationRepository;
import revend.persistence.UserRepository;
import revend.persistence.entity.ConversationEntity;
import revend.persistence.entity.UserEntity;

@Service
@AllArgsConstructor
public class CreateConversationUseCaseImpl implements CreateConversationUseCase {
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    @Override
    @Transactional
    public CreateConversationResponse createConversation(CreateConversationRequest request) {
        UserEntity user1 = userRepository.findById(request.getUser1Id())
                .orElseThrow(() -> new InvalidUserException("User not found"));
        UserEntity user2 = userRepository.findById(request.getUser2Id())
                .orElseThrow(() -> new InvalidUserException("User not found"));

        ConversationEntity existingConversation = conversationRepository.findByUsers(user1, user2);

        if (existingConversation != null) {
            return CreateConversationResponse.builder()
                    .conversationId(existingConversation.getId())
                    .build();
        }

        ConversationEntity newConversationEntity = ConversationEntity.builder()
                .title("Conversation between " + user1.getFirstName() + " and " + user2.getFirstName())
                .user1(user1)
                .user2(user2)
                .build();

        ConversationEntity savedConversation = conversationRepository.save(newConversationEntity);

        return CreateConversationResponse.builder()
                .conversationId(savedConversation.getId())
                .build();
    }
}
