package revend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table(name = "messages")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "uuid")
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    @NotNull
    private UserEntity fromUser;

    @NotNull
    @Column(name = "text")
    private String text;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @NotNull
    @Column(name = "from_user_name")
    private String fromUserName;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id")
    private ConversationEntity conversation;
}
