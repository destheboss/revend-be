package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.business.GetAllChats;
import revend.domain.Message;
import revend.persistence.MessageRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllChatsImpl implements GetAllChats {
    private final MessageRepository messageRepository;

    @Override
    public List<Message> getAllChats() {
        return messageRepository.findAll().stream()
                .map(MessageConverter::convert)
                .toList();
    }
}
