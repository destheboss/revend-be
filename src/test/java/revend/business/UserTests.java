package revend.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import revend.business.exception.EmailAlreadyExistsException;
import revend.business.exception.InvalidUserException;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.business.impl.*;
import revend.configuration.security.token.AccessToken;
import revend.domain.*;
import revend.persistence.entity.RoleEnum;
import revend.persistence.entity.UserEntity;
import revend.persistence.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCase;

    @InjectMocks
    private DeleteUserUseCaseImpl deleteUserUseCase;

    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCase;

    @InjectMocks
    private GetUsersUseCaseImpl getUsersUseCase;

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

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);

        CreateUserResponse response = createUserUseCase.createUser(request);

        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(any(UserEntity.class));
        assertNotNull(response, "Response should not be null");
        assertEquals(savedUserEntity.getId(), response.getUserId(), "The returned user ID should match the saved entity's ID");
    }

    @Test
    void createUserTest_EmailAlreadyExists() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("strongPassword123")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        Exception exception = assertThrows(EmailAlreadyExistsException.class, () ->
            createUserUseCase.createUser(request)
        );

        assertEquals("400 BAD_REQUEST \"EMAIL_ALREADY_EXISTS\"", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUserTest() {
        String email = "john.doe@example.com";
        doNothing().when(userRepository).deleteByEmail(email);

        deleteUserUseCase.deleteUser(email);

        verify(userRepository).deleteByEmail(email);
    }

    @Test
    void updateUserTest_Success() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("john.doe@example.com");
        request.setFirstName("John");
        request.setLastName("UpdatedLast");
        request.setPassword("newPassword123");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setEmail("john.doe@example.com");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setPassword("strongPassword123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        lenient().when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        lenient().when(requestAccessToken.getUserId()).thenReturn(1L);

        doAnswer(invocation -> invocation.getArgument(0)).when(userRepository).save(any(UserEntity.class));

        updateUserUseCase.updateUser(request);

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void updateUserTest_Unauthorized() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("john.doe@example.com");
        request.setFirstName("John");
        request.setLastName("UpdatedLast");
        request.setPassword("newPassword123");

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setEmail("john.doe@example.com");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setPassword("strongPassword123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataAccessException.class, () -> {
            updateUserUseCase.updateUser(request);
        });

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void updateUserTest_Failure_UserNotFound() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvalidUserException.class, () ->
                updateUserUseCase.updateUser(request)
        );

        assertEquals("400 BAD_REQUEST \"USER_EMAIL_INVALID\"", exception.getMessage());
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void getUser_WhenUserExists() {
        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setEmail("john.doe@example.com");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setPassword("strongPassword123");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        lenient().when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(true);
        lenient().when(requestAccessToken.getUserId()).thenReturn(1L);

        Optional<User> result = getUserUseCase.getUser(existingUser.getEmail());

        verify(userRepository, times(1)).findByEmail(existingUser.getEmail());

        assertTrue(result.isPresent());
        assertEquals(existingUser.getEmail(), result.get().getEmail());
    }

    @Test
    void getUser_WhenUnauthorized_ShouldThrowException() {
        String email = "test@example.com";
        UserEntity mockUser = new UserEntity();
        mockUser.setId(2L);
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(requestAccessToken.hasRole(RoleEnum.ADMIN.name())).thenReturn(false);
        when(requestAccessToken.getUserId()).thenReturn(1L);

        assertThrows(UnauthorizedDataAccessException.class, () -> {
            getUserUseCase.getUser(email);
        });
    }

    @Test
    void getUser_WhenUserDoesNotExist() {
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = getUserUseCase.getUser(email);

        assertFalse(result.isPresent());
    }

    @Test
    void getUsers_ReturnsUserList() {
        List<UserEntity> userList = List.of(
                new UserEntity(),
                new UserEntity()
        );
        when(userRepository.findAll()).thenReturn(userList);

        GetAllUsersResponse response = getUsersUseCase.getUsers(new GetAllUsersRequest());

        assertNotNull(response.getUsers());
        assertEquals(2, response.getUsers().size());
    }

    @Test
    void getUsers_ReturnsEmptyListWhenNoUsersFound() {
        when(userRepository.findAll()).thenReturn(List.of());

        GetAllUsersResponse response = getUsersUseCase.getUsers(new GetAllUsersRequest());

        assertNotNull(response.getUsers());
        assertTrue(response.getUsers().isEmpty());
    }
}
