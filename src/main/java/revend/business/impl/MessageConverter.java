package revend.business.impl;

import revend.domain.Message;
import revend.persistence.entity.MessageEntity;

public class MessageConverter {
    private MessageConverter() {
    }

    public static Message convert(MessageEntity messageEntity) {
        return Message.builder()
                .id(messageEntity.getId())
                .uuid(messageEntity.getUuid())
                .conversationId(messageEntity.getConversation().getId())
                .fromUserId(messageEntity.getFromUser().getId())
                .fromUserName(messageEntity.getFromUserName())
                .text(messageEntity.getText())
                .timestamp(messageEntity.getTimestamp())
                .build();
    }
}
