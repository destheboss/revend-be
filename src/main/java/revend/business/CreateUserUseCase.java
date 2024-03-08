package revend.business;

import revend.domain.CreateUserRequest;
import revend.domain.CreateUserResponse;
public interface CreateUserUseCase {
    CreateUserResponse createUser(CreateUserRequest request);
}
