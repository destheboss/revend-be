package revend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private String uuid;
    private Long conversationId;
    private Long fromUserId;
    private String fromUserName;
    private String text;
    private LocalDateTime timestamp;
}
