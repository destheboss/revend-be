package revend.configuration.security.token;

public interface RefreshTokenEncoder {
    String encode(RefreshToken refreshToken);
}
