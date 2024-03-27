package revend.business.impl;

import revend.business.UpdateUserUseCase;
import revend.business.exception.InvalidUserException;
import revend.domain.UpdateUserRequest;
import revend.persistence.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.persistence.entity.UserEntity;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private UserRepository userRepository;

    @Override
    public void updateUser(UpdateUserRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new InvalidUserException("USER_EMAIL_INVALID");
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
