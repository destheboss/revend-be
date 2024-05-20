package revend.business.impl;

import revend.business.UpdateUserUseCase;
import revend.business.exception.InvalidUserException;
import revend.business.exception.UnauthorizedDataAccessException;
import revend.configuration.security.token.AccessToken;
import revend.domain.UpdateUserRequest;
import revend.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.persistence.entity.RoleEnum;
import revend.persistence.entity.UserEntity;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;
    private AccessToken requestAccessToken;

    @Override
    public void updateUser(UpdateUserRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new InvalidUserException("USER_EMAIL_INVALID");
        }

        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) &&
                !requestAccessToken.getUserId().equals(userOptional.get().getId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        UserEntity user = userOptional.get();
        updateFields(request, user);
    }

    private void updateFields(UpdateUserRequest request, UserEntity user) {
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());

        userRepository.save(user);
    }
}
