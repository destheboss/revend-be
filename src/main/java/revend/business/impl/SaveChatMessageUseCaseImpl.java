package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.SaveChatMessageUseCase;
import revend.domain.Message;
import revend.persistence.ConversationRepository;
import revend.persistence.MessageRepository;
import revend.persistence.UserRepository;
import revend.persistence.entity.ConversationEntity;
import revend.persistence.entity.MessageEntity;
import revend.persistence.entity.UserEntity;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SaveChatMessageUseCaseImpl implements SaveChatMessageUseCase {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Override
    public void saveAndSendChatMessage(Message message) {
        UserEntity fromUser = userRepository.findById(message.getFromUserId()).orElseThrow();
        ConversationEntity conversation = conversationRepository.findById(message.getConversationId()).orElseThrow();

        message.setFromUserName(fromUser.getFirstName());

        MessageEntity entity = MessageEntity.builder()
                .fromUser(fromUser)
                .fromUserName(fromUser.getFirstName())
                .conversation(conversation)
                .text(message.getText())
                .timestamp(LocalDateTime.now())
                .build();

        messageRepository.save(entity);
    }
}
