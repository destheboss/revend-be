package revend.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.configuration.security.token.AccessToken;
import revend.persistence.ConversationRepository;
import revend.persistence.MessageRepository;
import revend.persistence.UserRepository;
import revend.persistence.entity.*;
import revend.domain.*;
import revend.business.*;
import revend.business.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversationMessageTests {
    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    CreateConversationUseCaseImpl createConversationUseCase;
}
