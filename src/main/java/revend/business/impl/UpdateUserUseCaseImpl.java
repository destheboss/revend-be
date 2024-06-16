package revend.business.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import revend.business.UpdateUserUseCase;
import revend.business.exception.ImageProcessingException;
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
    private final UserRepository userRepository;
    private final AccessToken requestAccessToken;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateUser(UpdateUserRequest request) {
        Optional<UserEntity> userOptional = userRepository.findById(request.getId());

        if (userOptional.isEmpty()) {
            throw new InvalidUserException("USER_EMAIL_INVALID");
        }

        if (!requestAccessToken.hasRole(RoleEnum.ADMIN.name()) &&
                !requestAccessToken.getUserId().equals(userOptional.get().getId())) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        byte[] imageData = null;
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            try {
                imageData = ImageDataConversion.decodeBase64ToBytes(request.getImageBase64());
            } catch (ImageProcessingException e) {
                throw new ImageProcessingException("Failed to decode image data");
            }
        }

        UserEntity user = userOptional.get();
        updateFields(request, user, imageData);
    }

    private void updateFields(UpdateUserRequest request, UserEntity user, byte[] imageData) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(encodedPassword);
        user.setImageData(imageData);

        userRepository.save(user);
    }
}
