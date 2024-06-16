package revend.business.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import revend.business.CreateUserUseCase;
import revend.business.exception.EmailAlreadyExistsException;
import revend.business.exception.ImageProcessingException;
import revend.domain.CreateUserRequest;
import revend.domain.CreateUserResponse;
import revend.persistence.UserRepository;
import revend.persistence.entity.RoleEnum;
import revend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import revend.persistence.entity.UserRoleEntity;

import java.util.Set;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        byte[] imageData = null;
        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            try {
                imageData = ImageDataConversion.decodeBase64ToBytes(request.getImageBase64());
            } catch (ImageProcessingException e) {
                throw new ImageProcessingException("Failed to decode image data");
            }
        }

        UserEntity savedUser = saveNewUser(request, request.getPassword(), imageData);

        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    private UserEntity saveNewUser(CreateUserRequest request, String password, byte[] imageData) {
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(encodedPassword)
                .imageData(imageData)
                .build();

        newUser.setUserRoles(Set.of(
                UserRoleEntity.builder()
                        .user(newUser)
                        .role(RoleEnum.USER)
                        .build()));
        return userRepository.save(newUser);
    }
}
