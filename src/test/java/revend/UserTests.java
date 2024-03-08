package revend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import revend.business.impl.CreateUserUseCaseImpl;
import revend.domain.CreateUserRequest;
import revend.domain.CreateUserResponse;
import revend.persistence.entity.UserEntity;
import revend.persistence.UserRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    @Test
    void createUserTest() {
        CreateUserRequest request = CreateUserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("strongPassword123")
                .build();

        UserEntity savedUserEntity = new UserEntity();
        savedUserEntity.setId(1L);
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);

        CreateUserResponse response = createUserUseCase.createUser(request);

        verify(userRepository).save(any(UserEntity.class));
    }
}
