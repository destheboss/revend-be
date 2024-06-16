package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetChatHistoryUseCase;
import revend.domain.Message;
import revend.persistence.MessageRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class GetChatHistoryUseCaseImpl implements GetChatHistoryUseCase {
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getChatHistory(Long conversationId) {
        return messageRepository.findByConversationId(conversationId).stream()
                .map(MessageConverter::convert)
                .toList();
    }
}
