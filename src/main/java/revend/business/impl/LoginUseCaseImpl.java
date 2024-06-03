package revend.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import revend.business.LoginUseCase;
import revend.business.exception.InvalidCredentialsException;
import revend.business.exception.InvalidRefreshTokenException;
import revend.configuration.security.token.AccessTokenEncoder;
import revend.configuration.security.token.RefreshTokenDecoder;
import revend.configuration.security.token.RefreshTokenEncoder;
import revend.configuration.security.token.impl.AccessTokenImplementation;
import revend.configuration.security.token.impl.RefreshTokenImpl;
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
    private final RefreshTokenEncoder refreshTokenEncoder;
    private final RefreshTokenDecoder refreshTokenDecoder;

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
        String refreshToken = generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String refresh(String refreshToken) {
        var decodedRefreshToken = refreshTokenDecoder.decode(refreshToken);

        Optional<UserEntity> user = userRepository.findById(decodedRefreshToken.getUserId());
        if (user.isEmpty()) {
            throw new InvalidRefreshTokenException();
        }

        return generateAccessToken(user);
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

    private String generateRefreshToken(Optional<UserEntity> userOptional) {
        UserEntity user = userOptional.orElseThrow(InvalidCredentialsException::new);

        Long userId = user.getId();
        List<String> roles = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();

        return refreshTokenEncoder.encode(
                new RefreshTokenImpl(user.getEmail(), userId, roles));
    }
}
