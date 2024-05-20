package revend.business;

import revend.domain.LoginRequest;
import revend.domain.LoginResponse;

public interface LoginUseCase {
    LoginResponse login(LoginRequest loginRequest);
}
