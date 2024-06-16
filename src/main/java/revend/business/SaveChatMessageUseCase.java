package revend.business;

import revend.domain.Message;

public interface SaveChatMessageUseCase {
    void saveAndSendChatMessage(Message message);
}
