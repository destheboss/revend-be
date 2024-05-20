package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import revend.business.LoginUseCase;
import revend.business.exception.InvalidCredentialsException;
import revend.configuration.security.token.AccessTokenEncoder;
import revend.configuration.security.token.impl.AccessTokenImplementation;
import revend.domain.LoginRequest;
import revend.domain.LoginResponse;
import revend.persistence.UserRepository;
import revend.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Optional<UserEntity> user = userRepository.findByEmail(loginRequest.getUsername());
        if (user.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), user.get().getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(Optional<UserEntity> userOptional) {
        UserEntity user = userOptional.orElseThrow(InvalidCredentialsException::new);

        Long userId = user.getId();
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return accessTokenEncoder.encode(
                new AccessTokenImplementation(user.getEmail(), userId, roles));
    }
}
