package revend.business;

import revend.domain.User;

import java.util.Optional;

public interface GetUserUseCase {
    Optional<User> getUser(Long id);
}
