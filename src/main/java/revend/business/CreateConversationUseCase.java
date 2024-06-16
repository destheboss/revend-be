package revend.business;

import revend.domain.CreateConversationResponse;
import revend.domain.CreateConversationRequest;

public interface CreateConversationUseCase {
    CreateConversationResponse createConversation(CreateConversationRequest request);
}