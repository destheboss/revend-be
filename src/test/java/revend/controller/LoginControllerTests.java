package revend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import revend.business.LoginUseCase;
import revend.domain.LoginRequest;
import revend.domain.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTests {

    @Mock
    private LoginUseCase loginUseCase;

    @InjectMocks
    private LoginController loginController;

    @Test
    void login_ValidRequest_ReturnsCreatedResponse() {
        LoginResponse expectedResponse = new LoginResponse("dummyAccessToken");
        when(loginUseCase.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");

        ResponseEntity<LoginResponse> responseEntity = loginController.login(loginRequest);

        verify(loginUseCase, times(1)).login(loginRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
