package revend.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import revend.business.exception.InvalidCredentialsException;
import revend.business.impl.LoginUseCaseImpl;
import revend.configuration.security.token.AccessToken;
import revend.configuration.security.token.AccessTokenEncoder;
import revend.domain.LoginRequest;
import revend.domain.LoginResponse;
import revend.persistence.UserRepository;
import revend.persistence.entity.RoleEnum;
import revend.persistence.entity.UserEntity;
import revend.persistence.entity.UserRoleEntity;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void login_SuccessfulLogin() {
        String email = "test@example.com";
        String password = "password123";
        String accessToken = "dummyAccessToken";

        UserRoleEntity role = new UserRoleEntity();
        role.setRole(RoleEnum.USER);
        Set<UserRoleEntity> roles = Set.of(role);

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRoles(roles);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessToken.class))).thenReturn(accessToken);

        LoginRequest request = new LoginRequest();
        request.setUsername(email);
        request.setPassword(password);
        LoginResponse response = loginUseCase.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
    }

    @SuppressWarnings("java:S2699")
    @Test
    void login_InvalidCredentials() {
        String email = "test@example.com";
        String password = "password123";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        Executable loginAction = () -> {
            LoginRequest request = new LoginRequest();
            request.setUsername(email);
            request.setPassword(password);
            loginUseCase.login(request);
        };

        assertThrows(InvalidCredentialsException.class, loginAction);
    }

    @Test
    void login_UserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setUsername(email);
        request.setPassword(password);

        assertThrows(InvalidCredentialsException.class, () -> {
            loginUseCase.login(request);
        });
    }

    @Test
    void access_Token_Generation() {
        String email = "test@example.com";
        String password = "password123";
        String accessToken = "dummyAccessToken";

        UserRoleEntity role = new UserRoleEntity();
        role.setRole(RoleEnum.USER);
        Set<UserRoleEntity> roles = Set.of(role);

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRoles(roles);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessToken.class))).thenReturn(accessToken);

        LoginRequest request = new LoginRequest();
        request.setUsername(email);
        request.setPassword(password);
        LoginResponse response = loginUseCase.login(request);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
    }
}
