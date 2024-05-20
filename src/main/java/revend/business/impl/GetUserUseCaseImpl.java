package revend.business.impl;

import revend.business.GetUserUseCase;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.configuration.security.token.AccessToken;
import revend.domain.User;
import revend.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.persistence.entity.RoleEnum;

import java.util.Optional;

@Service
@AllArgsConstructor

public class GetUserUseCaseImpl implements GetUserUseCase {
    private UserRepository userRepository;
    private AccessToken requestAccessToken;

    @Override
    public Optional<User> getUser(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email).map(UserConverter::convert);

        if (userOptional.isPresent()) {
            if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) &&
                    !requestAccessToken.getUserId().equals(userOptional.get().getId())) {
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
            return userOptional;
        }
        return userOptional;
    }
}
