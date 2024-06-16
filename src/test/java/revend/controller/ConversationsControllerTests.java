package revend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import revend.business.impl.*;
import revend.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ConversationsControllerTests {
    @Mock
    private GetChatHistoryUseCaseImpl getChatHistoryUseCase;

    @Mock
    private GetAllConversationsUseCaseImpl getAllConversationUseCase;

    @Mock
    private GetConversationUseCaseImpl getConversationUseCase;

    @Mock
    private CreateConversationUseCaseImpl createConversationUseCase;

    @InjectMocks
    private ConversationsController conversationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetConversation() {
        Long conversationId = 1L;
        Conversation conversation = new Conversation();

        when(getConversationUseCase.getConversation(conversationId)).thenReturn(Optional.of(conversation));

        ResponseEntity<Conversation> response = conversationsController.getConversation(conversationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conversation, response.getBody());
    }

    @Test
    void testGetChatHistory() {
        Long conversationId = 1L;
        List<Message> chatHistory = new ArrayList<>();
        chatHistory.add(new Message());
        chatHistory.add(new Message());

        when(getChatHistoryUseCase.getChatHistory(conversationId)).thenReturn(chatHistory);

        ResponseEntity<List<Message>> response = conversationsController.getChatHistory(conversationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatHistory, response.getBody());
    }

    @Test
    void testGetConversationNotFound() {
        Long conversationId = 1L;

        when(getConversationUseCase.getConversation(conversationId)).thenReturn(Optional.empty());

        ResponseEntity<Conversation> response = conversationsController.getConversation(conversationId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetUserConversations() {
        long userId = 123L;
        List<Conversation> conversations = new ArrayList<>();
        conversations.add(new Conversation());
        conversations.add(new Conversation());

        when(getAllConversationUseCase.getAllConversations(userId)).thenReturn(conversations);

        ResponseEntity<List<Conversation>> response = conversationsController.getUserConversations(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(conversations, response.getBody());
    }

    @Test
    void testCreateConversation() {
        CreateConversationRequest request = new CreateConversationRequest();
        CreateConversationResponse expectedResponse = CreateConversationResponse.builder().conversationId(123).build();
        when(createConversationUseCase.createConversation(request)).thenReturn(expectedResponse);

        ResponseEntity<CreateConversationResponse> response = conversationsController.createConversation(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}
