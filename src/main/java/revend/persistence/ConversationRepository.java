package revend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import revend.persistence.entity.ConversationEntity;
import revend.persistence.entity.UserEntity;

import java.util.List;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
    List<ConversationEntity> findAllByUser1IdOrUser2Id(Long userId1, Long userId2);

    @Query("SELECT c FROM ConversationEntity c WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    ConversationEntity findByUsers(@Param("user1") UserEntity user1, @Param("user2") UserEntity user2);
}
