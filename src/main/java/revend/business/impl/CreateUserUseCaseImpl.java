package revend.business.impl;

import jakarta.transaction.Transactional;
import revend.business.CreateUserUseCase;
import revend.business.exception.EmailAlreadyExistsException;
import revend.domain.CreateUserRequest;
import revend.domain.CreateUserResponse;
import revend.persistence.UserRepository;
import revend.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        UserEntity savedUser = saveNewUser(request);

        return CreateUserResponse.builder()
                .userId(savedUser.getId())
                .build();
    }

    private UserEntity saveNewUser(CreateUserRequest request) {
        UserEntity newUser = UserEntity.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .build();
        return userRepository.save(newUser);
    }
}
