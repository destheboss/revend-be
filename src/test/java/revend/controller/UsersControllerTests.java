package revend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import revend.business.*;
import revend.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersControllerTests {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private GetUserUseCase getUserUseCase;

    @Mock
    private GetUsersUseCase getUsersUseCase;

    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @InjectMocks
    private UsersController usersController;

    @Test
    void testGetUser_ExistingUser_ReturnsUser() {
        Long id = 1L;
        User user = new User();
        when(getUserUseCase.getUser(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = usersController.getUser(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void testGetUser_NonExistingUser_ReturnsNotFound() {
        Long id = 1L;
        when(getUserUseCase.getUser(id)).thenReturn(Optional.empty());

        ResponseEntity<User> response = usersController.getUser(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllUsers_ValidRequest_ReturnsOk() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "John", "Doe", "john@example.com", "password", ""));
        users.add(new User(2L, "Jane", "Doe", "jane@example.com", "password", ""));
        GetAllUsersRequest request = GetAllUsersRequest.builder().build();
        GetAllUsersResponse expectedResponse = new GetAllUsersResponse(users);

        when(getUsersUseCase.getUsers(request)).thenReturn(expectedResponse);

        ResponseEntity<GetAllUsersResponse> response = usersController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testDeleteUser_UserDeleted_ReturnsNoContent() {
        Long id = 1L;

        ResponseEntity<Void> response = usersController.deleteUser(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(deleteUserUseCase, times(1)).deleteUser(id);
    }

    @Test
    void testCreateUser_ValidRequest_ReturnsCreated() {
        CreateUserRequest request = new CreateUserRequest();
        CreateUserResponse expectedResponse = CreateUserResponse.builder().userId(123).build();
        when(createUserUseCase.createUser(request)).thenReturn(expectedResponse);

        ResponseEntity<CreateUserResponse> response = usersController.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testUpdateUser_ValidRequest_ReturnsNoContent() {
        long id = 1L;

        UpdateUserRequest request = new UpdateUserRequest();
        doNothing().when(updateUserUseCase).updateUser(request);

        ResponseEntity<Void> response = usersController.updateUser(id, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}