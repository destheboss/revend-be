package revend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import revend.business.impl.*;
import revend.domain.Conversation;
import revend.domain.CreateConversationRequest;
import revend.domain.CreateConversationResponse;
import revend.domain.Message;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/conversations")
@CrossOrigin(origins = "http://localhost:3000")
public class ConversationsController {
    private final SaveChatMessageUseCaseImpl saveChatMessageUseCaseImpl;
    private final GetChatHistoryUseCaseImpl getChatHistoryUseCaseImpl;
    private final GetAllConversationsUseCaseImpl getAllConversationUseCaseImpl;
    private final GetConversationUseCaseImpl getConversationUseCaseImpl;
    private final CreateConversationUseCaseImpl createConversationUseCaseImpl;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<Void> sendMessagesToUsers(@PathVariable Long conversationId, @RequestBody Message message) {
        message.setConversationId(conversationId);
        saveChatMessageUseCaseImpl.saveAndSendChatMessage(message);
        messagingTemplate.convertAndSend("/topic/conversation/" + message.getConversationId(), message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<Message>> getChatHistory(@PathVariable Long conversationId) {
        List<Message> chatHistory = getChatHistoryUseCaseImpl.getChatHistory(conversationId);
        return ResponseEntity.ok(chatHistory);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<Conversation> getConversation(@PathVariable Long conversationId) {
        Optional<Conversation> conversation = getConversationUseCaseImpl.getConversation(conversationId);
        return conversation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable Long userId) {
        List<Conversation> conversations = getAllConversationUseCaseImpl.getAllConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @PostMapping()
    public ResponseEntity<CreateConversationResponse> createConversation(@RequestBody CreateConversationRequest request) {
        CreateConversationResponse response = createConversationUseCaseImpl.createConversation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
