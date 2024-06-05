package revend.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import revend.business.*;
import revend.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UsersController {
    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("{userId}")
    public ResponseEntity<User> getUser(@PathVariable(value = "userId") final Long userId) {
        return getUserUseCase.getUser(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RolesAllowed({"ADMIN"})
    @GetMapping
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        GetAllUsersRequest request = GetAllUsersRequest.builder().build();
        GetAllUsersResponse response = getUsersUseCase.getUsers(request);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"ADMIN"})
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        deleteUserUseCase.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PutMapping("{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable("userId") long userId,
                                           @RequestBody @Valid UpdateUserRequest request) {
        request.setId(userId);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }
}
