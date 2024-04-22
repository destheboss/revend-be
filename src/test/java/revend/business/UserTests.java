package revend.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import revend.business.exception.EmailAlreadyExistsException;
import revend.business.exception.InvalidUserException;
import revend.business.impl.*;
import revend.domain.*;
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
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);

        CreateUserResponse response = createUserUseCase.createUser(request);

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
        doAnswer(invocation -> invocation.getArgument(0)).when(userRepository).save(any(UserEntity.class));

        updateUserUseCase.updateUser(request);

        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(any(UserEntity.class));
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
        String email = "test@example.com";
        UserEntity mockUser = new UserEntity();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Optional<User> result = getUserUseCase.getUser(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
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
                new UserEntity(), // Add user details as necessary
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
