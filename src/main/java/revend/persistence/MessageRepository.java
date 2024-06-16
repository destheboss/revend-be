package revend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import revend.persistence.entity.MessageEntity;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByConversationId(Long conversationId);
}
