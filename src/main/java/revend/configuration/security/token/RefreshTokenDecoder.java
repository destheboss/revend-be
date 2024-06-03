package revend.configuration.security.token;

public interface RefreshTokenDecoder {
    RefreshToken decode(String refreshTokenEncoded);
}
