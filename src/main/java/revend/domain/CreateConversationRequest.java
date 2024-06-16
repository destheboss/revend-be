package revend.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationRequest {
    @NotBlank()
    private String title;

    @NotBlank()
    private Long user1Id;

    @NotBlank()
    private Long user2Id;
}
