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
    @GetMapping("{email}")
    public ResponseEntity<User> getUser(@PathVariable(value = "email") final String email) {
        return getUserUseCase.getUser(email)
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
    @DeleteMapping("{userEmail}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userEmail) {
        deleteUserUseCase.deleteUser(userEmail);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = createUserUseCase.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PutMapping("{email}")
    public ResponseEntity<Void> updateUser(@PathVariable("email") String email,
                                           @RequestBody @Valid UpdateUserRequest request) {
        request.setEmail(email);
        updateUserUseCase.updateUser(request);
        return ResponseEntity.noContent().build();
    }
}
