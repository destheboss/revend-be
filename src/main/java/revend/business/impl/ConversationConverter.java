package revend.business.impl;

import revend.domain.Conversation;
import revend.persistence.entity.ConversationEntity;

public class ConversationConverter {
    private ConversationConverter(){
    }

    public static Conversation convert(ConversationEntity conversationEntity) {
        return Conversation.builder()
                .id(conversationEntity.getId())
                .title(conversationEntity.getTitle())
                .user1Id(conversationEntity.getUser1().getId())
                .user2Id(conversationEntity.getUser2().getId())
                .build();
    }
}
